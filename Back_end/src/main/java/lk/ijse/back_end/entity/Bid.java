package lk.ijse.back_end.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity

public class Bid implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double bidAmount;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Spice listing;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.PENDING;

    private LocalDateTime bidTime = LocalDateTime.now();
    enum BidStatus{
        PENDING,
        ACCEPTED,
        REJECTED
    }

    public Bid() {
    }

    public Bid(Long id, double bidAmount, Spice listening, User user, BidStatus status, LocalDateTime bidTime) {
        this.id = id;
        this.bidAmount = bidAmount;
        this.listing = listening;
        this.user = user;
        this.status = status;
        this.bidTime = bidTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Spice getListening() {
        return listing;
    }

    public void setListening(Spice listening) {
        this.listing = listening;
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

