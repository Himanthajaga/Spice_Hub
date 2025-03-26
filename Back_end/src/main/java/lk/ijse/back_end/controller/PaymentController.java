package lk.ijse.back_end.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lk.ijse.back_end.dto.PaymentDTO;
import lk.ijse.back_end.service.EmailService;
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

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "http://localhost:63342")
public class PaymentController {
    private  static final Logger log = LoggerFactory.getLogger(SpiceController.class);

    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private BidServiceImpl bidService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SpiceServiceImpl spiceService;
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
        return new ResponseUtil(201, "Payment saved successfully", paymentService.getAll());
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
            spiceService.delete(paymentDTO.getSpiceId());

            // Send email to the user who listed the spice

            String spiceOwnerEmail = paymentDTO.getSpiceOwnerEmail();
            String buyerEmail = paymentDTO.getBuyerEmail();
            String spiceName = paymentDTO.getSpiceName();

            if (spiceOwnerEmail != null && !spiceOwnerEmail.isEmpty()) {
                String ownerEmailContent = "Payment success for your listed spice: " + spiceName;
                emailService.sendEmail(spiceOwnerEmail, "Payment Success", ownerEmailContent);
            } else {
                // Log or handle the case where spiceOwnerEmail is null or empty
                log.error("Spice owner email is null or empty");
            }

            if (buyerEmail != null && !buyerEmail.isEmpty()) {
                String buyerEmailContent = "Your payment was successfully made for the spice: " + spiceName;
                emailService.sendEmail(buyerEmail, "Payment Confirmation", buyerEmailContent);
            } else {
                // Log or handle the case where buyerEmail is null or empty
                log.error("Buyer email is null or empty");
            }
            return new ResponseUtil(201, "Payment confirmed successfully", paymentIntent.getClientSecret());
        } catch (StripeException e) {
            return new ResponseUtil(500, "Payment confirmation failed: " + e.getMessage(), null);
        }
    }
}
