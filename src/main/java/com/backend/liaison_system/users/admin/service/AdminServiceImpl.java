package com.backend.liaison_system.users.admin.service;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.dao.LecturerData;
import com.backend.liaison_system.users.admin.dto.AdminPageRequest;
import com.backend.liaison_system.users.admin.dao.TabularDataResponse;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.users.admin.dto.StudentDto;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

import static com.backend.liaison_system.exception.Error.*;
import static com.backend.liaison_system.exception.Message.THE_FOLLOWING_FIELDS_ARE_EMPTY;
import static com.backend.liaison_system.exception.Message.THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUtil adminUtil;
    private final LecturerRepository lecturerRepository;

    public AdminServiceImpl(AdminRepository adminRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder, AdminUtil adminUtil, LecturerRepository lecturerRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUtil = adminUtil;
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public ResponseEntity<Response<Admin>> creatNewAdmin(NewUserRequest request) {
        Optional<Admin> admin = adminRepository.findByEmail(request.getEmail());
        if (admin.isPresent()) {
            throw new LiaisonException(EMAIL_ALREADY_EXISTS, new Throwable(THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM.label));
        }

        //create the admin
        Admin createdAdmin = createNewAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response.Builder<Admin>()
                        .status(HttpStatus.CREATED.value())
                        .message("admin created successfully")
                        .data(createdAdmin)
                        .build()
        );
    }


    /**
     * This method validates the fields in the NewUserRequest object to check for any empty or null values.<br>
     * It returns a list of field names that are empty or null.
     *
     * @param request the NewUserRequest object containing the fields to be validated
     */
    private void validateRequestFields(NewUserRequest request) {
        List<String> emptyFields = new ArrayList<>();
        if (request.getEmail() == null || request.getEmail().isEmpty()) emptyFields.add("email");
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) emptyFields.add("firstname");
        if (request.getLastName() == null || request.getLastName().isEmpty()) emptyFields.add("lastname");
        if (request.getPassword() == null || request.getPassword().isEmpty()) emptyFields.add("password");

        if (!emptyFields.isEmpty()) throw new LiaisonException(REQUIRED_FIELDS_ARE_EMPTY, new Throwable(THE_FOLLOWING_FIELDS_ARE_EMPTY.label + emptyFields));
    }

    private Admin createNewAdmin(NewUserRequest request) {

        // validate the input fields
        validateRequestFields(request);

        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setEmail(request.getEmail());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setOtherName(request.getOtherName());
        admin.setRole(request.getRole());

        // encode the password before setting it
        String encodedPass = passwordEncoder.encode(request.getPassword());
        admin.setPassword(encodedPass);

        try {
            return adminRepository.save(admin);

        } catch (Exception exception) {
            throw new LiaisonException(ERROR_SAVING_DATA);
        }

    }

    @Override
    public Response<?> uploadStudents(String adminID, MultipartFile file, boolean internship) {
        // confirm user is an admin
        adminUtil.verifyUserIsAdmin(adminID);

        try {

            //create an arrayList of students
            List<Student> students = new ArrayList<>();

            //Turn the file into an InputStream and turn into a workbook
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            Workbook workbook;
            if (FileMagic.valueOf(inputStream) == FileMagic.OOXML) {
                workbook = new XSSFWorkbook(inputStream); // For `.xlsx` files
            } else if (FileMagic.valueOf(inputStream) == FileMagic.OLE2) {
                workbook = new HSSFWorkbook(inputStream); // For `.xls` files
            } else  {
                throw new LiaisonException(ERROR_SAVING_DATA);
            }

            Sheet sheet = workbook.getSheetAt(0);

            //For each row in the sheet extract the student details
            for(Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                Student currentStudent = adminUtil.buildStudentFromExcel(row);
                //Ensure student does not already exist
                Optional<Student> studentCheck = studentRepository.findByEmail(currentStudent.getEmail());
                if(studentCheck.isEmpty()) {
                    if (!internship) {
                        currentStudent.setInternshipType(InternshipType.SEMESTER_OUT);
                    } else {
                        currentStudent.setInternshipType(InternshipType.INTERNSHIP);
                    }
                    students.add(currentStudent);
                }
            }
            studentRepository.saveAll(students);
            return new Response.Builder<>()
                    .status(HttpStatus.CREATED.value())
                    .message("Student accounts created successfully")
                    .build();
        } catch (Exception e) {
            throw new LiaisonException(ERROR_SAVING_DATA);
        }
    }

    @Override
    public Response<?> getStudents(String adminID, ConstantRequestParam param, int page, int size) {
        // Verify that the user is an admin
        adminUtil.verifyUserIsAdmin(adminID);

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentRepository.findAll(param, pageable);

        int studentSize = studentRepository.findAll().size();
        List<StudentDto> studentDtoList = students.stream().map(adminUtil::buildStudentDtoFromStudent).toList();
        TabularDataResponse response = new TabularDataResponse
                .Builder()
                .currentPage(students.getNumber())
                .pageSize(students.getSize())
                .totalData(studentSize)
                .totalPages(students.getTotalPages())
                .students(studentDtoList)
                .build();
        return new  Response.Builder<>()
                .message("Students Retrieved")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }


    @Override
    public ResponseEntity<Response<?>> getLecturers(String id, AdminPageRequest request) {

        // verify the user's role before proceeding with other operations
        adminUtil.verifyUserIsAdmin(id);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Lecturer> lecturers;

        if (request.getFind() != null) {
            lecturers = searchLecturer(request);
        } else {
            lecturers = lecturerRepository.findAll(pageable);
        }

        int lecturerDataSize = lecturerRepository.findAll().size();

        TabularDataResponse response = new TabularDataResponse
                .Builder()
                .currentPage(lecturers.getNumber())
                .pageSize(lecturers.getSize())
                .totalData(lecturerDataSize)
                .totalPages(lecturers.getTotalPages())
                .page(lecturers)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<>()
                        .message("all lecturers")
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    /**
     * Retrieves a paginated list of {@link Lecturer} entities based on the search criteria provided
     * in the {@link AdminPageRequest}.
     *
     * <p>This method constructs a {@link Pageable} object using the page number and size specified
     * in the request. It then delegates the actual search and pagination to the {@link LecturerRepository},
     * which performs the query based on the search key provided in the request.</p>
     *
     * <p>The page number from the request is adjusted (decremented by 1) to conform to the zero-based
     * index required by Spring Data's {@link PageRequest}.</p>
     *
     * @param request an {@link AdminPageRequest} containing the search key and pagination details.
     *                The search key should be non-null, and the page number and size should be positive integers.
     * @return a {@link Page} of {@link Lecturer} entities that match the search criteria and fit within
     *         the specified pagination parameters.
     */
    private Page<Lecturer> searchLecturer(AdminPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize());

        return  lecturerRepository.findLecturerBySearchKey(request.getFind(), pageable);
    }

    /**
     * This method verifies if the user with the given lecturer ID is a lecturer.
     * It checks the user's role and throws a LiaisonException if the user's role is not allowed (i.e., not a lecturer)
     * or if the user is not found in the lecturer repository.
     *
     * @param lecturerId the ID of the lecturer to be verified
     * @throws LiaisonException if the user's role is incorrect or if the user is not found
     */
    private Lecturer verifyUserIsLecturer(String lecturerId) {

        return lecturerRepository.findById(lecturerId)
                .filter(lec -> lec.getRole().equals(UserRoles.LECTURER))
                .orElseThrow(() -> new LiaisonException(WRONG_USER_ROLE, new Throwable(Message.THE_USER_ROLE_IS_NOT_ALLOWED.label)));
    }

    /**
     * This method retrieves a list of display pictures (dps) of lecturers from the same department,
     * excluding the lecturer with the provided ID. It filters lecturers based on the department and
     * removes the lecturer with the matching ID from the result.
     *
     * @param department the department to search for lecturers
     * @param id the ID of the lecturer to be excluded from the results
     * @return a list of display pictures (dps) of other lecturers from the same department, or null if none are found
     */
    private List<String> getOthersFromSameDepartment(String department, String id) {
        List<String> dps = new ArrayList<>();

        List<Lecturer> lecturers = lecturerRepository.findAllByDepartment(department)
                .stream()
                .filter(lecturer -> !Objects.equals(lecturer.getLecturerId(), id))
                .toList();
        if (lecturers.isEmpty()) {
            return null;
        } else {
            for (Lecturer lecturer : lecturers) {
                dps.add(lecturer.getDp());
            }
        }

        return dps;
    }

    @Override
    public ResponseEntity<Response<LecturerData>> getLecturer(String id, String lecturerId) {

        // verify the user requesting for the lecturer details is an admin
        adminUtil.verifyUserIsAdmin(id);

        // find the user, make sure the user is a lecturer before proceeding
        Lecturer lecturer = verifyUserIsLecturer(lecturerId);

        // get the other lectures from the same department
        List<String> others = getOthersFromSameDepartment(lecturer.getDepartment(), lecturer.getLecturerId());

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<LecturerData>()
                        .status(HttpStatus.OK.value())
                        .message("lecturer details")
                        .data(new LecturerData(
                                "#" + lecturer.getLecturerId(),
                                lecturer.getDp(),
                                lecturer.getLastName() + " " + lecturer.getFirstName(),
                                lecturer.getDepartment(),
                                lecturer.getPhone(),
                                lecturer.getEmail(),
                                "lecturer.getAge()",
                                lecturer.getFaculty(),
                                "lecturer.getGender()",
                                others
                        )).build()
        );
    }
}
