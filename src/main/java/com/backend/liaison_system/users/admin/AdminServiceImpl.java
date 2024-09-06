package com.backend.liaison_system.users.admin;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.dto.StudentDto;
import com.backend.liaison_system.enums.Status;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.backend.liaison_system.exception.Cause.THE_FOLLOWING_FIELDS_ARE_EMPTY;
import static com.backend.liaison_system.exception.Cause.THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM;
import static com.backend.liaison_system.exception.Error.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Response<Admin>> creatNewAdmin(NewUserRequest request) {
        Optional<Admin> admin = adminRepository.findByEmail(request.getEmail());
        if (admin.isPresent()) {
            throw new LiaisonException(EMAIL_ALREADY_EXISTS, new Throwable(THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM.label));
        }

        //create the admin
        Admin createdAdmin = createNewAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.<Admin>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("admin created successfully")
                        .data(createdAdmin)
                        .build()
        );
    }

    /*
      This section contains helper methods that provide additional utility functions
      to support the main application logic.<br>
      These methods typically perform common tasks such as validation, data formatting,
      or other reusable operations that assist in the overall functionality of the application.
     */

    /**
     * This method validates the fields in the NewUserRequest object to check for any empty or null values.<br>
     * It returns a list of field names that are empty or null.
     *
     * @param request the NewUserRequest object containing the fields to be validated
     */
    void validateRequestFields(NewUserRequest request) {
        List<String> emptyFields = new ArrayList<>();
        if (request.getEmail() == null || request.getEmail().isEmpty()) emptyFields.add("email");
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) emptyFields.add("firstname");
        if (request.getLastName() == null || request.getLastName().isEmpty()) emptyFields.add("lastname");
        if (request.getPassword() == null || request.getPassword().isEmpty()) emptyFields.add("password");

        if (!emptyFields.isEmpty()) throw new LiaisonException(REQUIRED_FIELDS_ARE_EMPTY, new Throwable(THE_FOLLOWING_FIELDS_ARE_EMPTY.label + emptyFields));
    }

    Admin createNewAdmin(NewUserRequest request) {

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
            log.error("Unable to save Data: {}", exception.getMessage());
            throw new LiaisonException(ERROR_SAVING_DATA);
        }

    }

    @Override
    public Response<?> uploadStudents(MultipartFile file) {
        try {
            log.info("File received: {}", file.getOriginalFilename());
            List<Student> students = new ArrayList<>();
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            Workbook workbook;
            if (FileMagic.valueOf(inputStream) == FileMagic.OOXML) {
                workbook = new XSSFWorkbook(inputStream); // For `.xlsx` files
            } else if (FileMagic.valueOf(inputStream) == FileMagic.OLE2) {
                workbook = new HSSFWorkbook(inputStream); // For `.xls` files
            } else  {
                log.error("Unsupported file magic: {}", FileMagic.valueOf(inputStream));
                throw new LiaisonException(ERROR_SAVING_DATA);
            }
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                Student currentStudent = buildStudentFromExcel(row);
                students.add(currentStudent);
            }
            studentRepository.saveAll(students);
            log.info("Students saved to database");
            return Response.builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Student accounts created successfully")
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new LiaisonException(ERROR_SAVING_DATA);
        }
    }

    /**
     * This is a function to extract all the information in a row and turn it into a Student Object
     * @param row the Excel row containing all the info on the student
     * @return a Student Object
     */
    private Student buildStudentFromExcel(Row row) {
        String password = getCellValueAsString(row.getCell(9));
        Student student = new Student();
        student.setEmail(getCellValueAsString(row.getCell(0)));
        student.setStudentFirstName(getCellValueAsString(row.getCell(1)));
        student.setStudentLastName(getCellValueAsString(row.getCell(2)));
        student.setStudentOtherName(getCellValueAsString(row.getCell(3)));
        student.setStudentFaculty(getCellValueAsString(row.getCell(4)));
        student.setStudentDepartment(getCellValueAsString(row.getCell(5)));
        student.setStudentAge(getCellValueAsString(row.getCell(6)));
        student.setStudentGender(getCellValueAsString(row.getCell(7)));
        student.setStudentCourse(getCellValueAsString(row.getCell(10)));
        student.setStudentEmail(getCellValueAsString(row.getCell(11)));
        student.setStudentPhone(getCellValueAsString(row.getCell(12)));
        student.setPlaceOfInternship(getCellValueAsString(row.getCell(13)));
        student.setStartDate(getCellValueAsString(row.getCell(14)));
        student.setEndDate(getCellValueAsString(row.getCell(15)));
        student.setStatus(Status.IN_PROGRESS);
        student.setPassword(passwordEncoder.encode(password));
        student.setRole(UserRoles.STUDENT);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return student;
    }

    /**
     * This is a simple helper function that takes in each cell and returns it's value as a String
     * @param cell the Cell containing the value
     * @return a String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    yield String.valueOf((long) numericValue);
                } else {
                    yield BigDecimal.valueOf(numericValue).toPlainString();
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }


    @Override
    public Response<?> getStudents(Long adminId) {
        List<Student> students = studentRepository.findAll();
        List<StudentDto> studentDtos = students.stream().map(this::buildStudentDtoFromStudent).toList();
        return Response
                .builder()
                .message("Students Retrieved")
                .status(HttpStatus.OK.value())
                .data(studentDtos)
                .build();
    }

    private StudentDto buildStudentDtoFromStudent(Student student) {
        return StudentDto
                .builder()
                .id(student.getEmail())
                .studentFirstName(student.getStudentFirstName())
                .studentLastName(student.getStudentLastName())
                .studentOtherName(student.getStudentOtherName())
                .studentDepartment(student.getStudentDepartment())
                .studentAge(student.getStudentAge())
                .studentGender(student.getStudentGender())
                .studentCourse(student.getStudentCourse())
                .studentEmail(student.getStudentEmail())
                .studentPhone(student.getStudentPhone())
                .placeOfInternship(student.getPlaceOfInternship())
                .startDate(student.getStartDate())
                .endDate(student.getEndDate())
                .studentAbout(student.getStudentAbout())
                .build();
    }

}
