package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dashboard.dao.AssignedAndUnassignedStudents;
import com.backend.liaison_system.users.admin.dashboard.dao.AssignedStudents;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import com.backend.liaison_system.users.admin.dashboard.dao.UnassignedStudents;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.AssumptionOfDutyRepository;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService{

    private final AdminUtil adminUtil;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final AssumptionOfDutyRepository assumptionOfDutyRepository;
    private final ZoneRepository zoneRepository;

    public DashboardServiceImpl(AdminUtil adminUtil, StudentRepository studentRepository, LecturerRepository lecturerRepository, AssumptionOfDutyRepository assumptionOfDutyRepository, ZoneRepository zoneRepository) {
        this.adminUtil = adminUtil;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
        this.zoneRepository = zoneRepository;
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


    @Override
    public ResponseEntity<Response<?>> getAssignedAndUnassignedStudents(String adminId, ConstantRequestParam param) {
        //verify that the user is an admin
        adminUtil.verifyUserIsAdmin(adminId);

        List<Student> assignedStudents = new ArrayList<>();
        List<Student> unAssignedStudents = new ArrayList<>();
        studentRepository.findAllStudents(param).forEach(student -> assumptionOfDutyRepository.findAssumptionOfDutyByStudentId(student.getId())
                .flatMap(assumptionOfDuty -> zoneRepository.findZoneByRegionAndTown(
                assumptionOfDuty.getCompanyDetails().getCompanyRegion(),
                assumptionOfDuty.getCompanyDetails().getCompanyTown()
        )).ifPresentOrElse(zone -> assignedStudents.add(student), () -> unAssignedStudents.add(student)));

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("assigned and unassigned students")
                .data(new AssignedAndUnassignedStudents(
                        new AssignedStudents(assignedStudents.stream().map(adminUtil::buildStudentDtoFromStudent).toList(), assignedStudents.size()),
                        new UnassignedStudents(unAssignedStudents.stream().map(adminUtil::buildStudentDtoFromStudent).toList(), unAssignedStudents.size()),
                        assignedStudents.size() + unAssignedStudents.size()
                )).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
