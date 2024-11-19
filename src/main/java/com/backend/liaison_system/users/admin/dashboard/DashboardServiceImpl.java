package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService{

    private final AdminUtil adminUtil;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;

    public DashboardServiceImpl(AdminUtil adminUtil, StudentRepository studentRepository, LecturerRepository lecturerRepository) {
        this.adminUtil = adminUtil;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public ResponseEntity<Response<Statistics>> getStatistics(String id, ConstantRequestParam param) {

        // verify user role - ADMIN
        adminUtil.verifyUserIsAdmin(id);

        List<Student> students = studentRepository.findAllStudents(param);
        List<Lecturer> lecturers = lecturerRepository.findAllLectures(param);

        Response<Statistics> response = new Response.Builder<Statistics>()
                .status(HttpStatus.OK.value())
                .message("statistics")
                .data(new Statistics(
                        lecturers.size(),
                        students.size(),
                        0
                )).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
