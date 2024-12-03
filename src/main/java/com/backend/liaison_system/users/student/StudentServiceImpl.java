package com.backend.liaison_system.users.student;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.student.response.StudentDashboardRes;
import com.backend.liaison_system.users.student.util.StudentUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentUtil studentUtil;
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentUtil studentUtil, StudentRepository studentRepository) {
        this.studentUtil = studentUtil;
        this.studentRepository = studentRepository;
    }

    @Override
    public ResponseEntity<Response<?>> getDashboardData(String studentId) {

        // Verify that the use is a student
        studentUtil.verifyUserIsStudent(studentId);

        AtomicReference<StudentDashboardRes> dashboardRes = new AtomicReference<>();

        studentRepository.findById(studentId).ifPresent(
                data -> dashboardRes.set(new StudentDashboardRes(
                        data.getId(),
                        data.getStudentFirstName(),
                        data.getEmail(),
                        data.getProfilePictureUrl(),
                        data.isSupervised(),
                        data.isAssumeDuty()
                ))
        );

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("dashboard data")
                .data(dashboardRes.get())
                .build();

        System.out.println("response = " + response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
