package com.backend.liaison_system.users.student;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.AssumptionOfDutyRepository;
import com.backend.liaison_system.users.student.response.AssignedLecturer;
import com.backend.liaison_system.users.student.response.Colleagues;
import com.backend.liaison_system.users.student.response.StudentDashboardRes;
import com.backend.liaison_system.users.student.util.StudentUtil;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentUtil studentUtil;
    private final StudentRepository studentRepository;
    private final ZoneRepository zoneRepository;
    private final LecturerRepository lecturerRepository;
    private final AssumptionOfDutyRepository assumptionOfDutyRepository;

    public StudentServiceImpl(StudentUtil studentUtil, StudentRepository studentRepository, ZoneRepository zoneRepository, LecturerRepository lecturerRepository, AssumptionOfDutyRepository assumptionOfDutyRepository) {
        this.studentUtil = studentUtil;
        this.studentRepository = studentRepository;
        this.zoneRepository = zoneRepository;
        this.lecturerRepository = lecturerRepository;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
    }

    @Override
    public ResponseEntity<Response<?>> getDashboardData(String studentId, ConstantRequestParam param) {
        // Verify that the use is a student
        studentUtil.verifyUserIsStudent(studentId);

        AtomicReference<StudentDashboardRes> dashboardRes = new AtomicReference<>();

        studentRepository.findById(studentId).ifPresent(
                data -> {
                    List<Colleagues> colleagues = new ArrayList<>();
                    assumptionOfDutyRepository.findAssumptionOfDutyByStudentId(data.getId(), param).ifPresent(assumptionOfDuty -> assumptionOfDutyRepository.findAll().forEach(
                            duty -> {
                                if (assumptionOfDuty.getCompanyDetails().getCompanyName().equals(duty.getCompanyDetails().getCompanyName())) {
                                    studentRepository.findById(duty.getStudentId()).ifPresent(
                                            student -> colleagues.add(new Colleagues(
                                                    student.getId(),
                                                    student.getStudentOtherName() == null? student.getStudentFirstName() + " " + student.getStudentLastName() : student.getStudentFirstName() + " " + student.getStudentOtherName() + " " + student.getStudentLastName(),
                                                    student.getStudentEmail(),
                                                    student.getProfilePictureUrl(),
                                                    student.getStudentDepartment()
                                            ))
                                    );
                                }
                            }
                    ));
                    colleagues.removeIf(s -> s.id().equals(data.getId())); // remove the student from the colleagues list

                    // get a list of the student's previous assumption of duties
                    List<AssumptionOfDuty> duties = assumptionOfDutyRepository.findAll().stream().filter(assumptionOfDuty -> assumptionOfDuty.getStudentId().equals(data.getId())).toList();

                    List<AssignedLecturer> assignedLecturers = getAssignedLecturers(data.getId(), param);
                    dashboardRes.set(new StudentDashboardRes(
                            data.getId(),
                            data.getStudentFirstName(),
                            data.getStudentEmail(),
                            data.getProfilePictureUrl(),
                            data.isSupervised(),
                            data.isAssumeDuty(),
                            colleagues,
                            colleagues.size(),
                            assignedLecturers,
                            assignedLecturers.size(),
                            duties,
                            duties.size()
                    ));
                }
        );

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("dashboard data")
                .data(dashboardRes.get())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private List<AssignedLecturer> getAssignedLecturers(String studentId, ConstantRequestParam param) {

        // find the zone using the Region r and Town t
        List<String> lecturers = new ArrayList<>();
        assumptionOfDutyRepository.findAssumptionOfDutyByStudentId(studentId, param)
                .flatMap(assumptionOfDuty -> zoneRepository.findZoneByRegionAndTown(assumptionOfDuty.getCompanyDetails().getCompanyRegion(), assumptionOfDuty.getCompanyDetails().getCompanyTown(), param))
                .ifPresent(zone -> lecturers.addAll(zone.getLecturers().lecturers()));


        List<AssignedLecturer> assignedLecturers = new ArrayList<>();
        for (String id : lecturers) {
            lecturerRepository.findById(id).map(
                    lecturer -> assignedLecturers.add(new AssignedLecturer(
                            lecturer.getOtherName() == null? lecturer.getFirstName() + " " + lecturer.getLastName() : lecturer.getFirstName() + " " + lecturer.getOtherName() + " " + lecturer.getLastName(),
                            lecturer.getEmail(),
                            lecturer.getPhone(),
                            lecturer.isZoneLead(),
                            lecturer.getProfilePictureUrl()
                    ))
            );
        }

        return assignedLecturers;
    }
}
