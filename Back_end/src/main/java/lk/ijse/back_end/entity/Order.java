package lk.ijse.back_end.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID oid;
    private Date orderDate;
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    public Order() {
    }

    public Order(UUID oid, Date orderDate, double totalAmount, User user) {
        this.oid = oid;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
