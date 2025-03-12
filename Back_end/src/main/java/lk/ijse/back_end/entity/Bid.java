package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Bid implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double bidAmount;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Spice listing;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.PENDING;

    private LocalDateTime bidTime = LocalDateTime.now();

    public enum BidStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}