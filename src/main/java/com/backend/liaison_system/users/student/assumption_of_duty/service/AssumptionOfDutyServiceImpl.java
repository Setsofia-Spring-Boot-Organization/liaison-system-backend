package com.backend.liaison_system.users.student.assumption_of_duty.service;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.CompanyDetails;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.AssumptionOfDutyRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.requests.CreateNewAssumptionOfDuty;
import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import com.backend.liaison_system.google_maps.GoogleMapServices;
import jakarta.transaction.Transactional;
import org.cloudinary.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AssumptionOfDutyServiceImpl implements AssumptionOfDutyService {

    private final StudentRepository studentRepository;
    private final AssumptionOfDutyRepository assumptionOfDutyRepository;

    public AssumptionOfDutyServiceImpl(StudentRepository studentRepository, AssumptionOfDutyRepository assumptionOfDutyRepository) {
        this.studentRepository = studentRepository;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
    }

    @Transactional(rollbackOn = LiaisonException.class)
    @Override
    public ResponseEntity<Response<AssumptionOfDuty>> createNewAssumptionOfDuty(String studentID, ConstantRequestParam param, CreateNewAssumptionOfDuty newAssumptionOfDuty) {

        // confirm the identity of the student
        Student student = studentRepository.findById(studentID).orElseThrow(() -> new LiaisonException(Error.USER_NOT_FOUND, new Throwable(Message.USER_NOT_FOUND_CAUSE.label)));

        AssumptionOfDuty assumption = createAssumption(param, newAssumptionOfDuty, student);

        try {
            // update the students assumeDuty after he/she submitted it
            student.setAssumeDuty(true);
            studentRepository.save(student);

            Response<AssumptionOfDuty> response = new Response<>(
                    HttpStatus.CREATED.value(),
                    "assumption of duty submitted successfully!",
                    assumption
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA);
        }
    }

    private AssumptionOfDuty createAssumption(ConstantRequestParam param, CreateNewAssumptionOfDuty newAssumptionOfDuty, Student student) {

        AssumptionOfDuty assumption = new AssumptionOfDuty();

        assumption.setStudentId(student.getId());
        assumption.setDateCreated(LocalDateTime.now());
        assumption.setDateUpdated(LocalDateTime.now());

        assumption.setDateCommenced(newAssumptionOfDuty.dateCommenced());

        assumption.setInternship(param.internship());
        assumption.setStartOfAcademicYear(param.startOfAcademicYear());
        assumption.setEndOfAcademicYear(param.endOfAcademicYear());

        // get the company details
        CompanyDetails companyDetails = createCompanyDetails(newAssumptionOfDuty);

        assumption.setCompanyDetails(companyDetails);

        try {
            return assumptionOfDutyRepository.save(assumption);
        } catch (Exception e) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA);
        }
    }

    private CompanyDetails createCompanyDetails(CreateNewAssumptionOfDuty newAssumptionOfDuty) {

        // use the Google map service to find the lng and lat of the place
        GoogleMapServices googleMapServices = new GoogleMapServices();
        JSONObject jsonObject = googleMapServices.getCoordinates(newAssumptionOfDuty.companyExactLocation());
        if ("OK".equals(jsonObject.getString("status"))) {
            JSONObject location = jsonObject
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            return createCompanyDetails(newAssumptionOfDuty, lng, lat);
        }

        throw new LiaisonException(Error.ERROR_SAVING_DATA, new Throwable(Message.THE_EXACT_COMPANY_LOCATION_DOES_NOT_EXISTS.label));
    }

    private static CompanyDetails createCompanyDetails(CreateNewAssumptionOfDuty newAssumptionOfDuty, double lng, double lat) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setCompanyName(newAssumptionOfDuty.companyName());
        companyDetails.setCompanyPhone(newAssumptionOfDuty.companyPhone());
        companyDetails.setCompanyExactLocation(newAssumptionOfDuty.companyExactLocation());

        companyDetails.setCompanyTown(newAssumptionOfDuty.companyTown());
        companyDetails.setCompanyRegion(newAssumptionOfDuty.companyRegion());
        companyDetails.setCompanyAddress(newAssumptionOfDuty.companyAddress());

        companyDetails.setCompanyEmail(newAssumptionOfDuty.companyEmail());
        companyDetails.setCompanySupervisor(newAssumptionOfDuty.companySupervisor());
        companyDetails.setSupervisorPhone(newAssumptionOfDuty.supervisorPhone());

        companyDetails.setLetterTo(newAssumptionOfDuty.letterTo());
        companyDetails.setCompanyLongitude(lng);
        companyDetails.setCompanyLatitude(lat);
        return companyDetails;
    }


}
