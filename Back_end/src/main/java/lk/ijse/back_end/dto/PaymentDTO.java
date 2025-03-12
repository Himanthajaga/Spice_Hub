package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "Invalid date")
    private Date paymentDate;
    @NotBlank(message = "Payment method is required")
    @Size(max = 255)
    private String paymentMethod;
    @NotNull
    private UUID orderId;
}
