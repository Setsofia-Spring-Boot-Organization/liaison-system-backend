package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Response<Statistics>> getStatistics(String id, ConstantRequestParam constantRequestParam) {

        // verify user role - ADMIN
        adminUtil.verifyUserIsAdmin(id);

        List<Student> students = getTotalStudents(constantRequestParam);
        List<Lecturer> lecturers = getTotalLecturers(constantRequestParam);

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

    /**
     * This method retrieves a list of students based on the internship type and year range.
     * It throws a {@link LiaisonException} if the internship type is invalid.
     *
     * @param constantRequestParam an object containing request parameters such as internship type, start year, and end year
     * @return a list of students that match the internship type and year range
     * @throws LiaisonException if the internship type is invalid
     */
    private List<Student> getTotalStudents(ConstantRequestParam constantRequestParam) throws LiaisonException {

        if (constantRequestParam.internshipType().equals(InternshipType.INTERNSHIP.name())) {
            return studentRepository.findAllStudents(
                    constantRequestParam.startYear(),
                    constantRequestParam.endYear(),
                    constantRequestParam.internshipType().toUpperCase()
            );
        } else if (constantRequestParam.internshipType().equals(InternshipType.SEMESTER_OUT.name())) {
            return studentRepository.findAllStudents(
                    constantRequestParam.startYear(),
                    constantRequestParam.endYear(),
                    constantRequestParam.internshipType().toUpperCase()
            );
        } else
            throw new LiaisonException(Error.INVALID_INTERNSHIP_TYPE, new Throwable(Message.THE_INTERNSHIP_TYPE_IS_INCORRECT.label));
    }

    /**
     * This method retrieves a list of lecturers based on the internship type and year range.
     * It throws a {@link LiaisonException} if the internship type is invalid.
     *
     * @param constantRequestParam an object containing request parameters such as internship type, start year, and end year
     * @return a list of lecturers that match the internship type and year range
     * @throws LiaisonException if the internship type is invalid
     */
    private List<Lecturer> getTotalLecturers(ConstantRequestParam constantRequestParam) throws LiaisonException {

        if (constantRequestParam.internshipType().equals(InternshipType.INTERNSHIP.name())) {
            return lecturerRepository.findAllLectures(
                    constantRequestParam.startYear(),
                    constantRequestParam.endYear()
            );
        } else if (constantRequestParam.internshipType().equals(InternshipType.SEMESTER_OUT.name())) {
            return lecturerRepository.findAllLectures(
                    constantRequestParam.startYear(),
                    constantRequestParam.endYear()
            );
        } else
            throw new LiaisonException(Error.INVALID_INTERNSHIP_TYPE, new Throwable(Message.THE_INTERNSHIP_TYPE_IS_INCORRECT.label));
    }
}
