package com.backend.liaison_system.assumption_of_duty.entities;

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

    private String startOfAcademicYear;
    private String endOfAcademicYear;

    private boolean isInternship;

    @Embedded
    private CompanyDetails companyDetails;

    public AssumptionOfDuty() { }

    public String getId() {
        return id;
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

    public String getStartOfAcademicYear() {
        return startOfAcademicYear;
    }

    public void setStartOfAcademicYear(String startOfAcademicYear) {
        this.startOfAcademicYear = startOfAcademicYear;
    }

    public String getEndOfAcademicYear() {
        return endOfAcademicYear;
    }

    public void setEndOfAcademicYear(String endOfAcademicYear) {
        this.endOfAcademicYear = endOfAcademicYear;
    }

    public boolean isInternship() {
        return isInternship;
    }

    public void setInternship(boolean internship) {
        isInternship = internship;
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
        return isInternship == that.isInternship && Objects.equals(id, that.id) && Objects.equals(studentId, that.studentId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated) && Objects.equals(startOfAcademicYear, that.startOfAcademicYear) && Objects.equals(endOfAcademicYear, that.endOfAcademicYear) && Objects.equals(companyDetails, that.companyDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, dateCreated, dateUpdated, startOfAcademicYear, endOfAcademicYear, isInternship, companyDetails);
    }

    @Override
    public String toString() {
        return "AssumptionOfDuty{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", startOfAcademicYear='" + startOfAcademicYear + '\'' +
                ", endOfAcademicYear='" + endOfAcademicYear + '\'' +
                ", isInternship=" + isInternship +
                ", companyDetails=" + companyDetails +
                '}';
    }
}
