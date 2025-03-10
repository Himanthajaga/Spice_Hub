package lk.ijse.back_end.dto;

import java.sql.Date;
import java.util.UUID;

public class PaymentDTO {
    private UUID pid;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
    private UUID orderId;

    public PaymentDTO() {
    }

    public PaymentDTO(UUID pid, double amount, Date paymentDate, String paymentMethod, UUID orderId) {
        this.pid = pid;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.orderId = orderId;
    }

    public UUID getPid() {
        return pid;
    }

    public void setPid(UUID pid) {
        this.pid = pid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
