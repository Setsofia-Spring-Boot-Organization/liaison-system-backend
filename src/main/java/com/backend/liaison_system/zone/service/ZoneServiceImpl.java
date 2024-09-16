package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.zone.dto.NewZone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService{

    private final AdminUtil adminUtil;
    private final LecturerRepository lecturerRepository;

    @Override
    public ResponseEntity<Response<?>> createNewZone(String id, NewZone zone) {

        // verify that the user sending the request is an admin
        adminUtil.verifyUserIsAdmin(id);

        // sanitize the ids, making sure that they exist
        sanitizeTheLecturerIds(zone);

        return null;
    }

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
