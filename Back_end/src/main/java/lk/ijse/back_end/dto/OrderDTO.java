package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public class OrderDTO {

    private UUID oid;
    @NotBlank(message = "Order date is required")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "Invalid date")
    private Date orderDate;
    @NotBlank(message = "Total amount is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid total amount")
    @Size(max = 255)
    private double totalAmount;
    @NotNull
    private UUID userId;
    @NotNull
    private List<BidSpiceListningDTO> orderItems;
    @NotNull
    private List<PaymentDTO> payments;

    public OrderDTO() {
    }

    public OrderDTO(UUID oid, Date orderDate, double totalAmount, UUID userId, List<BidSpiceListningDTO> orderItems, List<PaymentDTO> payments) {
        this.oid = oid;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.orderItems = orderItems;
        this.payments = payments;
    }

    public UUID getOid() {
        return oid;
    }

    public void setOid(UUID oid) {
        this.oid = oid;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<BidSpiceListningDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<BidSpiceListningDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }
}
