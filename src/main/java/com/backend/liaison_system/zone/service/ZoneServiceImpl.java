package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.common.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.zone.dao.AllZones;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        // create the new zone data
        int startOfAcademicYear = Integer.parseInt(param.startOfAcademicYear());
        int endOfAcademicYear = Integer.parseInt(param.endOfAcademicYear());

        LocalDateTime startDate = LocalDate.of(startOfAcademicYear, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(endOfAcademicYear, 12, 31).atTime(23, 59, 59, 999999999);

        List<Zone> zones1 = new ArrayList<>();
        for (NewZone zone : zones) {
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

            zone1.setLecturers(new ZoneLecturers(zone.lecturerIds()));

            zones1.add(zone1);
        }

        try {
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

        List<List<String>> lecturerIds = zones.stream().map(NewZone::lecturerIds).toList();

        for (List<String> ids : lecturerIds.stream().toList()) {
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

        List<Zone> zones = zoneSpecification.findZonesUsingZoneTypeAndAcademicDates(param);

        List<AllZones> allZones = new ArrayList<>();
        for (Zone zone : zones) {
            allZones.add(
                    new AllZones(
                            zone.getId(),
                            zone.getName(),
                            zone.getRegion(),
                            zone.getZoneLead(),
                            zone.getStartOfAcademicYear(),
                            zone.getEndOfAcademicYear(),
                            zone.getDateCreated(),
                            zone.getDateUpdated(),
                            zone.getInternshipType(),
                            zone.getLecturers(),
                            zone.getTowns(),
                            zone.getLecturers().lecturers().size()
                    )
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new Response.Builder<List<AllZones>>()
                        .status(HttpStatus.OK.value())
                        .message("zones")
                        .data(allZones)
                        .build()
        );
    }
}
