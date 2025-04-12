package lk.ijse.back_end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.ijse.back_end.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {

    private UUID pid;
    @NotBlank(message = "Amount is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid amount")
    @Size(max = 10)
    private double amount;
    @NotBlank
    private Date paymentDate;
    @NotBlank(message = "Payment method ID is required")
    private String paymentMethodId;
    @NotBlank(message = "Bid ID is required")
    private String bidId;
    @NotBlank(message = "Spice owner email is required")
    @Email(message = "Invalid email format")
    private String spiceOwnerEmail;

    @NotBlank(message = "Buyer email is required")
    @Email(message = "Invalid email format")
    private String buyerEmail;
    private String spiceName;
    @NotBlank(message = "Spice ID is required")
    private String spiceId;
    private String friendlyId;
//    public PaymentDTO(Payment payment) {
//    }
}