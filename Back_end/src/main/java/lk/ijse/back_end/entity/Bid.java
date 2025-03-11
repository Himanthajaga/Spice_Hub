package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

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

    public Bid() {
    }

    public Bid(UUID id, double bidAmount, Spice listing, User user, BidStatus status, LocalDateTime bidTime) {
        this.id = id;
        this.bidAmount = bidAmount;
        this.listing = listing;
        this.user = user;
        this.status = status;
        this.bidTime = bidTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Spice getListing() {
        return listing;
    }

    public void setListing(Spice listing) {
        this.listing = listing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
}