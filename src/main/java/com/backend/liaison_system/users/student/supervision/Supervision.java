package com.backend.liaison_system.users.student.supervision;

import com.backend.liaison_system.enums.InternshipType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Supervision {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime startOfAcademicYear;
    private LocalDateTime endOfAcademicYear;
    private int semester;

    private InternshipType internshipType;

    private String studentId;
    private String supervisorId;

    public Supervision() { }

    public Supervision(LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime startOfAcademicYear, LocalDateTime endOfAcademicYear, int semester, InternshipType internshipType, String studentId, String supervisorId) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startOfAcademicYear = startOfAcademicYear;
        this.endOfAcademicYear = endOfAcademicYear;
        this.semester = semester;
        this.internshipType = internshipType;
        this.studentId = studentId;
        this.supervisorId = supervisorId;
    }

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

    public InternshipType getInternshipType() {
        return internshipType;
    }

    public void setInternshipType(InternshipType internshipType) {
        this.internshipType = internshipType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supervision that = (Supervision) o;
        return semester == that.semester && Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(startOfAcademicYear, that.startOfAcademicYear) && Objects.equals(endOfAcademicYear, that.endOfAcademicYear) && internshipType == that.internshipType && Objects.equals(studentId, that.studentId) && Objects.equals(supervisorId, that.supervisorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, startOfAcademicYear, endOfAcademicYear, semester, internshipType, studentId, supervisorId);
    }

    @Override
    public String toString() {
        return "Supervision{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", startOfAcademicYear=" + startOfAcademicYear +
                ", endOfAcademicYear=" + endOfAcademicYear +
                ", semester=" + semester +
                ", internshipType=" + internshipType +
                ", studentId='" + studentId + '\'' +
                ", supervisorId='" + supervisorId + '\'' +
                '}';
    }
}
