package lk.ijse.back_end.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BidDTO<T> implements Serializable {
    private UUID id;

    private UUID userId;

    private UUID listingId;

    private double bidAmount;

    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime bidTime;
    private T imageURL;
    private String listingName; // Add this field
    private String listingDescription; // Add this field
}
