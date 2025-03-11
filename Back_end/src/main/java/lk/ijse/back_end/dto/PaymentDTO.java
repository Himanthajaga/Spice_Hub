package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.UUID;

public class PaymentDTO {

    private UUID pid;
    @NotBlank(message = "Amount is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid amount")
    @Size(max = 10)
    private double amount;
    @NotBlank
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "Invalid date")
    private Date paymentDate;
    @NotBlank(message = "Payment method is required")
    @Size(max = 255)
    private String paymentMethod;
    @NotNull
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
