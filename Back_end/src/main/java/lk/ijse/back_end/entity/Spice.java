package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Spice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String category;
    private double price;
    private String quantity;
//    @Column(columnDefinition = "LONGTEXT",name = "image_url")
    private String imageURL;
    private String location;
    private Date listedTime; // Add this line

    @Version
    private int version;
    @ManyToOne
    @JoinColumn(name = "user_uid", referencedColumnName = "uid")
    private User user;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Bid> bids;

}