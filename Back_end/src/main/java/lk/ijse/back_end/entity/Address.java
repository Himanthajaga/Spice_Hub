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
@Table(name = "address")
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String street;
    private String city;
    private String country;
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
}
