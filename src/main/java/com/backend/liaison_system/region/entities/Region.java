package com.backend.liaison_system.region.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String region;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    @Embedded
    private Town town;

    public Region(String id, String region, LocalDateTime dateCreated, LocalDateTime dateUpdated, Town town) {
        this.id = id;
        this.region = region;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.town = town;
    }

    public Region() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id='" + id + '\'' +
                ", region='" + region + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", town=" + town +
                '}';
    }
}
