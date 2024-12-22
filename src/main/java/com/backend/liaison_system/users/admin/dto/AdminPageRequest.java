package com.backend.liaison_system.users.admin.dto;


public class AdminPageRequest {
    Integer page;
    Integer size;
    String find;
    String adminId;
    String name;
    String faculty;
    String department;
    int semester;
    Boolean internship;

    public AdminPageRequest() {}

    public AdminPageRequest(Integer page, Integer size, String find, String adminId, String name, String faculty, String department, int semester, Boolean internship) {
        this.page = page;
        this.size = size;
        this.find = find;
        this.adminId = adminId;
        this.name = name;
        this.faculty = faculty;
        this.department = department;
        this.semester = semester;
        this.internship = internship;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFind() {
        return find;
    }

    public void setFind(String find) {
        this.find = find;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getInternship() {
        return internship;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setInternship(Boolean internship) {
        this.internship = internship;
    }

    @Override
    public String toString() {
        return "AdminPageRequest{" +
                "page=" + page +
                ", size=" + size +
                ", find='" + find + '\'' +
                ", adminId='" + adminId + '\'' +
                ", name='" + name + '\'' +
                ", faculty='" + faculty + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", internship=" + internship +
                '}';
    }

    private AdminPageRequest(Builder builder) {
        this.page = builder.page;
        this.size = builder.size;
        this.find = builder.find;
        this.adminId = builder.adminId;
        this.name = builder.name;
        this.faculty = builder.faculty;
        this.department = builder.department;
        this.internship = builder.internship;
    }

    public static class Builder {
        Integer page;
        Integer size;
        String find;
        String adminId;
        String name;
        String faculty;
        String department;
        int semester;
        Boolean internship;

        public Builder semester(Integer semester) {
            this.semester = semester;
            return this;
        }

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder find(String find) {
            this.find = find;
            return this;
        }

        public Builder adminId(String adminId) {
            this.adminId = adminId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder faculty(String faculty) {
            this.faculty = faculty;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder internship(boolean internship) {
            this.internship = internship;
            return this;
        }

        public AdminPageRequest build() {
            return new AdminPageRequest(this);
        }
    }
}
