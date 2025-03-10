package lk.ijse.back_end.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@ToString
@Entity
@Table(name = "systemuser")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @Column(insertable = false, updatable = false)
    private String role;
    private String profilePicture;

    public User() {
    }

    public User(UUID uid, String email, String password, String name, String role, String profilePicture) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.profilePicture = profilePicture;
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
}