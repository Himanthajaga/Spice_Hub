package lk.ijse.back_end.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {
    @Email(message = "Invalid email")
    private String email;
    @NotBlank
    @Pattern(regexp = "^[0-9]*$", message = "Invalid phone number")
    private String phone;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Invalid password")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Invalid name")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Invalid role")
    private String role;
    private String profilePicture;

    public UserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public UserDTO(String email, String phone, String password, String name, String role, String profilePicture) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.role = role;
        this.profilePicture = profilePicture;
    }
}
