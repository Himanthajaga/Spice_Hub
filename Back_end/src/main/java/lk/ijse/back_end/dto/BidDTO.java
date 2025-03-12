package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BidDTO {
    private UUID id;
    @NotNull
    private Long userId;
    @NotNull
    private Long listingId;
    @NotNull
    @NotBlank(message = "Bid amount is required")
    private double bidAmount;
    @NotBlank(message = "Status is required")
    @Size(max = 255)
    private String status;
    private LocalDateTime bidTime;
}
