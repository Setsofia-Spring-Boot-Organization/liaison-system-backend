package com.backend.liaison_system.zone.entity;

import com.backend.liaison_system.enums.InternshipType;
import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String region;
    private String zoneLead;

    /**
     * The start and end dates are the start
     * and end of the academic years in which the ZONE
     * was created
     */
    private LocalDateTime startOfAcademicYear;
    private LocalDateTime endOfAcademicYear;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    private int semester;

    private InternshipType internshipType;

    @Embedded
    private ZoneLecturers lecturers;

    @Embedded
    private Towns towns;


    public Zone() {}

    public Zone(String name, String region, String zoneLead, LocalDateTime startOfAcademicYear, LocalDateTime endOfAcademicYear, LocalDateTime dateCreated, LocalDateTime dateUpdated, int semester, InternshipType internshipType, ZoneLecturers lecturers, Towns towns) {
        this.name = name;
        this.region = region;
        this.zoneLead = zoneLead;
        this.startOfAcademicYear = startOfAcademicYear;
        this.endOfAcademicYear = endOfAcademicYear;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.semester = semester;
        this.internshipType = internshipType;
        this.lecturers = lecturers;
        this.towns = towns;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZoneLead() {
        return zoneLead;
    }

    public void setZoneLead(String zoneLead) {
        this.zoneLead = zoneLead;
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

    public InternshipType getInternshipType() {
        return internshipType;
    }

    public void setInternshipType(InternshipType internshipType) {
        this.internshipType = internshipType;
    }

    public ZoneLecturers getLecturers() {
        return lecturers;
    }

    public void setLecturers(ZoneLecturers lecturers) {
        this.lecturers = lecturers;
    }

    public Towns getTowns() {
        return towns;
    }

    public void setTowns(Towns towns) {
        this.towns = towns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return semester == zone.semester && Objects.equals(id, zone.id) && Objects.equals(name, zone.name) && Objects.equals(region, zone.region) && Objects.equals(zoneLead, zone.zoneLead) && Objects.equals(startOfAcademicYear, zone.startOfAcademicYear) && Objects.equals(endOfAcademicYear, zone.endOfAcademicYear) && Objects.equals(dateCreated, zone.dateCreated) && Objects.equals(dateUpdated, zone.dateUpdated) && internshipType == zone.internshipType && Objects.equals(lecturers, zone.lecturers) && Objects.equals(towns, zone.towns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, region, zoneLead, startOfAcademicYear, endOfAcademicYear, dateCreated, dateUpdated, semester, internshipType, lecturers, towns);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", zoneLead='" + zoneLead + '\'' +
                ", startOfAcademicYear=" + startOfAcademicYear +
                ", endOfAcademicYear=" + endOfAcademicYear +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", semester=" + semester +
                ", internshipType=" + internshipType +
                ", lecturers=" + lecturers +
                ", towns=" + towns +
                '}';
    }
}
