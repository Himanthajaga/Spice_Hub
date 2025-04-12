package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pid;
    private double amount;
    private Date paymentdate;
    private String paymentMethodId;
    private String bidId;
    private String spiceOwnerEmail;
    private String buyerEmail;
    private String friendlyId;
}
