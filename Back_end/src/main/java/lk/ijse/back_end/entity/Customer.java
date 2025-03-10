package lk.ijse.back_end.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
public class Customer extends User implements Serializable {
    private String phoneNumber;
    @OneToMany(mappedBy = "user")
    private List<Order>orders;
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;
    public Customer() {
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Customer(UUID uid, String email, String password, String name, String role, String profilePicture, String phoneNumber) {
        super(uid, email, password, name, role, profilePicture);
        this.phoneNumber = phoneNumber;
    }

    public Customer(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
