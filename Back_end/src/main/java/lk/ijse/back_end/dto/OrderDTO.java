package lk.ijse.back_end.dto;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public class OrderDTO {
    private UUID oid;
    private Date orderDate;
    private double totalAmount;
    private UUID userId;
    private List<BidSpiceListningDTO> orderItems;
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
