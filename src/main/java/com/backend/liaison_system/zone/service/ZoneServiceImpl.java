package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.util.UAcademicYear;
import com.backend.liaison_system.zone.dao.AllZones;
import com.backend.liaison_system.zone.dao.LecturerData;
import com.backend.liaison_system.zone.dao.ZoneData;
import com.backend.liaison_system.zone.dto.NewZone;
import com.backend.liaison_system.zone.entity.Towns;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.entity.ZoneLecturers;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import com.backend.liaison_system.zone.specification.ZoneSpecification;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ZoneServiceImpl implements ZoneService{

    private final AdminUtil adminUtil;
    private final LecturerRepository lecturerRepository;
    private final ZoneRepository zoneRepository;
    private final ZoneSpecification zoneSpecification;

    public ZoneServiceImpl(AdminUtil adminUtil, LecturerRepository lecturerRepository, ZoneRepository zoneRepository, ZoneSpecification zoneSpecification) {
        this.adminUtil = adminUtil;
        this.lecturerRepository = lecturerRepository;
        this.zoneRepository = zoneRepository;
        this.zoneSpecification = zoneSpecification;
    }


    @Override
    public ResponseEntity<Response<?>> getSingleZone(String adminId, String zoneId) {
        // verify that the user is an admin
        adminUtil.verifyUserIsAdmin(adminId);

        Set<Lecturer> lecturers = new HashSet<>();
        AtomicReference<ZoneData> zoneData = new AtomicReference<>();
        zoneRepository.findById(zoneId).ifPresent(
                zone -> {
                    Lecturer lecturer = getZoneLeadData(zone.getZoneLead());
                    zone.getLecturers().lecturers().forEach(lecturerId -> lecturerRepository.findById(lecturerId).ifPresent(lecturers::add));

                    // set the zone data
                    zoneData.set(new ZoneData(
                            zone.getName(),
                            zone.getRegion(),
                            zone.getTowns().towns(),
                            lecturer.getOtherName() == null? lecturer.getFirstName() + " " + lecturer.getLastName() : lecturer.getFirstName() + " " + lecturer.getOtherName() + " " + lecturer.getLastName(),
                            UAcademicYear.sanitizeLocalDateTimeToAcademicYear(zone.getStartOfAcademicYear()),
                            UAcademicYear.sanitizeLocalDateTimeToAcademicYear(zone.getEndOfAcademicYear()),
                            zone.getSemester(),
                            new LecturerData(lecturers, lecturers.size())
                    ));
                }
        );

        Response<?> response = new Response.Builder<>()
                .status(HttpStatus.OK.value())
                .message("zone details")
                .data(zoneData)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * This method retrieves the {@link Lecturer} data for a specific zone lead based on their ID.
     * It fetches the lecturer from the repository if the ID is found.
     *
     * @param id the ID of the zone lead
     * @return the {@link Lecturer} object representing the zone lead, or {@code null} if no lecturer is found
     */
    private Lecturer getZoneLeadData(String id) {
        AtomicReference<Lecturer> lecturer = new AtomicReference<>();

        lecturerRepository.findById(id).ifPresent(lecturer::set);
        return lecturer.get();
    }



    @Transactional(rollbackOn = { Exception.class, LiaisonException.class })
    @Override
    public ResponseEntity<Response<?>> createNewZone(String id, List<NewZone> zones, ConstantRequestParam param) {
        for (NewZone zone : zones) {
            if (zone.zoneLead() == null || zone.zoneLead().isEmpty()) throw new LiaisonException(Error.REQUIRED_FIELDS_ARE_EMPTY, new Throwable(Message.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + "[zoneLead]"));
        }

        // verify that the user sending the request is an admin
        adminUtil.verifyUserIsAdmin(id);

        // sanitize the ids, making sure that they exist
        sanitizeTheLecturerIds(zones);

        LocalDateTime startDate = UAcademicYear.startOfAcademicYear(param.startOfAcademicYear());
        LocalDateTime endDate = UAcademicYear.endOfAcademicYear(param.endOfAcademicYear());

        List<Zone> zones1 = new ArrayList<>();
        List<String> zoneLeadIds = new ArrayList<>();
        for (NewZone zone : zones) {

            // add the id of the zone lead
            zoneLeadIds.add(zone.zoneLead());

            // create the zone
            Zone zone1 = new Zone();
            zone1.setName(zone.name());
            zone1.setRegion(zone.region());
            zone1.setTowns(new Towns(zone.towns()));
            zone1.setZoneLead(zone.zoneLead());

            zone1.setDateCreated(LocalDateTime.now());
            zone1.setDateUpdated(LocalDateTime.now());

            zone1.setInternshipType((param.internship()) ? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT);
            zone1.setStartOfAcademicYear(startDate);
            zone1.setEndOfAcademicYear(endDate);

            zone1.setSemester(param.semester());

            zone1.setLecturers(new ZoneLecturers(zone.lecturerIds()));

            zones1.add(zone1);
        }

        // update all the lecturers to zone leads
        List<Lecturer> zoneLeads = new ArrayList<>();
        zoneLeadIds.forEach(leadId -> lecturerRepository.findById(leadId).ifPresent(lecturer -> {
            lecturer.setZoneLead(true);
            zoneLeads.add(lecturer);
        }));

        try {
            lecturerRepository.saveAll(zoneLeads);
            zoneRepository.saveAll(zones1);
        } catch (Exception exception) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response.Builder<>()
                        .status(HttpStatus.CREATED.value())
                        .message("New zone(s) created successfully")
                        .build()
        );
    }

    /**
     * This method sanitizes the lecturer IDs in the provided {@code zone} by checking if any of the IDs
     * do not exist in the system. If invalid IDs are found, it throws a {@link LiaisonException}.
     *
     * @param zones The {@link NewZone} object containing the lecturer IDs to be validated.
     * @throws LiaisonException If one or more lecturer IDs do not exist in the system.
     */
    private void sanitizeTheLecturerIds(List<NewZone> zones) throws LiaisonException {
        List<String> invalidIds = new ArrayList<>();

        List<Set<String>> lecturerIds = zones.stream().map(NewZone::lecturerIds).toList();

        for (Set<String> ids : lecturerIds.stream().toList()) {
            for (String id : ids) {
                boolean result = lecturerRepository.findById(id).isEmpty();
                if (result) invalidIds.add(id);
            }
        }

        if (!invalidIds.isEmpty())
            throw new LiaisonException(Error.INVALID_USER_IDS, new Throwable(Message.THE_FOLLOWING_IDS_DO_NOT_EXIST.label + " " + invalidIds));
    }



    @Override
    public ResponseEntity<Response<List<AllZones>>> getAllZones(String adminId, ConstantRequestParam param) {
        adminUtil.verifyUserIsAdmin(adminId); // verify that the user is an admin

        List<AllZones> allZones = new ArrayList<>();
        zoneSpecification.findZonesUsingZoneTypeAndAcademicDates(param).forEach(zone -> lecturerRepository.findById(zone.getZoneLead()).ifPresent(
                lecturer -> allZones.add(
                        new AllZones(
                                zone.getId(),
                                zone.getName(),
                                zone.getRegion(),
                                lecturer.getOtherName() == null? lecturer.getFirstName() +" "+ lecturer.getLastName() : lecturer.getFirstName() +" "+ lecturer.getOtherName() +" "+ lecturer.getLastName(),
                                zone.getStartOfAcademicYear(),
                                zone.getEndOfAcademicYear(),
                                zone.getDateCreated(),
                                zone.getDateUpdated(),
                                zone.getInternshipType(),
                                zone.getLecturers(),
                                zone.getTowns(),
                                zone.getLecturers().lecturers().size()
                        )
                )
        ));

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<List<AllZones>>()
                        .status(HttpStatus.OK.value())
                        .message("zones")
                        .data(allZones)
                        .build()
        );
    }



    @Override
    public ResponseEntity<Response<?>> updateZone(String adminId, String zoneId, NewZone zone) {

        // Verify that user is an admin
        adminUtil.verifyUserIsAdmin(adminId);

        zoneRepository.findById(zoneId).ifPresentOrElse(
                oldZone -> {
                    oldZone.setName(zone.name().isEmpty() || zone.name().isBlank()? oldZone.getName() : zone.name());
                    oldZone.setRegion(zone.region().isEmpty() || zone.region().isBlank()? oldZone.getRegion() : zone.region());
                    oldZone.setZoneLead(zone.zoneLead().isEmpty() || zone.zoneLead().isBlank()? oldZone.getZoneLead() : zone.zoneLead());
                    oldZone.getLecturers().lecturers().addAll(!zone.lecturerIds().isEmpty()? zone.lecturerIds() : oldZone.getLecturers().lecturers());
                    oldZone.getTowns().towns().addAll(zone.towns().isEmpty()? oldZone.getTowns().towns() : zone.towns());
                    oldZone.setDateUpdated(LocalDateTime.now());

                    zoneRepository.save(oldZone);

                    System.out.println("\nZone updated = \n");
                },
                () -> {throw new LiaisonException(Error.INVALID_ZONE_ID, new Throwable(Message.THE_REQUESTED_ZONE_DOES_NOT_EXIST.label));}
        );

        Response<String> response = new Response.Builder<String>()
                .status(HttpStatus.CREATED.value())
                .message("zone updated successfully!")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
