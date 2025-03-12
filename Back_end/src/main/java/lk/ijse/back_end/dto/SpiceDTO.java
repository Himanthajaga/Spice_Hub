package lk.ijse.back_end.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
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


    private int quantity;

    private T imageURL;
    private Long sellerId;
    private boolean active;
}