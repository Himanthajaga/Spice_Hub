package lk.ijse.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BidDTO<T>implements Serializable {
    private UUID id;

    private UUID userId;

    private UUID listingId;

    private double bidAmount;

    private String status;
    private LocalDateTime bidTime;
    private String imageURL;
}
