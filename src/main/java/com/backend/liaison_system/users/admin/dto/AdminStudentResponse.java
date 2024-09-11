package com.backend.liaison_system.users.admin.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminStudentResponse {
    private int pageSize;
    private int currentPage;
    private int totalPages;
    private int totalData;
    List<StudentDto> students;
}
