package lk.ijse.back_end.dto;

import java.time.LocalDateTime;


public class BidDTO {
    private Long id;
    private Long userId;
    private Long listingId;
    private double bidAmount;
    private String status;
    private LocalDateTime bidTime;

    public BidDTO() {
    }

    public BidDTO(Long id, Long userId, Long listingId, double bidAmount, String status, LocalDateTime bidTime) {
        this.id = id;
        this.userId = userId;
        this.listingId = listingId;
        this.bidAmount = bidAmount;
        this.status = status;
        this.bidTime = bidTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
}
