package com.backend.liaison_system.users.student;

import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.enums.Status;
import com.backend.liaison_system.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@ToString
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String password;

    private String studentFirstName;
    private String studentLastName;
    private String studentOtherName;

    private String studentAge;
    private String studentEmail;
    private String studentGender;
    private String studentPhone;
    private String studentAbout;

    private String profilePictureUrl;

    private String studentDepartment;
    private String studentFaculty;

    private String studentCourse;
    private String placeOfInternship;

    /**
     * The start and end dates are the start
     * and end of the academic years in which the student
     * was added
    */
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Status status;

    @Enumerated(EnumType.STRING)
    private InternshipType internshipType;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    private boolean isSupervised;
    private boolean isAssumeDuty;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Student() { }

    public Student(String email, String password, String studentFirstName, String studentLastName, String studentOtherName, String studentAge, String studentEmail, String studentGender, String studentPhone, String studentAbout, String profilePictureUrl, String studentDepartment, String studentFaculty, String studentCourse, String placeOfInternship, LocalDateTime startDate, LocalDateTime endDate, Status status, InternshipType internshipType, UserRoles role, boolean isSupervised, boolean isAssumeDuty, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.email = email;
        this.password = password;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentOtherName = studentOtherName;
        this.studentAge = studentAge;
        this.studentEmail = studentEmail;
        this.studentGender = studentGender;
        this.studentPhone = studentPhone;
        this.studentAbout = studentAbout;
        this.profilePictureUrl = profilePictureUrl;
        this.studentDepartment = studentDepartment;
        this.studentFaculty = studentFaculty;
        this.studentCourse = studentCourse;
        this.placeOfInternship = placeOfInternship;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.internshipType = internshipType;
        this.role = role;
        this.isSupervised = isSupervised;
        this.isAssumeDuty = isAssumeDuty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getStudentOtherName() {
        return studentOtherName;
    }

    public void setStudentOtherName(String studentOtherName) {
        this.studentOtherName = studentOtherName;
    }

    public String getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(String studentAge) {
        this.studentAge = studentAge;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStudentAbout() {
        return studentAbout;
    }

    public void setStudentAbout(String studentAbout) {
        this.studentAbout = studentAbout;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getStudentDepartment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }

    public String getStudentFaculty() {
        return studentFaculty;
    }

    public void setStudentFaculty(String studentFaculty) {
        this.studentFaculty = studentFaculty;
    }

    public String getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(String studentCourse) {
        this.studentCourse = studentCourse;
    }

    public String getPlaceOfInternship() {
        return placeOfInternship;
    }

    public void setPlaceOfInternship(String placeOfInternship) {
        this.placeOfInternship = placeOfInternship;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public InternshipType getInternshipType() {
        return internshipType;
    }

    public void setInternshipType(InternshipType internshipType) {
        this.internshipType = internshipType;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public boolean isSupervised() {
        return isSupervised;
    }

    public void setSupervised(boolean supervised) {
        isSupervised = supervised;
    }

    public boolean isAssumeDuty() {
        return isAssumeDuty;
    }

    public void setAssumeDuty(boolean assumeDuty) {
        isAssumeDuty = assumeDuty;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return isSupervised == student.isSupervised && isAssumeDuty == student.isAssumeDuty && Objects.equals(id, student.id) && Objects.equals(email, student.email) && Objects.equals(password, student.password) && Objects.equals(studentFirstName, student.studentFirstName) && Objects.equals(studentLastName, student.studentLastName) && Objects.equals(studentOtherName, student.studentOtherName) && Objects.equals(studentAge, student.studentAge) && Objects.equals(studentEmail, student.studentEmail) && Objects.equals(studentGender, student.studentGender) && Objects.equals(studentPhone, student.studentPhone) && Objects.equals(studentAbout, student.studentAbout) && Objects.equals(profilePictureUrl, student.profilePictureUrl) && Objects.equals(studentDepartment, student.studentDepartment) && Objects.equals(studentFaculty, student.studentFaculty) && Objects.equals(studentCourse, student.studentCourse) && Objects.equals(placeOfInternship, student.placeOfInternship) && Objects.equals(startDate, student.startDate) && Objects.equals(endDate, student.endDate) && status == student.status && internshipType == student.internshipType && role == student.role && Objects.equals(createdAt, student.createdAt) && Objects.equals(updatedAt, student.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, studentFirstName, studentLastName, studentOtherName, studentAge, studentEmail, studentGender, studentPhone, studentAbout, profilePictureUrl, studentDepartment, studentFaculty, studentCourse, placeOfInternship, startDate, endDate, status, internshipType, role, isSupervised, isAssumeDuty, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", studentFirstName='" + studentFirstName + '\'' +
                ", studentLastName='" + studentLastName + '\'' +
                ", studentOtherName='" + studentOtherName + '\'' +
                ", studentAge='" + studentAge + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", studentGender='" + studentGender + '\'' +
                ", studentPhone='" + studentPhone + '\'' +
                ", studentAbout='" + studentAbout + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", studentDepartment='" + studentDepartment + '\'' +
                ", studentFaculty='" + studentFaculty + '\'' +
                ", studentCourse='" + studentCourse + '\'' +
                ", placeOfInternship='" + placeOfInternship + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", internshipType=" + internshipType +
                ", role=" + role +
                ", isSupervised=" + isSupervised +
                ", isAssumeDuty=" + isAssumeDuty +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
