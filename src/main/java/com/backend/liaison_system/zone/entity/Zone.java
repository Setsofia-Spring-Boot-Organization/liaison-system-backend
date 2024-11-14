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

    private InternshipType internshipType;

    @Embedded
    private ZoneLecturers lecturers;

    @Embedded
    private Towns towns;

    public Zone(String id, String name, String region, String zoneLead, LocalDateTime startOfAcademicYear, LocalDateTime endOfAcademicYear, LocalDateTime dateCreated, LocalDateTime dateUpdated, InternshipType internshipType, ZoneLecturers lecturers, Towns towns) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.zoneLead = zoneLead;
        this.startOfAcademicYear = startOfAcademicYear;
        this.endOfAcademicYear = endOfAcademicYear;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.internshipType = internshipType;
        this.lecturers = lecturers;
        this.towns = towns;
    }

    public Zone() {}

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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Zone zone = (Zone) o;
        return getId() != null && Objects.equals(getId(), zone.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
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
                ", internshipType=" + internshipType +
                ", lecturers=" + lecturers +
                ", towns=" + towns +
                '}';
    }
}
