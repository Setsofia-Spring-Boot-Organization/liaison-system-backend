package com.backend.liaison_system.users.student;

import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "liaison/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(path = "/dashboard/{student-id}")
    public ResponseEntity<Response<?>> studentDashboardData(
            @PathVariable("student-id") String studentId
    ) {
            return studentService.getDashboardData(studentId);
    }
}
