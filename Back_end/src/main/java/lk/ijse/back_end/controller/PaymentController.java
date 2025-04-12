package lk.ijse.back_end.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lk.ijse.back_end.config.PayHereConfig;
import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.dto.PaymentDTO;
import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.service.EmailService;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.service.impl.BidServiceImpl;
import lk.ijse.back_end.service.impl.PaymentServiceImpl;
import lk.ijse.back_end.service.impl.SpiceServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "http://localhost:63342")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(SpiceController.class);
private final PayHereConfig payHereConfig;
    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private BidServiceImpl bidService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SpiceServiceImpl spiceService;
    @Autowired
    private UserService userService;
    @Value("${payhere.merchant.secret}")
    private String stripeApiKey;

    public PaymentController(PayHereConfig payHereConfig) {
        this.payHereConfig = payHereConfig;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping(path = "/save")
    public ResponseUtil savePayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.save(paymentDTO);
        return new ResponseUtil(201, "Payment saved successfully", null);
    }

    @GetMapping(path = "/get")
    public ResponseUtil getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return new ResponseUtil(200, "Payments retrieved successfully", payments);
    }


    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deletePayment(@PathVariable String id) {
        paymentService.delete(id);
        return new ResponseUtil(201, "Payment Deleted Successfully", null);
    }

    @PutMapping(path = "/update")
    public ResponseUtil updatePayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.update(paymentDTO);
        return new ResponseUtil(201, "Payment Updated Successfully", null);
    }

    @PostMapping(path = "/confirm")
    public ResponseUtil confirmPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            // Validate required fields
            if (paymentDTO.getBidId() == null || paymentDTO.getSpiceId() == null) {
                return new ResponseUtil(400, "Bid ID or Spice ID is missing", null);
            }

            paymentDTO.setPaymentDate(Date.valueOf(LocalDate.now()));
            paymentService.save(paymentDTO);

            try {
                bidService.delete(paymentDTO.getBidId());
            } catch (Exception e) {
                log.error("Failed to delete bid: {}", e.getMessage());
            }

            try {
                spiceService.delete(paymentDTO.getSpiceId());
            } catch (Exception e) {
                log.error("Failed to delete spice: {}", e.getMessage());
            }

            if (paymentDTO.getSpiceOwnerEmail() != null) {
                emailService.sendEmail(paymentDTO.getSpiceOwnerEmail(), "Payment Success",
                        "Payment success for your listed spice: " + paymentDTO.getSpiceName());
            }

            if (paymentDTO.getBuyerEmail() != null) {
                emailService.sendEmail(paymentDTO.getBuyerEmail(), "Payment Confirmation",
                        "Your payment was successfully made for the spice: " + paymentDTO.getSpiceName());
            }

            return new ResponseUtil(201, "Payment confirmed successfully", null);
        } catch (Exception e) {
            log.error("Payment confirmation failed: {}", e.getMessage());
            return new ResponseUtil(500, "Payment confirmation failed: " + e.getMessage(), null);
        }
    }
    @PostMapping("/notify")
    public ResponseEntity<String> handlePaymentNotification(@RequestParam Map<String, String> params) {
        log.info("Received payment notification: {}", params);

        try {
            String merchantId = params.get("merchant_id");
            String orderId = params.get("order_id");
            String payhereAmount = params.get("payhere_amount");
            String payhereCurrency = params.get("payhere_currency");
            String statusCode = params.get("status_code");
            String md5sig = params.get("md5sig");

            // Validate required parameters
            if (merchantId == null || orderId == null || payhereAmount == null ||
                    payhereCurrency == null || statusCode == null || md5sig == null) {
                log.error("Missing required parameters in payment notification");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment verification failed: Missing required parameters");
            }

            // Validate amount
            double amount;
            try {
                amount = Double.parseDouble(payhereAmount);
            } catch (NumberFormatException e) {
                log.error("Invalid amount format: {}", payhereAmount);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment verification failed: Invalid amount format");
            }

            // Generate the local MD5 signature
            String localMd5sig = generateMd5Sig(
                    merchantId, orderId, payhereAmount, payhereCurrency, statusCode, payHereConfig.getMerchantSecret()
            );

            // Verify the MD5 signature
            if (!localMd5sig.equals(md5sig)) {
                log.error("Invalid MD5 signature. Expected: {}, Received: {}", localMd5sig, md5sig);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment verification failed: Invalid MD5 signature");
            }

            // Check the payment status
            if ("2".equals(statusCode)) {
                // Payment successful
                log.info("Payment successful for orderId: {}", orderId);
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setBidId(orderId);
                paymentDTO.setAmount(amount);
                paymentDTO.setPaymentDate(Date.valueOf(LocalDate.now()));
                paymentDTO.setPaymentMethodId("PAYHERE");
                paymentService.save(paymentDTO);

                return ResponseEntity.ok("Payment verified and processed successfully");
            } else {
                // Payment declined or failed
                String statusMessage = params.get("status_message");
                log.warn("Payment declined: {}", statusMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment declined: " + statusMessage);
            }
        } catch (Exception e) {
            log.error("Error handling payment notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment notification failed: " + e.getMessage());
        }
    }

    private String generateMd5Sig(String merchantId, String orderId, String amount, String currency, String statusCode, String merchantSecret) {
        // Validate input parameters
        if (merchantId == null || orderId == null || amount == null || currency == null || statusCode == null || merchantSecret == null) {
            throw new IllegalArgumentException("One or more required parameters are null");
        }

        // Generate MD5 signature
        String rawData = merchantId + orderId + amount + currency + statusCode + merchantSecret;
        return DigestUtils.md5DigestAsHex(rawData.getBytes()).toUpperCase();
    }

    private String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

