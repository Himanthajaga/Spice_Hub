package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "bid_spicelistning")
public class Bid_SpiceListning implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bidId;
    private String spiceId;
    private String bidDate;
    private String bidTime;
    private String bidPrice;
    private String bidQty;

    @ManyToOne
    @JoinColumn(name = "id")
    private Spice spice;

}
