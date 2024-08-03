package com.example.AmbulanceServiceProvider.models;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "app_user")
@Schema(description = "Auto Generated User Id", example = "1")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "First name of the user", example = "John")
    private Long id;
    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "pass_hash")
    private String passHash;
    @NotNull
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)

    private userTypes role;

    // Constructors
    public user() {}

    public user(String firstName, String lastName, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public void setRole(userTypes role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", password='" + passHash + '\'' +
                '}';
    }
}