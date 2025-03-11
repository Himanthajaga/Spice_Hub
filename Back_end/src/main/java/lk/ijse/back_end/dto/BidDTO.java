package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;


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

    public BidDTO() {
    }

    public BidDTO(UUID id, Long userId, Long listingId, double bidAmount, String status, LocalDateTime bidTime) {
        this.id = id;
        this.userId = userId;
        this.listingId = listingId;
        this.bidAmount = bidAmount;
        this.status = status;
        this.bidTime = bidTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
