package lk.ijse.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class SpiceDTO {
    private UUID id;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid name")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid description")
    private String description;
    @NotBlank(message = "Category is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid category")
    @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
    private String category;
    @NotBlank(message = "Price is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid price")
    @Size(min = 1, max = 10, message = "Price must be between 1 and 10 characters")
    private double price;
    @NotBlank(message = "Quantity is required")
    @Pattern(regexp = "^[0-9]*$", message = "Invalid quantity")
    @Size(min = 1, max = 10, message = "Quantity must be between 1 and 10 characters")
    private int quantity;
    @NotBlank(message = "Image URL is required")
    @Size(min = 3, max = 100, message = "Image URL must be between 3 and 100 characters")
    @Pattern(regexp = "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)$", message = "Invalid image URL")
    private String imageURL;

    private Long sellerId;
    private boolean active;

    public SpiceDTO() {
    }

    public SpiceDTO(UUID id, String name, String description, String category, double price, int quantity, String imageURL, Long sellerId, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.sellerId = sellerId;
        this.active = active;
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

    public void setPrice(double price) {
        this.price = price;
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
