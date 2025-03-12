package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDTO {
    private UUID id;
    @NotBlank(message = "Street is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Invalid street")
    @Size(min = 3, max = 50, message = "Street must be between 3 and 50 characters")
    private String street;
    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid city")
    @Size(min = 3, max = 50, message = "City must be between 3 and 50 characters")
    private String city;
    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid country")
    @Size(min = 3, max = 50, message = "Country must be between 3 and 50 characters")
    private String country;
    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid postal code")
    @Size(min = 3, max = 50, message = "Postal code must be between 3 and 50 characters")
    private String postalCode;
    @NotNull
    private UUID userId;
}
