package com.backend.liaison_system.users.admin.dao;

import com.backend.liaison_system.users.admin.dto.StudentDto;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import org.springframework.data.domain.Page;

import java.util.List;


public class TabularDataResponse {
    // I changed the class name from AdminStudentResponse to TabularDataResponse
    // so that we can use it different instances

    private int pageSize;
    private int currentPage;
    private int totalPages;
    private int totalData;

    Page<Lecturer> page;
    List<Lecturers> lecturers;
    List<StudentDto> students;

    public TabularDataResponse(int pageSize, int currentPage, int totalPages, int totalData, Page<Lecturer> page, List<Lecturers> lecturers, List<StudentDto> students) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalData = totalData;
        this.page = page;
        this.lecturers = lecturers;
        this.students = students;
    }

    public TabularDataResponse() {}

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public Page<Lecturer> getPage() {
        return page;
    }

    public void setPage(Page<Lecturer> page) {
        this.page = page;
    }

    public List<Lecturers> getLecturers() {
        return lecturers;
    }

    public void setLecturers(List<Lecturers> lecturers) {
        this.lecturers = lecturers;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "TabularDataResponse{" +
                "pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalData=" + totalData +
                ", page=" + page +
                ", lecturers=" + lecturers +
                ", students=" + students +
                '}';
    }

    private TabularDataResponse(Builder builder) {
        this.pageSize = builder.pageSize;
        this.currentPage = builder.currentPage;
        this.totalPages = builder.totalPages;
        this.totalData = builder.totalData;

        this.page = builder.page;
        this.lecturers = builder.lecturers;
        this.students = builder.students;
    }

    public static class Builder {
        private int pageSize;
        private int currentPage;
        private int totalPages;
        private int totalData;

        Page<Lecturer> page;
        List<Lecturers> lecturers;
        List<StudentDto> students;

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder totalData(int totalData) {
            this.totalData = totalData;
            return this;
        }

        public Builder page(Page<Lecturer> page) {
            this.page = page;
            return this;
        }

        public Builder lecturers(List<Lecturers> lecturers) {
            this.lecturers = lecturers;
            return this;
        }

        public Builder students(List<StudentDto> students) {
            this.students = students;
            return this;
        }

        public TabularDataResponse build() {
            return new TabularDataResponse(this);
        }
    }
}
