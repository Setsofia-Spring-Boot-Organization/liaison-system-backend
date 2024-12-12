package com.backend.liaison_system.users.student.assumption_of_duty.service;

import com.backend.liaison_system.google_maps.responses.GMapLocation;
import com.backend.liaison_system.region.util.RegionUtil;
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
import com.backend.liaison_system.users.student.util.StudentUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AssumptionOfDutyServiceImpl implements AssumptionOfDutyService {

    private final StudentRepository studentRepository;
    private final AssumptionOfDutyRepository assumptionOfDutyRepository;
    private final RegionUtil regionUtil;
    private final StudentUtil studentUtil;

    public AssumptionOfDutyServiceImpl(StudentRepository studentRepository, AssumptionOfDutyRepository assumptionOfDutyRepository, RegionUtil regionUtil, StudentUtil studentUtil) {
        this.studentRepository = studentRepository;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
        this.regionUtil = regionUtil;
        this.studentUtil = studentUtil;
    }


    @Override
    public ResponseEntity<Response<?>> getStudentAssumptionOfDuties(String studentId) {

        // verify that the user is a students
        studentUtil.verifyUserIsStudent(studentId);

        //filtering the data by the student's id
        List<AssumptionOfDuty> duties =  assumptionOfDutyRepository.findAll().stream().filter(assumptionOfDuty -> assumptionOfDuty.getStudentId().equals(studentId)).toList();

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("duties")
                .data(duties)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
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

            // update the region and add the student's town if it does not exist in the region
            regionUtil.addNewTowns(assumption.getCompanyDetails().getCompanyRegion(), assumption.getCompanyDetails().getCompanyTown());

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
        assumption.setUpdated(false);

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
        GMapLocation location = googleMapServices.getCoordinates(newAssumptionOfDuty.companyExactLocation());

        return createCompanyDetails(newAssumptionOfDuty, location.lng(), location.lat());
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


    @Transactional(rollbackOn = LiaisonException.class)
    @Override
    public ResponseEntity<Response<?>> updateAssumptionOfDuty(String studentId, String dutyId, CreateNewAssumptionOfDuty duty) {

        // verify that the user is a student
        studentUtil.verifyUserIsStudent(studentId);

        assumptionOfDutyRepository.findById(dutyId).ifPresent(assumptionOfDuty -> {
            try {
                // update the assumption of duty
                CompanyDetails companyDetails = updateCompanyDetails(duty, assumptionOfDuty);

                assumptionOfDuty.setDateCommenced(!(duty.dateCommenced().isEmpty()) ? duty.dateCommenced() : assumptionOfDuty.getDateCommenced());
                assumptionOfDuty.setCompanyDetails(companyDetails);
                assumptionOfDuty.setUpdated(true);
                assumptionOfDuty.setDateUpdated(LocalDateTime.now());

                assumptionOfDutyRepository.save(assumptionOfDuty);
            } catch (Exception e) {
                throw new LiaisonException(Error.ERROR_SAVING_DATA, new Throwable(Message.THE_UPDATED_DATA_CANNOT_BE_SAVED.label));
            }
        });

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.CREATED.value())
                .message("assumption of duty submitted successfully!")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private CompanyDetails updateCompanyDetails(CreateNewAssumptionOfDuty duty, AssumptionOfDuty assumptionOfDuty) {

        // Use the Google map service to find the lng and lat of the place
        AtomicReference<GMapLocation> location = new AtomicReference<>();
        if (!(duty.companyExactLocation().isEmpty())) {
            GoogleMapServices googleMapServices = new GoogleMapServices();
            location.set(googleMapServices.getCoordinates(duty.companyExactLocation()));
        }

        // set the company details
        CompanyDetails companyDetails = new CompanyDetails();

        companyDetails.setCompanyName(!(duty.companyName().isEmpty()) ? duty.companyName() : assumptionOfDuty.getCompanyDetails().getCompanyName());
        companyDetails.setCompanyPhone(!(duty.companyPhone().isEmpty()) ? duty.companyPhone() : assumptionOfDuty.getCompanyDetails().getCompanyPhone());
        companyDetails.setCompanyExactLocation(!(duty.companyExactLocation().isEmpty()) ? duty.companyExactLocation() : assumptionOfDuty.getCompanyDetails().getCompanyExactLocation());
        companyDetails.setCompanyTown(!(duty.companyTown().isEmpty()) ? duty.companyTown() : assumptionOfDuty.getCompanyDetails().getCompanyTown());
        companyDetails.setCompanyRegion(!(duty.companyRegion().isEmpty()) ? duty.companyRegion() : assumptionOfDuty.getCompanyDetails().getCompanyRegion());
        companyDetails.setCompanyAddress(!(duty.companyAddress().isEmpty()) ? duty.companyAddress() : assumptionOfDuty.getCompanyDetails().getCompanyAddress());
        companyDetails.setCompanyEmail(!(duty.companyEmail().isEmpty()) ? duty.companyEmail() : assumptionOfDuty.getCompanyDetails().getCompanyEmail());
        companyDetails.setCompanySupervisor(!(duty.companySupervisor().isEmpty()) ? duty.companySupervisor() : assumptionOfDuty.getCompanyDetails().getCompanySupervisor());
        companyDetails.setSupervisorPhone(!(duty.supervisorPhone().isEmpty()) ? duty.supervisorPhone() : assumptionOfDuty.getCompanyDetails().getSupervisorPhone());
        companyDetails.setLetterTo(!(duty.letterTo().isEmpty()) ? duty.letterTo() : assumptionOfDuty.getCompanyDetails().getLetterTo());

        // Handle coordinates
        companyDetails.setCompanyLongitude(!(duty.companyExactLocation().isEmpty()) ? location.get().lng() : assumptionOfDuty.getCompanyDetails().getCompanyLongitude());
        companyDetails.setCompanyLatitude(!(duty.companyExactLocation().isEmpty()) ? location.get().lat() : assumptionOfDuty.getCompanyDetails().getCompanyLatitude());

        System.out.println("companyDetails = " + companyDetails);

        return companyDetails;
    }
}
