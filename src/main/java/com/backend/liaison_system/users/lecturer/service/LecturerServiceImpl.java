package com.backend.liaison_system.users.lecturer.service;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.dao.StudentLocationData;
import com.backend.liaison_system.users.admin.dto.StudentDto;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.dao.LecturerList;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.lecturer.responses.*;
import com.backend.liaison_system.users.lecturer.util.LecturerUtil;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.AssumptionOfDutyRepository;
import com.backend.liaison_system.users.student.supervision.Supervision;
import com.backend.liaison_system.users.student.supervision.SupervisionRepository;
import com.backend.liaison_system.users.student.supervision.suoervision_report.SupervisionReportGenerator;
import com.backend.liaison_system.util.UAcademicYear;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.specification.ZoneSpecification;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AdminUtil adminUtil;
    private final LecturerUtil lecturerUtil;
    private final AssumptionOfDutyRepository assumptionOfDutyRepository;
    private final StudentRepository studentRepository;
    private final ZoneSpecification zoneSpecification;
    private final SupervisionRepository supervisionRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, AdminUtil adminUtil, LecturerUtil lecturerUtil, AssumptionOfDutyRepository assumptionOfDutyRepository, StudentRepository studentRepository, ZoneSpecification zoneSpecification, SupervisionRepository supervisionRepository) {
        this.lecturerRepository = lecturerRepository;
        this.adminUtil = adminUtil;
        this.lecturerUtil = lecturerUtil;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
        this.studentRepository = studentRepository;
        this.zoneSpecification = zoneSpecification;
        this.supervisionRepository = supervisionRepository;
    }

    @Override
    public ResponseEntity<Response<List<Lecturer>>> createNewLecturer(String adminID, List<NewLecturerRequest> requests, ConstantRequestParam param) {
        // verify that the user performing this action is an admin
        adminUtil.verifyUserIsAdmin(adminID);

        // verify if the emails already exist...
        verifyExistingEmails(requests);

        // save the lecturers
        List<Lecturer> lecturers = saveLecturers(requests, param);

        // save the lecturers
        List<Lecturer> savedLectures = lecturerRepository.saveAll(lecturers);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response.Builder<List<Lecturer>>()
                        .status(HttpStatus.CREATED.value())
                        .message("lecturers added successfully!")
                        .data(savedLectures)
                        .build()
        );
    }



    @Override
    public ResponseEntity<Response<List<LecturerList>>> getLecturers(String id, ConstantRequestParam param) {
        adminUtil.verifyUserIsAdmin(id);

        List<Lecturer> lecturers = lecturerRepository.findAllLectures(param);

        List<LecturerList> lecturerLists = getLecturerLists(lecturers);

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<List<LecturerList>>()
                        .status(HttpStatus.OK.value())
                        .message("lecturers")
                        .data(lecturerLists)
                        .build()
        );
    }

    private static List<LecturerList> getLecturerLists(List<Lecturer> lecturers) {
        List<LecturerList> lecturerLists = new ArrayList<>();

        for (Lecturer lecturer : lecturers) {
            lecturerLists.add(
                    new LecturerList(
                            lecturer.getId(),
                            lecturer.getOtherName() == null? lecturer.getFirstName() + " " + lecturer.getLastName() : lecturer.getFirstName() + " " + lecturer.getOtherName() + " " + lecturer.getLastName(),
                            lecturer.getProfilePictureUrl()
                    )
            );
        }
        return lecturerLists;
    }

    @Override
    public ResponseEntity<Response<?>> getDashboardData(String id, ConstantRequestParam param) {
        //Verify that the user is a lecturer
        lecturerUtil.verifyUserIsLecturer(id);

        Zone atomicZone = getLecturerZone(id, param); // get the lecturer's zone

        List<AssumptionOfDuty> duties = getAssumptionOfDutiesInAParticularZone(atomicZone, param); // get the list of assumption of duties from a particular zone

        List<Student> students = new ArrayList<>();
        Map<String, Integer> companies = new HashMap<>();
        duties.forEach(assumptionOfDuty -> {
            studentRepository.findById(assumptionOfDuty.getStudentId()).ifPresent(student -> {

                // add the student to the students list
                students.add(student);

                // add the company to the companies list
                String companyName = (assumptionOfDuty.getCompanyDetails().getCompanyName());
                if (companies.containsKey(companyName)) {
                    int count = companies.get(companyName);
                    companies.put(companyName, count + 1);
                } else {
                    companies.put(companyName, 1);
                }
            });
        });

        // put the data together
        List<StudentDto> studentData = new ArrayList<>(students.stream().map(adminUtil::buildStudentDtoFromStudent).toList());
        studentData.sort(Comparator.comparing(StudentDto::isSupervised));

        LecturerDashboardDataRes dataRes = new LecturerDashboardDataRes(
                new StudentsData(studentData, students.size()),
                new CompaniesData(companies, companies.size()),
                atomicZone != null? new OtherLecturersData(atomicZone.getLecturers().lecturers(), atomicZone.getLecturers().lecturers().size()) :
                                    new OtherLecturersData(Set.of(), 0)
        );

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("dashboard data")
                .data(dataRes)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Transactional(rollbackOn = LiaisonException.class)
    @Override
    public ResponseEntity<Response<?>> superviseStudent(String lecturerId, String studentId, ConstantRequestParam param) {

        //Verify that the user is a lecturer
        lecturerUtil.verifyUserIsLecturer(lecturerId);

        AtomicReference<Boolean> isSupervised = new AtomicReference<>();
        studentRepository.findById(studentId).ifPresent(student -> {
            try {
                isSupervised.set(!student.isSupervised()); // set the supervised state

                student.setSupervised(!student.isSupervised()); // set the supervised state to its opposite
                studentRepository.save(student);

                saveSupervisedStudent(student.getId(), lecturerId, param);
            } catch (Exception e) {
                throw new LiaisonException(Error.ERROR_SAVING_DATA, new Throwable(Message.FAILED_TO_SUPERVISE_STUDENT.label));
            }
        });

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.CREATED.value())
                .message(isSupervised.get()? "student supervised successfully!" : "student unsupervised successfully!")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Create the supervision data
    @Transactional(rollbackOn = LiaisonException.class)
    protected void saveSupervisedStudent(String studentId, String supervisorId, ConstantRequestParam param) {
        Supervision supervision = new Supervision();
        supervision.setCreatedAt(LocalDateTime.now());
        supervision.setUpdatedAt(LocalDateTime.now());

        supervision.setStudentId(studentId);
        supervision.setSupervisorId(supervisorId);

        supervision.setStartOfAcademicYear(UAcademicYear.startOfAcademicYear(param.startOfAcademicYear()));
        supervision.setEndOfAcademicYear(UAcademicYear.endOfAcademicYear(param.endOfAcademicYear()));

        supervision.setSemester(param.semester());
        supervision.setInternshipType(param.internship()? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT);

        try {
            supervisionRepository.save(supervision);
        } catch (Exception e) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA, new Throwable(Message.THE_STUDENT_CANNOT_BE_SUPERVISED_A_THIS_MOMENT.label));
        }
    }



    @Override
    public ResponseEntity<Response<?>> getStudentsSupervisedBySupervisor(String supervisorId, ConstantRequestParam param) {
        // make sure that the user is a supervisor
        lecturerUtil.verifyUserIsLecturer(supervisorId);

        // get the students supervised by this supervisor
        Set<StudentDto> supervisedStudents = getSupervisedStudents(supervisorId, param);

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("supervised students")
                .data(supervisedStudents)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Override
    public void generateStudentSupervisionReport(HttpServletResponse response, String supervisorId, ConstantRequestParam param) throws IOException {
        // assert that the user is a supervisor
        lecturerUtil.verifyUserIsLecturer(supervisorId);

        response.setContentType("application/octet-stream");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(formatter);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student_supervision_report" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        // get the students supervised by this supervisor
        Set<StudentDto> supervisedStudents = getSupervisedStudents(supervisorId, param);

        SupervisionReportGenerator reportGenerator = new SupervisionReportGenerator(supervisedStudents);
        reportGenerator.generateSupervisionReport(response);
    }

    /**
     * This method retrieves a set of {@link Student} objects supervised by a specific supervisor based on the provided criteria.
     * It fetches supervision records for the supervisor and retrieves the corresponding student details.
     *
     * @param supervisorId the ID of the supervisor
     * @param param the {@link ConstantRequestParam} object containing details such as academic year, semester,
     *              and internship type
     * @return a set of {@link Student} objects supervised by the specified supervisor
     */
    public Set<StudentDto> getSupervisedStudents(String supervisorId, ConstantRequestParam param) {
        Set<Student> supervisedStudents = new HashSet<>();
        supervisionRepository.findAllStudentsSupervisedBySpecificSupervisor(supervisorId, param).forEach(
                supervision -> studentRepository.findById(supervision.getStudentId()).ifPresent(supervisedStudents::add)
        );
        return supervisedStudents.stream().map(adminUtil::buildStudentDtoFromStudent).collect(Collectors.toSet()).stream().filter(StudentDto::isSupervised).collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<Response<?>> getStudentsLocation(String lecturerId, ConstantRequestParam param) {
        //Verify that the user is a lecturer
        lecturerUtil.verifyUserIsLecturer(lecturerId);

        Zone atomicZone = getLecturerZone(lecturerId, param); // get the lecturer's zone

        List<AssumptionOfDuty> duties = getAssumptionOfDutiesInAParticularZone(atomicZone, param); // get the list of assumption of duties from a particular zone

        List<StudentLocationData> locationData = new ArrayList<>();
        duties.forEach(assumptionOfDuty -> {
            studentRepository.findById(assumptionOfDuty.getStudentId()).ifPresent(student ->
                    locationData.add(adminUtil.createStudentLocationData(student, assumptionOfDuty)));
        });

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("students location")
                .data(locationData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Override
    public ResponseEntity<Response<?>> getTopIndustries(String lecturerId, ConstantRequestParam param) {
        // Verify that the uer is an admin
        lecturerUtil.verifyUserIsLecturer(lecturerId);

        Zone zone = getLecturerZone(lecturerId, param);

        List<AssumptionOfDuty> duties = getAssumptionOfDutiesInAParticularZone(zone, param);

        List<TopCompaniesData> topCompaniesData = new ArrayList<>();
        duties.forEach(assumptionOfDuty -> {
            String companyName = assumptionOfDuty.getCompanyDetails().getCompanyName();
            String companyTown = assumptionOfDuty.getCompanyDetails().getCompanyTown();
            String companyExactLocation = assumptionOfDuty.getCompanyDetails().getCompanyExactLocation();

            // make sure there is at least a single data in the top companies
            Optional<TopCompaniesData> existingCompany = topCompaniesData.stream()
                    .filter(comp -> companyName.equals(comp.getName()))
                    .findFirst();

            if (existingCompany.isPresent()) {
                existingCompany.get().setTotalStudents(existingCompany.get().getTotalStudents() + 1);
            } else {
                topCompaniesData.add(createTopCompanyData(companyName, companyTown, companyExactLocation));
            }
        });

        topCompaniesData.sort(Comparator.comparing(TopCompaniesData::getTotalStudents).reversed());
        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("top companies")
                .data(topCompaniesData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Override
    public ResponseEntity<Response<?>> getStudentsFacultyAnalytics(String lecturerId, ConstantRequestParam param) {
        // verify that the user is a lecturer
        lecturerUtil.verifyUserIsLecturer(lecturerId);

        Zone zone = getLecturerZone(lecturerId, param);

        List<FacultyData> facultyData = new ArrayList<>();
        List<AssumptionOfDuty> duties = getAssumptionOfDutiesInAParticularZone(zone, param);

        duties.forEach(assumptionOfDuty -> studentRepository.findById(assumptionOfDuty.getStudentId())
                .ifPresent(student -> {

                    Optional<FacultyData> existingData = facultyData.stream()
                            .filter(data -> data.getName().equals(student.getStudentFaculty()))
                            .findFirst();

                    if (existingData.isPresent()) {
                        existingData.get().setTotalStudents(existingData.get().getTotalStudents() + 1);
                    } else {
                        facultyData.add(new FacultyData(
                                student.getStudentFaculty(),
                                1
                        ));
                    }
                }));

        StudentsFacultyData studentsFacultyData = new StudentsFacultyData(facultyData, duties.size());

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<>()
                        .status(HttpStatus.OK.value())
                        .message("students faculty analytics")
                        .data(studentsFacultyData)
                .build());
    }

    /**
     * This method retrieves the {@link Zone} where a specific lecturer is located, based on their ID.
     * It searches through all available zones to find the one containing the given lecturer ID.
     *
     * @param lecturerId the ID of the lecturer whose zone is to be retrieved
     * @return the {@link Zone} object containing the lecturer, or {@code null} if no matching zone is found
     */
    private Zone getLecturerZone(String lecturerId, ConstantRequestParam param) {
        AtomicReference<Zone> atomicZone = new AtomicReference<>();
        zoneSpecification.findZonesUsingZoneTypeAndAcademicDates(param).forEach(
                zone -> {
                    if (zone.getLecturers().lecturers().contains(lecturerId)) {
                        atomicZone.set(zone);
                    }
                }
        );

        return atomicZone.get();
    }

    /**
     * This method retrieves a list of {@link AssumptionOfDuty} objects that are located within a specific zone.
     * It filters the assumptions of duty based on the region and towns of the specified {@link Zone}.
     *
     * @param atomicZone the {@link Zone} object containing the region and towns to filter assumptions of duty
     * @return a list of {@link AssumptionOfDuty} objects that match the specified zone
     */
    private List<AssumptionOfDuty> getAssumptionOfDutiesInAParticularZone(Zone atomicZone, ConstantRequestParam param) {
        List<AssumptionOfDuty> duties = new ArrayList<>();
        assumptionOfDutyRepository.findAllDuties(param).iterator().forEachRemaining(assumptionOfDuty ->
                {
                    if (assumptionOfDuty.getCompanyDetails().getCompanyRegion().equals(atomicZone.getRegion()) && atomicZone.getTowns().towns().contains(assumptionOfDuty.getCompanyDetails().getCompanyTown())) {
                        duties.add(assumptionOfDuty);
                    }
                }
        );

        return duties;
    }


    /**
     * This method creates a new {@link TopCompaniesData} object with the provided company details.
     * The initial ranking is set to 1.
     *
     * @param companyName the name of the company
     * @param companyTown the town where the company is located
     * @param companyExactLocation the exact location of the company
     * @return a new {@link TopCompaniesData} object populated with the given details and an initial ranking of 1
     */
    private TopCompaniesData createTopCompanyData(String companyName, String companyTown, String companyExactLocation) {
        return new TopCompaniesData(
                companyName,
                companyTown,
                companyExactLocation,
                1
        );
    }

    /**
     * This method verifies if any of the email addresses in the provided list of NewLecturerRequest objects
     * already exist in the system. If any email is found to be associated with an existing Lecturer,
     * a LiaisonException is thrown with the list of duplicate emails.
     *
     * @param requests a list of NewLecturerRequest objects containing the email addresses to be verified
     * @throws LiaisonException if one or more email addresses already exist in the system
     */
    private void verifyExistingEmails(List<NewLecturerRequest> requests) {

        List<String> emails = new ArrayList<>();
        for (NewLecturerRequest request : requests) {
            Optional<Lecturer> lecturer = lecturerRepository.findByEmail(request.email());

            lecturer.ifPresent(lec -> emails.add(lec.getEmail()));
        }

        if (!emails.isEmpty()) {
            throw new LiaisonException(Error.EMAIL_ALREADY_EXISTS, new Throwable(Message.THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM.label + ": " + emails));
        }
    }

    /**
     * This method processes a list of NewLecturerRequest objects to create and save new Lecturer entities.
     * It initializes each Lecturer with a unique ID, current timestamps, and the provided request data,
     * such as name, email, profile, phone, and company. The password is encoded before being set,
     * and the role is set to LECTURER. All created lecturers are added to a list and returned.
     *
     * @param requests a list of NewLecturerRequest objects containing the data for each new lecturer
     * @return a list of Lecturer entities created from the provided requests
     */
    private List<Lecturer> saveLecturers(List<NewLecturerRequest> requests, ConstantRequestParam param) {

        List<Lecturer> lecturers = new ArrayList<>();
        for (NewLecturerRequest request : requests) {

            Lecturer lecturer = new Lecturer();

            lecturer.setLecturerId(RandomStringUtils.randomAlphanumeric(7).toUpperCase());
            lecturer.setCreatedAt(LocalDateTime.now());
            lecturer.setUpdatedAt(LocalDateTime.now());

            lecturer.setFirstName(request.firstName());
            lecturer.setOtherName(request.otherName());
            lecturer.setLastName(request.lastName());
            lecturer.setEmail(request.email());
            lecturer.setProfilePictureUrl(request.userProfilePicture());

            lecturer.setPhone(request.phone());
            lecturer.setCompany(request.company());

            lecturer.setFaculty(request.faculty());
            lecturer.setDepartment(request.department());

            String pass = passwordEncoder.encode(request.password());
            lecturer.setPassword(pass);

            lecturer.setRole(UserRoles.LECTURER);
            lecturer.setZoneLead(false);

            lecturer.setSemester(param.semester());

            // add the lecturer to the lecturers' list
            lecturers.add(lecturer);
        }

        return lecturers;
    }
}
