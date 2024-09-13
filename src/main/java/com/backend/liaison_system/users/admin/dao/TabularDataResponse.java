package com.backend.liaison_system.users.admin.dao;

import com.backend.liaison_system.users.admin.dto.StudentDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabularDataResponse {
    // I changed the class name from AdminStudentResponse to TabularDataResponse
    // so that we can use it different instances

    private int pageSize;
    private int currentPage;
    private int totalPages;
    private int totalData;

    List<Lecturers> lecturers;
    List<StudentDto> students;
}
