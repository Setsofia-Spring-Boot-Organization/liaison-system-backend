package com.backend.liaison_system.users.lecturer.responses;

import java.util.Objects;

public class TopCompaniesData{
        private String name;
        private String town;
        private String exactLocation;
        private int totalStudents;

    public TopCompaniesData() {
    }

    public TopCompaniesData(String name, String town, String exactLocation, int totalStudents) {
        this.name = name;
        this.town = town;
        this.exactLocation = exactLocation;
        this.totalStudents = totalStudents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getExactLocation() {
        return exactLocation;
    }

    public void setExactLocation(String exactLocation) {
        this.exactLocation = exactLocation;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopCompaniesData that = (TopCompaniesData) o;
        return totalStudents == that.totalStudents && Objects.equals(name, that.name) && Objects.equals(town, that.town) && Objects.equals(exactLocation, that.exactLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, town, exactLocation, totalStudents);
    }

    @Override
    public String toString() {
        return "TopCompaniesData{" +
                "name='" + name + '\'' +
                ", town='" + town + '\'' +
                ", exactLocation='" + exactLocation + '\'' +
                ", totalStudents=" + totalStudents +
                '}';
    }
}
