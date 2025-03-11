package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

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
