package lk.ijse.back_end.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pid;
    private double amount;
    private Date paymentdate;
    private String paymentmethod;

    @ManyToOne
    @JoinColumn(name = "oid")
    private Order order;

    public Payment() {
    }

    public Payment(UUID pid, double amount, Date paymentdate, String paymentmethod, Order order) {
        this.pid = pid;
        this.amount = amount;
        this.paymentdate = paymentdate;
        this.paymentmethod = paymentmethod;
        this.order = order;
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

    public Date getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
