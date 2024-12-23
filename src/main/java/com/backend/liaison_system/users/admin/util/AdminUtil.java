package com.backend.liaison_system.users.admin.util;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.Status;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.dao.CompanyDetails;
import com.backend.liaison_system.users.admin.dao.StudentDetails;
import com.backend.liaison_system.users.admin.dao.StudentLocationData;
import com.backend.liaison_system.users.admin.dto.StudentDto;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.backend.liaison_system.exception.Error.USER_NOT_FOUND;

@Component
public class AdminUtil {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public AdminUtil(PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    public StudentDto buildStudentDtoFromStudent(Student student) {
        return new StudentDto (
                student.getId(),
                student.getStudentFirstName() + student.getStudentLastName() + student.getStudentOtherName(),
                student.getStudentDepartment(),
                student.getStudentFaculty(),
                student.getStudentAge(),
                student.getStudentEmail(),
                student.getStudentGender(),
                student.getStudentPhone(),
                student.getProfilePictureUrl(),
                student.getStudentAbout(),
                student.getStudentCourse(),
                student.getPlaceOfInternship(),
                student.isSupervised(),
                student.isAssumeDuty(),
                student.getStartDate(),
                student.getEndDate(),
                student.getEndDate().isBefore(LocalDateTime.now())? Status.COMPLETED : Status.IN_PROGRESS
        );
    }

    /**
     * This is a function to extract all the information in a row and turn it into a Student Object
     * @param row the Excel row containing all the info on the student
     * @return a Student Object
     */
    public Student buildStudentFromExcel(Row row, ConstantRequestParam param) {
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
        student.setStartDate(row.getCell(14).getLocalDateTimeCellValue());
        student.setEndDate(row.getCell(15).getLocalDateTimeCellValue());
        student.setStatus(Status.IN_PROGRESS);
        student.setPassword(passwordEncoder.encode(password));
        student.setRole(UserRoles.STUDENT);
        student.setAssumeDuty(false);
        student.setSupervised(false);
        student.setSemester(param.semester());
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return student;
    }

    /**
     * This is a simple helper function that takes in each cell and returns its value as a String
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

    /**
     * This method verifies if the user with the given ID is an admin.
     * It checks the user's role and throws a LiaisonException if the user is not authorized (i.e., not an admin)
     * or if the user is not found in the admin repository.
     *
     * @param id the ID of the user to be verified as an admin
     * @throws LiaisonException if the user is not authorized (not an admin) or if the user is not found
     */
    public void verifyUserIsAdmin(String id) {
        adminRepository.findById(id).ifPresentOrElse((admin -> {
                    if (!admin.getRole().equals(UserRoles.ADMIN)) {
                        throw new LiaisonException(Error.UNAUTHORIZED_USER, new Throwable(Message.THE_USER_IS_NOT_AUTHORIZED.label));
                    }
                }),
                () -> {throw new LiaisonException(USER_NOT_FOUND, new Throwable(Message.USER_NOT_FOUND_CAUSE.label));}
        );
    }


    public StudentLocationData createStudentLocationData(Student student, AssumptionOfDuty assumptionOfDuty) {
        String studentName = student.getStudentOtherName() == null? student.getStudentFirstName() + student.getStudentLastName() : student.getStudentFirstName() + student.getStudentOtherName() + student.getStudentLastName();
        return new StudentLocationData(
                new StudentDetails(
                        student.getId(),
                        studentName,
                        student.getStudentEmail(),
                        student.getStudentPhone(),
                        student.getProfilePictureUrl(),
                        student.isSupervised(),
                        student.isAssumeDuty()
                ),
                new CompanyDetails(
                        assumptionOfDuty.getCompanyDetails().getCompanyName(),
                        assumptionOfDuty.getCompanyDetails().getCompanyEmail(),
                        assumptionOfDuty.getCompanyDetails().getCompanyPhone(),
                        assumptionOfDuty.getCompanyDetails().getCompanyRegion(),
                        assumptionOfDuty.getCompanyDetails().getCompanyExactLocation()
                ),
                assumptionOfDuty.getCompanyDetails().getCompanyLatitude(),
                assumptionOfDuty.getCompanyDetails().getCompanyLongitude()
        );
    }
}
