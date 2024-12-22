package com.backend.liaison_system.users.student.assumption_of_duty.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AssumptionOfDuty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    private  String dateCommenced;

    private LocalDateTime startOfAcademicYear;
    private LocalDateTime endOfAcademicYear;

    private int semester;

    private boolean isInternship;
    private boolean isUpdated;

    @Embedded
    private CompanyDetails companyDetails;

    public AssumptionOfDuty() { }

    public AssumptionOfDuty(String studentId, LocalDateTime dateCreated, LocalDateTime dateUpdated, String dateCommenced, LocalDateTime startOfAcademicYear, LocalDateTime endOfAcademicYear, int semester, boolean isInternship, boolean isUpdated, CompanyDetails companyDetails) {
        this.studentId = studentId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateCommenced = dateCommenced;
        this.startOfAcademicYear = startOfAcademicYear;
        this.endOfAcademicYear = endOfAcademicYear;
        this.semester = semester;
        this.isInternship = isInternship;
        this.isUpdated = isUpdated;
        this.companyDetails = companyDetails;
    }

    public String getId() {
        return id;
    }

    public int getSemester() {
        return semester;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDateCommenced() {
        return dateCommenced;
    }

    public void setDateCommenced(String dateCommenced) {
        this.dateCommenced = dateCommenced;
    }

    public LocalDateTime getStartOfAcademicYear() {
        return startOfAcademicYear;
    }

    public void setStartOfAcademicYear(LocalDateTime startOfAcademicYear) {
        this.startOfAcademicYear = startOfAcademicYear;
    }

    public LocalDateTime getEndOfAcademicYear() {
        return endOfAcademicYear;
    }

    public void setEndOfAcademicYear(LocalDateTime endOfAcademicYear) {
        this.endOfAcademicYear = endOfAcademicYear;
    }

    public boolean isInternship() {
        return isInternship;
    }

    public void setInternship(boolean internship) {
        isInternship = internship;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssumptionOfDuty that = (AssumptionOfDuty) o;
        return semester == that.semester && isInternship == that.isInternship && isUpdated == that.isUpdated && Objects.equals(id, that.id) && Objects.equals(studentId, that.studentId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated) && Objects.equals(dateCommenced, that.dateCommenced) && Objects.equals(startOfAcademicYear, that.startOfAcademicYear) && Objects.equals(endOfAcademicYear, that.endOfAcademicYear) && Objects.equals(companyDetails, that.companyDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, dateCreated, dateUpdated, dateCommenced, startOfAcademicYear, endOfAcademicYear, semester, isInternship, isUpdated, companyDetails);
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "AssumptionOfDuty{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", dateCommenced='" + dateCommenced + '\'' +
                ", startOfAcademicYear=" + startOfAcademicYear +
                ", endOfAcademicYear=" + endOfAcademicYear +
                ", semester=" + semester +
                ", isInternship=" + isInternship +
                ", isUpdated=" + isUpdated +
                ", companyDetails=" + companyDetails +
                '}';
    }
}
