package com.backend.liaison_system.users.admin.dto;

import com.backend.liaison_system.enums.Status;
import java.time.LocalDateTime;

public class StudentDto {
    private String id;
    private String name;
    private String department;
    private String faculty;
    private String age;
    private String email;
    private String gender;
    private String phone;
    private String about;
    private String course;
    private String placeOfInternship;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;

    public StudentDto(String id, String name, String department, String faculty, String age, String email, String gender, String phone, String about, String course, String placeOfInternship, LocalDateTime startDate, LocalDateTime endDate, Status status) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.faculty = faculty;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.about = about;
        this.course = course;
        this.placeOfInternship = placeOfInternship;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public StudentDto() {}

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    @Override
    public String toString() {
        return "StudentDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", faculty='" + faculty + '\'' +
                ", age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", about='" + about + '\'' +
                ", course='" + course + '\'' +
                ", placeOfInternship='" + placeOfInternship + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }

    private StudentDto(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.department = builder.department;
        this.faculty = builder.faculty;
        this.age = builder.age;
        this.email = builder.email;
        this.gender = builder.gender;
        this.phone = builder.phone;
        this.about = builder.about;
        this.course = builder.course;
        this.placeOfInternship = builder.placeOfInternship;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
    }

    public static class Builder {
        private String id;
        private String name;
        private String department;
        private String faculty;
        private String age;
        private String email;
        private String gender;
        private String phone;
        private String about;
        private String course;
        private String placeOfInternship;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Status status;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder faculty(String faculty) {
            this.faculty = faculty;
            return this;
        }

        public Builder age(String age) {
            this.age = age;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder about(String about) {
            this.about = about;
            return this;
        }

        public Builder course(String course) {
            this.course = course;
            return this;
        }

        public Builder placeOfInternship(String placeOfInternship) {
            this.placeOfInternship = placeOfInternship;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public StudentDto build() {
            return new StudentDto(this);
        }
    }
}
