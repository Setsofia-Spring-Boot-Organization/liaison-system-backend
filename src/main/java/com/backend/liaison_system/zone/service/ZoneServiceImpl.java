package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.zone.dto.NewZone;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.entity.ZoneLecturers;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService{

    private final AdminUtil adminUtil;
    private final LecturerRepository lecturerRepository;
    private final ZoneRepository zoneRepository;

    @Transactional(rollbackOn = { Exception.class, LiaisonException.class })
    @Override
    public ResponseEntity<Response<?>> createNewZone(String id, NewZone newZone) {
        if (newZone.zoneLead() == null || newZone.zoneLead().isEmpty()) throw new LiaisonException(Error.REQUIRED_FIELDS_ARE_EMPTY, new Throwable(Message.THE_FOLLOWING_FIELDS_ARE_EMPTY.label + "[zoneLead]"));

        // verify that the user sending the request is an admin
        adminUtil.verifyUserIsAdmin(id);

        // sanitize the ids, making sure that they exist
        sanitizeTheLecturerIds(newZone);

        // create the new zone data
        Zone zone = new Zone();
        zone.setName(newZone.name());
        zone.setRegion(newZone.region());
        zone.setZoneLead(newZone.zoneLead());
        zone.setLecturers(new ZoneLecturers(newZone.lecturerIds()));

        try {
            zoneRepository.save(zone);
        } catch (Exception exception) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("New zone created successfully")
                        .build()
        );
    }

    /**
     * This method sanitizes the lecturer IDs in the provided {@code zone} by checking if any of the IDs
     * do not exist in the system. If invalid IDs are found, it throws a {@link LiaisonException}.
     *
     * @param zone The {@link NewZone} object containing the lecturer IDs to be validated.
     * @throws LiaisonException If one or more lecturer IDs do not exist in the system.
     */
    private void sanitizeTheLecturerIds(NewZone zone) throws LiaisonException {
        List<String> invalidIds = new ArrayList<>();

        for (String id : zone.lecturerIds()) {
            boolean result = lecturerRepository.findById(id).isEmpty();
            if (result) invalidIds.add(id);
        }

        if (!invalidIds.isEmpty())
            throw new LiaisonException(Error.INVALID_USER_IDS, new Throwable(Message.THE_FOLLOWING_IDS_DO_NOT_EXIST.label + " " + invalidIds));
    }
}
