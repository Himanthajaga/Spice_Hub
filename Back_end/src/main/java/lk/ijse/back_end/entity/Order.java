package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
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
}
