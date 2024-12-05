package com.backend.liaison_system.users.lecturer.entity;

import com.backend.liaison_system.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@ToString
@AllArgsConstructor
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String lecturerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String profilePictureUrl;

    private String phone;
    private String company;
    private String password;

    private String faculty;
    private String department;

    private boolean isZoneLead;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public Lecturer() {}

    public Lecturer(String lecturerId, LocalDateTime createdAt, LocalDateTime updatedAt, String firstName, String lastName, String otherName, String email, String profilePictureUrl, String phone, String company, String password, String faculty, String department, boolean isZoneLead, UserRoles role) {
        this.lecturerId = lecturerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.phone = phone;
        this.company = company;
        this.password = password;
        this.faculty = faculty;
        this.department = department;
        this.isZoneLead = isZoneLead;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isZoneLead() {
        return isZoneLead;
    }

    public void setZoneLead(boolean zoneLead) {
        isZoneLead = zoneLead;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecturer lecturer = (Lecturer) o;
        return isZoneLead == lecturer.isZoneLead && Objects.equals(id, lecturer.id) && Objects.equals(lecturerId, lecturer.lecturerId) && Objects.equals(createdAt, lecturer.createdAt) && Objects.equals(updatedAt, lecturer.updatedAt) && Objects.equals(firstName, lecturer.firstName) && Objects.equals(lastName, lecturer.lastName) && Objects.equals(otherName, lecturer.otherName) && Objects.equals(email, lecturer.email) && Objects.equals(profilePictureUrl, lecturer.profilePictureUrl) && Objects.equals(phone, lecturer.phone) && Objects.equals(company, lecturer.company) && Objects.equals(password, lecturer.password) && Objects.equals(faculty, lecturer.faculty) && Objects.equals(department, lecturer.department) && role == lecturer.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lecturerId, createdAt, updatedAt, firstName, lastName, otherName, email, profilePictureUrl, phone, company, password, faculty, department, isZoneLead, role);
    }
}
