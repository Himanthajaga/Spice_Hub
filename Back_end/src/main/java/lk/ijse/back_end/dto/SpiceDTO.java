package lk.ijse.back_end.dto;

import lk.ijse.back_end.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpiceDTO<T> implements Serializable {
    private UUID id;

    private String name;

    private String description;


    private String category;


    private Double price;


    private String quantity;

    private T imageURL;
    private String location;
    private UUID sellerId;
    private boolean active;
    private Date listedTime; // Add this line

}