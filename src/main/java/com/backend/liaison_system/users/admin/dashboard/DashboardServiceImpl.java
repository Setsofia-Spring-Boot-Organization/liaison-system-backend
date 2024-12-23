package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dao.Lecturers;
import com.backend.liaison_system.users.admin.dashboard.dao.*;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.AssumptionOfDutyRepository;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import com.backend.liaison_system.zone.specification.ZoneSpecification;
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
    private final ZoneSpecification zoneSpecification;

    public DashboardServiceImpl(AdminUtil adminUtil, StudentRepository studentRepository, LecturerRepository lecturerRepository, AssumptionOfDutyRepository assumptionOfDutyRepository, ZoneRepository zoneRepository, ZoneSpecification zoneSpecification) {
        this.adminUtil = adminUtil;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
        this.zoneRepository = zoneRepository;
        this.zoneSpecification = zoneSpecification;
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
    public ResponseEntity<Response<?>> getAssignedAndUnassignedStudentsAndLecturers(String adminId, ConstantRequestParam param) {
        //verify that the user is an admin
        adminUtil.verifyUserIsAdmin(adminId);

        AssignedAndUnassignedStudents assignedAndUnassignedStudents = getAssignedAndUnassignedStudents(param);
        AssignedAndUnassignedLecturers assignedAndUnassignedLecturers = getAssignedAndUnassignedLecturers(param);

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("assigned and unassigned students and lecturers")
                .data(new AssignedAndUnassignedLecturersAndStudentsData(
                        assignedAndUnassignedStudents,
                        assignedAndUnassignedLecturers
                ))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private AssignedAndUnassignedStudents getAssignedAndUnassignedStudents(ConstantRequestParam param) {

        List<Student> assignedStudents = new ArrayList<>();
        List<Student> unAssignedStudents = new ArrayList<>();
        studentRepository.findAllStudents(param).forEach(student -> assumptionOfDutyRepository.findAssumptionOfDutyByStudentId(student.getId(), param)
                .flatMap(assumptionOfDuty -> zoneRepository.findZoneByRegionAndTown(
                assumptionOfDuty.getCompanyDetails().getCompanyRegion(),
                assumptionOfDuty.getCompanyDetails().getCompanyTown(),
                param
        )).ifPresentOrElse(zone -> assignedStudents.add(student), () -> unAssignedStudents.add(student)));

        return  new AssignedAndUnassignedStudents(
                new AssignedStudents(assignedStudents.stream().map(adminUtil::buildStudentDtoFromStudent).toList(), assignedStudents.size()),
                new UnassignedStudents(unAssignedStudents.stream().map(adminUtil::buildStudentDtoFromStudent).toList(), unAssignedStudents.size()),
                assignedStudents.size() + unAssignedStudents.size()
        );
    }


    private AssignedAndUnassignedLecturers getAssignedAndUnassignedLecturers(ConstantRequestParam param) {

        List<Lecturers> assignedLecturers = new ArrayList<>();
        List<Lecturers> unAssignedLecturers = new ArrayList<>();
        lecturerRepository.findAllLectures(param).forEach(lecturer -> zoneSpecification.findZonesUsingZoneTypeAndAcademicDates(param).forEach(
                zone -> {
                    if (zone.getLecturers().lecturers().contains(lecturer.getId())) {
                        assignedLecturers.add(createLecturerDTO(lecturer));
                    } else {
                        unAssignedLecturers.add(createLecturerDTO(lecturer));
                    }
                }
        ));

        return new AssignedAndUnassignedLecturers(
                new AssignedLecturers(assignedLecturers, assignedLecturers.size()),
                new UnassignedLecturers(unAssignedLecturers, unAssignedLecturers.size()),
                assignedLecturers.size() + unAssignedLecturers.size()
        );
    }

    private Lecturers createLecturerDTO(Lecturer lecturer) {
        return new Lecturers(
                lecturer.getId(),
                lecturer.getProfilePictureUrl(),
                lecturer.getOtherName() == null? lecturer.getFirstName() + " " + lecturer.getLastName() : lecturer.getFirstName() + " " + lecturer.getOtherName() + " " + lecturer.getLastName(),
                lecturer.getFaculty(),
                lecturer.getDepartment()
        );
    }
}
