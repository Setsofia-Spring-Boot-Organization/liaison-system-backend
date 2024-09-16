package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.zone.dto.NewZone;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ZoneServiceImpl implements ZoneService{

    @Override
    public ResponseEntity<Response<?>> createNewZone(String id, NewZone zone) {


        return null;
    }
}
