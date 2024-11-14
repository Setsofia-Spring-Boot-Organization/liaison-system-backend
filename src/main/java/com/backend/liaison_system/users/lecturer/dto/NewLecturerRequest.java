package com.backend.liaison_system.users.lecturer.dto;

import com.backend.liaison_system.enums.UserRoles;

import java.util.Objects;

public class NewLecturerRequest {

    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String dp;

    private String phone;
    private String company;
    private String password;

    private String faculty;
    private String department;

    private UserRoles role;

    public NewLecturerRequest(String firstName, String lastName, String otherName, String email, String dp, String phone, String company, String password, String faculty, String department, UserRoles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.email = email;
        this.dp = dp;
        this.phone = phone;
        this.company = company;
        this.password = password;
        this.faculty = faculty;
        this.department = department;
        this.role = role;
    }

    public NewLecturerRequest() {}

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

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
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
        NewLecturerRequest that = (NewLecturerRequest) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(otherName, that.otherName) && Objects.equals(email, that.email) && Objects.equals(dp, that.dp) && Objects.equals(phone, that.phone) && Objects.equals(company, that.company) && Objects.equals(password, that.password) && Objects.equals(faculty, that.faculty) && Objects.equals(department, that.department) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, otherName, email, dp, phone, company, password, faculty, department, role);
    }

    @Override
    public String toString() {
        return "NewLecturerRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", otherName='" + otherName + '\'' +
                ", email='" + email + '\'' +
                ", dp='" + dp + '\'' +
                ", phone='" + phone + '\'' +
                ", company='" + company + '\'' +
                ", password='" + password + '\'' +
                ", faculty='" + faculty + '\'' +
                ", department='" + department + '\'' +
                ", role=" + role +
                '}';
    }
}
