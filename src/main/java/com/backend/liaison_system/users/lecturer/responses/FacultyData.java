package com.backend.liaison_system.users.lecturer.responses;

public class FacultyData {
        String name;
        int totalStudents;

    public FacultyData(String name, int totalStudents) {
        this.name = name;
        this.totalStudents = totalStudents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }
}
