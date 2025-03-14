package lk.ijse.back_end.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
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
    @Min(1)
    @Max(10000)
    private int quantity;
//    @Column(columnDefinition = "LONGTEXT",name = "image_url")
    private String imageURL;

    @Version
    private int version;
    @ManyToOne
    @JoinColumn(name = "user_uid", referencedColumnName = "uid")
    private User user;

    @OneToMany(mappedBy = "listing")
    private List<Bid> bids;

}