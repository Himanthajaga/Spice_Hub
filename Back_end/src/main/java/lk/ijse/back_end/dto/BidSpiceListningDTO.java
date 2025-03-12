package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BidSpiceListningDTO {

    private UUID id;
    @NotBlank(message = "Quantity is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid quantity")
    @Size(max = 255)
    private int quantity;
    @NotBlank(message = "Price is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid price")
    @Size(max = 255)
    private double price;
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID spiceId;
}
