package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO <T>implements Serializable {

    private UUID id;
    @NotBlank(message = "Category name is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid category name")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;
    private String description;
    private T imageURL;
}
