package lk.ijse.back_end.dto;

import java.util.UUID;

public class AddressDTO {
    private UUID id;
    private String street;
    private String city;
    private String country;
    private String postalCode;
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
