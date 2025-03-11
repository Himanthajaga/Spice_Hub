package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
public class Spice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String category;
    private double price;
    private int quantity;
    private String imageURL;

    @Version
    private int version;

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @ManyToOne
    @JoinColumn(name = "user_uid")
    private User user;

    @OneToMany(mappedBy = "listing")
    private List<Bid> bids;

    public Spice() {
    }

    public Spice(UUID id, String name, String description, String category, double price, int quantity, String imageURL, int version, User user, List<Bid> bids) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.version = version;
        this.user = user;
        this.bids = bids;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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