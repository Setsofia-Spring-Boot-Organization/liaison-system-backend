package com.backend.liaison_system.users.admin.entity;

import com.backend.liaison_system.enums.UserRoles;
import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String email;
    private String firstName;
    private String lastName;
    private String otherName;

    private String password;
    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public Admin(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, String firstName, String lastName, String otherName, String password, UserRoles role) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.password = password;
        this.role = role;
    }

    public Admin() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Admin admin = (Admin) o;
        return getId() != null && Objects.equals(getId(), admin.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", otherName='" + otherName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
