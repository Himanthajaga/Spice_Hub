package lk.ijse.back_end.dto;

import java.util.UUID;

public class BidSpiceListningDTO {
    private UUID id;
    private int quantity;
    private double price;
    private UUID orderId;
    private UUID spiceId;

    public BidSpiceListningDTO() {
    }

    public BidSpiceListningDTO(UUID id, int quantity, double price, UUID orderId, UUID spiceId) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.spiceId = spiceId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getSpiceId() {
        return spiceId;
    }

    public void setSpiceId(UUID spiceId) {
        this.spiceId = spiceId;
    }
}
