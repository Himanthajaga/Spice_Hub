package lk.ijse.back_end.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
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
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "http://localhost:63342")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(SpiceController.class);

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
    @Value("${stripe.api.key}")
    private String stripeApiKey;

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
            log.info("Received paymentDTO: {}", paymentDTO);
            log.info("Spice ID received: {}", paymentDTO.getSpiceId());

//            // Fetch the spice details to get the owner email
//            SpiceDTO<String> spiceDTO = spiceService.getById(UUID.fromString(paymentDTO.getSpiceId()));
//            if (spiceDTO == null) {
//                log.error("Spice not found with id: {}", paymentDTO.getSpiceId());
//                return new ResponseUtil(404, "Spice not found", null);
//            }
//            log.info("SpiceDTO retrieved: {}", spiceDTO);
//            if (spiceDTO.getSellerId() == null) {
//                log.error("Spice seller ID is null for spice id: {}", paymentDTO.getSpiceId());
//                return new ResponseUtil(400, "Spice seller ID must not be null", null);
//            }
//            User spiceOwner = userService.findById(spiceDTO.getSellerId());
//            if (spiceOwner == null) {
//                log.error("Spice owner not found with id: {}", spiceDTO.getSellerId());
//                return new ResponseUtil(404, "Spice owner not found", null);
//            }
//            paymentDTO.setSpiceOwnerEmail(spiceOwner.getEmail());
//            paymentDTO.setSpiceName(spiceDTO.getName());
//
// //Fetch the bid details to get the buyer email
//            BidDTO<String> bidDTO = bidService.getBidById(UUID.fromString(paymentDTO.getBidId()));
//            if (bidDTO == null) {
//                log.error("Bid not found with id: {}", paymentDTO.getBidId());
//                return new ResponseUtil(404, "Bid not found", null);
//            }
//            log.info("BidDTO retrieved: {}", bidDTO);
//            if (bidDTO.getUserId() == null) {
//                log.error("Bid user ID is null for bid id: {}", paymentDTO.getBidId());
//                return new ResponseUtil(400, "Bid user ID must not be null", null);
//            }
//            User buyer = userService.findById(bidDTO.getUserId());
//            if (buyer == null) {
//                log.error("Buyer not found with id: {}", bidDTO.getUserId());
//                return new ResponseUtil(404, "Buyer not found", null);
//            }
//            paymentDTO.setBuyerEmail(buyer.getEmail());

            // Create a PaymentIntent
            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount((long) (paymentDTO.getAmount() * 100)) // Amount in cents
                    .setCurrency("usd")
                    .setPaymentMethod(paymentDTO.getPaymentMethodId())
                    .setConfirm(true)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);

            // Save payment details
            paymentDTO.setPaymentMethodId(paymentIntent.getId());
            paymentDTO.setPaymentDate(Date.valueOf(LocalDate.now()));
            paymentService.save(paymentDTO);

            // Delete the bid
            bidService.delete(paymentDTO.getBidId());

            // Ensure spiceId is not null before calling delete
            if (paymentDTO.getSpiceId() != null && !paymentDTO.getSpiceId().isEmpty()) {
                spiceService.delete(paymentDTO.getSpiceId());
            } else {
                log.error("Spice ID is null or empty");
                throw new IllegalArgumentException("Spice ID cannot be null or empty");
            }

            // Send email to the user who listed the spice
            String spiceOwnerEmail = paymentDTO.getSpiceOwnerEmail();
            String buyerEmail = paymentDTO.getBuyerEmail();
            String spiceName = paymentDTO.getSpiceName();

            if (spiceOwnerEmail != null && !spiceOwnerEmail.isEmpty()) {
                String ownerEmailContent = "Payment success for your listed spice: " + spiceName;
                emailService.sendEmail(spiceOwnerEmail, "Payment Success", ownerEmailContent);
            } else {
                log.error("Spice owner email is null or empty");
            }

            if (buyerEmail != null && !buyerEmail.isEmpty()) {
                String buyerEmailContent = "Your payment was successfully made for the spice: " + spiceName;
                emailService.sendEmail(buyerEmail, "Payment Confirmation", buyerEmailContent);
            } else {
                log.error("Buyer email is null or empty");
            }

            return new ResponseUtil(201, "Payment confirmed successfully", paymentIntent.getClientSecret());
        } catch (StripeException e) {
            return new ResponseUtil(500, "Payment confirmation failed: " + e.getMessage(), null);
        }
    }
}

