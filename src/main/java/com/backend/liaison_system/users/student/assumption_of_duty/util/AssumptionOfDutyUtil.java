package com.backend.liaison_system.users.student.assumption_of_duty.util;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.student.StudentRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.CompanyDetails;
import com.backend.liaison_system.util.UAcademicYear;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class AssumptionOfDutyUtil {

    private final AdminUtil adminUtil;
    private final StudentRepository studentRepository;

    public AssumptionOfDutyUtil(AdminUtil adminUtil, StudentRepository studentRepository) {
        this.adminUtil = adminUtil;
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves a student by their index number.
     * This method attempts to find a student based on the provided student index number.
     * @return an {@link Optional} containing the student if found, or an empty {@link Optional} if no student
     *         is found with the provided index number
     * @throws IllegalArgumentException if the provided student index number is {@code null} or empty
     */
    public AssumptionOfDuty buildDutyFromExcel(Row row, ConstantRequestParam param, String studentIndexNumber) {

        AssumptionOfDuty assumption = new AssumptionOfDuty();
        studentRepository.findByEmail(studentIndexNumber).ifPresent(student ->
                {
                    assumption.setStudentId(student.getId());
                    assumption.setStudentIndexNumber(student.getEmail());
                    assumption.setDateCreated(LocalDateTime.now());
                    assumption.setDateUpdated(LocalDateTime.now());
                    assumption.setUpdated(false);

                    assumption.setDateCommenced(adminUtil.getCellValueAsString(row.getCell(1)));

                    assumption.setInternship(param.internship());
                    assumption.setStartOfAcademicYear(UAcademicYear.startOfAcademicYear(param.startOfAcademicYear()));
                    assumption.setEndOfAcademicYear(UAcademicYear.endOfAcademicYear(param.endOfAcademicYear()));

                    assumption.setSemester(param.semester());

                    CompanyDetails companyDetails = new CompanyDetails();
                    companyDetails.setCompanyName(adminUtil.getCellValueAsString(row.getCell(6)));
                    companyDetails.setCompanyPhone(adminUtil.getCellValueAsString(row.getCell(7)));
                    companyDetails.setCompanyExactLocation(adminUtil.getCellValueAsString(row.getCell(8)));

                    companyDetails.setCompanyTown(adminUtil.getCellValueAsString(row.getCell(9)));
                    companyDetails.setCompanyRegion(adminUtil.getCellValueAsString(row.getCell(10)));
                    companyDetails.setCompanyAddress(adminUtil.getCellValueAsString(row.getCell(11)));

                    companyDetails.setCompanyEmail(adminUtil.getCellValueAsString(row.getCell(12)));
                    companyDetails.setCompanySupervisor(adminUtil.getCellValueAsString(row.getCell(13)));
                    companyDetails.setSupervisorPhone(adminUtil.getCellValueAsString(row.getCell(14)));

                    companyDetails.setLetterTo(adminUtil.getCellValueAsString(row.getCell(15)));

                    double lat = Double.parseDouble(adminUtil.getCellValueAsString(row.getCell(16)));
                    double lng = Double.parseDouble(adminUtil.getCellValueAsString(row.getCell(17)));

                    companyDetails.setCompanyLongitude(lng);
                    companyDetails.setCompanyLatitude(lat);

                    assumption.setCompanyDetails(companyDetails);
                }
        );
        return assumption;
    }
}
