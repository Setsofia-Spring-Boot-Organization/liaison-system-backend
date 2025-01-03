package com.backend.liaison_system.users.student.assumption_of_duty.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class OldAssumptionOfDuty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;
    private String assumptionId;

    private LocalDateTime dateCreated;

    private  String dateCommenced;

    private LocalDateTime startOfAcademicYear;
    private LocalDateTime endOfAcademicYear;

    private int semester;

    private boolean isInternship;
    private boolean isUpdated;

    @Embedded
    private CompanyDetails companyDetails;

    public OldAssumptionOfDuty() { }

    public OldAssumptionOfDuty(String studentId, String assumptionId, LocalDateTime dateCreated, String dateCommenced, LocalDateTime startOfAcademicYear, LocalDateTime endOfAcademicYear, int semester, boolean isInternship, boolean isUpdated, CompanyDetails companyDetails) {
        this.studentId = studentId;
        this.assumptionId = assumptionId;
        this.dateCreated = dateCreated;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAssumptionId() {
        return assumptionId;
    }

    public void setAssumptionId(String assumptionId) {
        this.assumptionId = assumptionId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
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

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
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
        if (o == null || getClass() != o.getClass()) return false;
        OldAssumptionOfDuty that = (OldAssumptionOfDuty) o;
        return semester == that.semester && isInternship == that.isInternship && isUpdated == that.isUpdated && Objects.equals(id, that.id) && Objects.equals(studentId, that.studentId) && Objects.equals(assumptionId, that.assumptionId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateCommenced, that.dateCommenced) && Objects.equals(startOfAcademicYear, that.startOfAcademicYear) && Objects.equals(endOfAcademicYear, that.endOfAcademicYear) && Objects.equals(companyDetails, that.companyDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, assumptionId, dateCreated, dateCommenced, startOfAcademicYear, endOfAcademicYear, semester, isInternship, isUpdated, companyDetails);
    }

    @Override
    public String toString() {
        return "OldAssumptionOfDuty{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", assumptionId='" + assumptionId + '\'' +
                ", dateCreated=" + dateCreated +
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
