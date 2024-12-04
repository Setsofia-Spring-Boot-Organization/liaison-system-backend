package com.backend.liaison_system.users.student.assumption_of_duty.entities;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CompanyDetails {
    private String companyName;
    private String companyPhone;

    private String companyExactLocation;
    private String companyTown;
    private String companyRegion;

    private String companyAddress;
    private String companyEmail;

    private String companySupervisor;
    private String supervisorPhone;

    private String letterTo;

    private String companyLongitude;
    private String companyLatitude;

    public CompanyDetails() { }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyExactLocation() {
        return companyExactLocation;
    }

    public void setCompanyExactLocation(String companyExactLocation) {
        this.companyExactLocation = companyExactLocation;
    }

    public String getCompanyTown() {
        return companyTown;
    }

    public void setCompanyTown(String companyTown) {
        this.companyTown = companyTown;
    }

    public String getCompanyRegion() {
        return companyRegion;
    }

    public void setCompanyRegion(String companyRegion) {
        this.companyRegion = companyRegion;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanySupervisor() {
        return companySupervisor;
    }

    public void setCompanySupervisor(String companySupervisor) {
        this.companySupervisor = companySupervisor;
    }

    public String getSupervisorPhone() {
        return supervisorPhone;
    }

    public void setSupervisorPhone(String supervisorPhone) {
        this.supervisorPhone = supervisorPhone;
    }

    public String getLetterTo() {
        return letterTo;
    }

    public void setLetterTo(String letterTo) {
        this.letterTo = letterTo;
    }

    public String getCompanyLongitude() {
        return companyLongitude;
    }

    public void setCompanyLongitude(String companyLongitude) {
        this.companyLongitude = companyLongitude;
    }

    public String getCompanyLatitude() {
        return companyLatitude;
    }

    public void setCompanyLatitude(String companyLatitude) {
        this.companyLatitude = companyLatitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDetails that = (CompanyDetails) o;
        return Objects.equals(companyName, that.companyName) && Objects.equals(companyPhone, that.companyPhone) && Objects.equals(companyExactLocation, that.companyExactLocation) && Objects.equals(companyTown, that.companyTown) && Objects.equals(companyRegion, that.companyRegion) && Objects.equals(companyAddress, that.companyAddress) && Objects.equals(companyEmail, that.companyEmail) && Objects.equals(companySupervisor, that.companySupervisor) && Objects.equals(supervisorPhone, that.supervisorPhone) && Objects.equals(letterTo, that.letterTo) && Objects.equals(companyLongitude, that.companyLongitude) && Objects.equals(companyLatitude, that.companyLatitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, companyPhone, companyExactLocation, companyTown, companyRegion, companyAddress, companyEmail, companySupervisor, supervisorPhone, letterTo, companyLongitude, companyLatitude);
    }

    @Override
    public String toString() {
        return "CompanyDetails{" +
                "companyName='" + companyName + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyExactLocation='" + companyExactLocation + '\'' +
                ", companyTown='" + companyTown + '\'' +
                ", companyRegion='" + companyRegion + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", companySupervisor='" + companySupervisor + '\'' +
                ", supervisorPhone='" + supervisorPhone + '\'' +
                ", letterTo='" + letterTo + '\'' +
                ", companyLongitude='" + companyLongitude + '\'' +
                ", companyLatitude='" + companyLatitude + '\'' +
                '}';
    }
}
