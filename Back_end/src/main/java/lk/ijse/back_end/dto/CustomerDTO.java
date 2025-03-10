package lk.ijse.back_end.dto;

import java.util.List;
import java.util.UUID;

public class CustomerDTO {
    private UUID uid;
    private String email;
    private String password;
    private String name;
    private String role;
    private String profilePicture;
    private String phoneNumber;
    private List<OrderDTO> orders;
    private List<AddressDTO> addresses;

    public CustomerDTO() {
    }

    public CustomerDTO(UUID uid, String email, String password, String name, String role, String profilePicture, String phoneNumber, List<OrderDTO> orders, List<AddressDTO> addresses) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.orders = orders;
        this.addresses = addresses;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}
