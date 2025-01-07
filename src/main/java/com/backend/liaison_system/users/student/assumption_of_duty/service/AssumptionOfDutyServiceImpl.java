package com.backend.liaison_system.users.student.assumption_of_duty.service;

import com.backend.liaison_system.enums.Status;
import com.backend.liaison_system.google_maps.responses.GMapLocation;
import com.backend.liaison_system.region.util.RegionUtil;
import com.backend.liaison_system.users.admin.dao.UpdatedAssumptionOfDutyData;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.CompanyDetails;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.OldAssumptionOfDuty;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.AssumptionOfDutyRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.repository.OldAssumptionOfDutyRepository;
import com.backend.liaison_system.users.student.assumption_of_duty.requests.CreateNewAssumptionOfDuty;
import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import com.backend.liaison_system.google_maps.GoogleMapServices;
import com.backend.liaison_system.users.student.assumption_of_duty.responses.Attachments;
import com.backend.liaison_system.users.student.assumption_of_duty.util.AssumptionOfDutyUtil;
import com.backend.liaison_system.users.student.util.StudentUtil;
import com.backend.liaison_system.util.UAcademicYear;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.backend.liaison_system.exception.Error.ERROR_SAVING_DATA;

@Service
public class AssumptionOfDutyServiceImpl implements AssumptionOfDutyService {

    private final StudentRepository studentRepository;
    private final AssumptionOfDutyRepository assumptionOfDutyRepository;
    private final RegionUtil regionUtil;
    private final StudentUtil studentUtil;
    private final OldAssumptionOfDutyRepository oldAssumptionOfDutyRepository;
    private final AdminUtil adminUtil;
    private final AssumptionOfDutyUtil dutyUtil;

    public AssumptionOfDutyServiceImpl(StudentRepository studentRepository, AssumptionOfDutyRepository assumptionOfDutyRepository, RegionUtil regionUtil, StudentUtil studentUtil, OldAssumptionOfDutyRepository oldAssumptionOfDutyRepository, AdminUtil adminUtil, AssumptionOfDutyUtil dutyUtil) {
        this.studentRepository = studentRepository;
        this.assumptionOfDutyRepository = assumptionOfDutyRepository;
        this.regionUtil = regionUtil;
        this.studentUtil = studentUtil;
        this.oldAssumptionOfDutyRepository = oldAssumptionOfDutyRepository;
        this.adminUtil = adminUtil;
        this.dutyUtil = dutyUtil;
    }


    @Override
    public ResponseEntity<Response<?>> getStudentAssumptionOfDuties(String studentId, ConstantRequestParam param) {

        // verify that the user is a students
        studentUtil.verifyUserIsStudent(studentId);

        //filtering the data by the student's id
        List<AssumptionOfDuty> duties =  assumptionOfDutyRepository.findAllDuties(param).stream().filter(assumptionOfDuty -> assumptionOfDuty.getStudentId().equals(studentId)).toList();

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
        assumption.setStartOfAcademicYear(UAcademicYear.startOfAcademicYear(param.startOfAcademicYear()));
        assumption.setEndOfAcademicYear(UAcademicYear.endOfAcademicYear(param.endOfAcademicYear()));

        assumption.setSemester(param.semester());

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
                // create the old version of the assumption of duty
                createOldAssumptionOfDuty(assumptionOfDuty);

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
                .message("assumption of duty updated successfully!")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private static CompanyDetails getOldCompanyDetails(AssumptionOfDuty assumptionOfDuty) {
        CompanyDetails oldCompanyDetails = new CompanyDetails();
        oldCompanyDetails.setCompanyName(assumptionOfDuty.getCompanyDetails().getCompanyName());
        oldCompanyDetails.setCompanyPhone(assumptionOfDuty.getCompanyDetails().getCompanyPhone());
        oldCompanyDetails.setCompanyExactLocation(assumptionOfDuty.getCompanyDetails().getCompanyExactLocation());
        oldCompanyDetails.setCompanyTown(assumptionOfDuty.getCompanyDetails().getCompanyTown());
        oldCompanyDetails.setCompanyRegion(assumptionOfDuty.getCompanyDetails().getCompanyRegion());
        oldCompanyDetails.setCompanyAddress(assumptionOfDuty.getCompanyDetails().getCompanyAddress());
        oldCompanyDetails.setCompanyEmail(assumptionOfDuty.getCompanyDetails().getCompanyEmail());
        oldCompanyDetails.setCompanySupervisor(assumptionOfDuty.getCompanyDetails().getCompanySupervisor());
        oldCompanyDetails.setSupervisorPhone(assumptionOfDuty.getCompanyDetails().getSupervisorPhone());
        oldCompanyDetails.setLetterTo(assumptionOfDuty.getCompanyDetails().getLetterTo());
        oldCompanyDetails.setCompanyLatitude(assumptionOfDuty.getCompanyDetails().getCompanyLatitude());
        oldCompanyDetails.setCompanyLongitude(assumptionOfDuty.getCompanyDetails().getCompanyLongitude());
        return oldCompanyDetails;
    }


    @Transactional(rollbackOn = LiaisonException.class)
    protected void createOldAssumptionOfDuty(AssumptionOfDuty assumptionOfDuty) {
        OldAssumptionOfDuty oldAssumptionOfDuty = new OldAssumptionOfDuty();

        oldAssumptionOfDuty.setStudentId(oldAssumptionOfDuty.getStudentId());
        oldAssumptionOfDuty.setUpdatedAssumptionOfDutyId(assumptionOfDuty.getId());

        oldAssumptionOfDuty.setDateCreated(assumptionOfDuty.getDateCreated());
        oldAssumptionOfDuty.setDateUpdated(assumptionOfDuty.getDateUpdated());

        oldAssumptionOfDuty.setDateCommenced(assumptionOfDuty.getDateCommenced());

        oldAssumptionOfDuty.setStartOfAcademicYear(assumptionOfDuty.getStartOfAcademicYear());
        oldAssumptionOfDuty.setEndOfAcademicYear(assumptionOfDuty.getEndOfAcademicYear());

        oldAssumptionOfDuty.setSemester(assumptionOfDuty.getSemester());

        oldAssumptionOfDuty.setInternship(assumptionOfDuty.isInternship());
        oldAssumptionOfDuty.setUpdated(assumptionOfDuty.isUpdated());

        CompanyDetails oldCompanyDetails = getOldCompanyDetails(assumptionOfDuty);
        oldAssumptionOfDuty.setCompanyDetails(oldCompanyDetails);

        try {
            oldAssumptionOfDutyRepository.save(oldAssumptionOfDuty);
        } catch (Exception e) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA, new Throwable(Message.THE_UPDATED_DATA_CANNOT_BE_SAVED.label));
        }
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

        return companyDetails;
    }


    @Override
    public ResponseEntity<Response<?>> getUpdatedDutyDetails(String adminId, String assumptionId) {
        // verify that the user is and admin
        adminUtil.verifyUserIsAdmin(adminId);

        AtomicReference<UpdatedAssumptionOfDutyData> updatedAssumptionOfDutyData = new AtomicReference<>();
        Set<OldAssumptionOfDuty> oldAssumptionOfDuties = new HashSet<>();

        assumptionOfDutyRepository.findById(assumptionId).ifPresent(assumptionOfDuty ->
                {
                    oldAssumptionOfDuties.addAll(oldAssumptionOfDutyRepository.findByUpdatedAssumptionOfDutyIdOrderByDateCreatedDesc(assumptionOfDuty.getId()));
                    updatedAssumptionOfDutyData.set(new UpdatedAssumptionOfDutyData(oldAssumptionOfDuties, assumptionOfDuty));
                }
        );

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("updated assumption of duty")
                .data(updatedAssumptionOfDutyData)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Override
    public ResponseEntity<Response<?>> getAllDuties(String adminId, ConstantRequestParam param, int page, int size) {
        // Verify that the user performing the action is an admin
        adminUtil.verifyUserIsAdmin(adminId);

        List<Attachments> attachments = new ArrayList<>();
        assumptionOfDutyRepository.findAllDutiesWithPagination(param, page, size).forEach(assumption -> studentRepository.findById(assumption.getStudentId()).ifPresent(
                student -> attachments.add(
                        new Attachments(
                                student.getId(),
                                student.getStudentFirstName() + student.getStudentLastName() + student.getStudentOtherName(),
                                student.getStudentDepartment(),
                                student.getStudentFaculty(),
                                UAcademicYear.stringDateToLocalDateTime(assumption.getDateCommenced()),
                                student.getEndDate(),
                                student.isSupervised()? Status.COMPLETED : student.isAssumeDuty()? Status.IN_PROGRESS : Status.NOT_STARTED
                        )
                )
        ));

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("attachments")
                .data(attachments)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Override
    public ResponseEntity<Response<?>> uploadAssumptionOfDuties(String adminId, ConstantRequestParam param, MultipartFile file) {
        //Verify that the user performing this action is an admin
        adminUtil.verifyUserIsAdmin(adminId);

        try {

            System.out.println("STAGE ONE - 1");

            //create an arrayList of students
            List<AssumptionOfDuty> assumptionOfDuties = new ArrayList<>();

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

            System.out.println("STAGE ONE - 2");

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("STAGE ONE - 2.1.0");

            //For each row in the sheet extract the student details
            for(Row row : sheet) {

                System.out.println("STAGE ONE - 2.1.1");

                if(row.getRowNum() == 0) continue;
                AssumptionOfDuty currentDuty = dutyUtil.buildDutyFromExcel(row, param);

                System.out.println("currentDuty = " + currentDuty);

                System.out.println("STAGE ONE - 2.1");

                //Ensure assumption does not already exist
                assumptionOfDutyRepository.findById(currentDuty.getId()).orElseGet(() -> {
                    currentDuty.setInternship(!param.internship());
                    assumptionOfDuties.add(currentDuty);
                    return null;}
                );
            }

            System.out.println("STAGE ONE - 3");

//            assumptionOfDutyRepository.saveAll(assumptionOfDuties);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response.Builder<>()
                    .status(HttpStatus.CREATED.value())
                    .message("assumption of duties created successfully")
                    .build()
            );
        } catch (Exception e) {
            throw new LiaisonException(ERROR_SAVING_DATA);
        }
    }
}
