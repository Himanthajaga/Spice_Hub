package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

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

    public AddressDTO() {
    }

    public AddressDTO(UUID id, String street, String city, String country, String postalCode, UUID userId) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
