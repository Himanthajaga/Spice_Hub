package lk.ijse.back_end.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor

public class Spice implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String name;
    private String description;
    private String category;
    private double price;
    private int quantity;
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = " user_uid")
    private User user;

    @OneToMany(mappedBy = "listing")
    private List<Bid>bids;

    public Spice() {
    }

    public Spice(Long id, String name, String description, String category, double price, int quantity, String imageURL, User user, List<Bid> bids) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.user = user;
        this.bids = bids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
