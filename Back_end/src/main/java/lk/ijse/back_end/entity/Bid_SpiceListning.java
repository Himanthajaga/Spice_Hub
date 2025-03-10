package lk.ijse.back_end.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

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
    @JoinColumn(name = "oid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "id")
    private Spice spice;

    public Bid_SpiceListning() {
    }

    public Bid_SpiceListning(UUID bidId, String spiceId, String bidDate, String bidTime, String bidPrice, String bidQty) {
        this.bidId = bidId;
        this.spiceId = spiceId;
        this.bidDate = bidDate;
        this.bidTime = bidTime;
        this.bidPrice = bidPrice;
        this.bidQty = bidQty;
    }

    public UUID getBidId() {
        return bidId;
    }

    public void setBidId(UUID bidId) {
        this.bidId = bidId;
    }

    public String getSpiceId() {
        return spiceId;
    }

    public void setSpiceId(String spiceId) {
        this.spiceId = spiceId;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidQty() {
        return bidQty;
    }

    public void setBidQty(String bidQty) {
        this.bidQty = bidQty;
    }

    @Override
    public String toString() {
        return "Bid_SpiceListning{" +
                "bidId='" + bidId + '\'' +
                ", spiceId='" + spiceId + '\'' +
                ", bidDate='" + bidDate + '\'' +
                ", bidTime='" + bidTime + '\'' +
                ", bidPrice='" + bidPrice + '\'' +
                ", bidQty='" + bidQty + '\'' +
                '}';
    }
}
