package com.backend.liaison_system.zone.service;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.zone.dao.AllZones;
import com.backend.liaison_system.zone.dto.NewZone;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ZoneService {

    /**
     * This method creates a new zone and associates it with the admin user identified by the provided ID.
     * It validates the admin's authorization before proceeding with the zone creation.
     *
     * @param id the ID of the admin user making the request
     * @param zones the NewZone object containing the details of the new zone to be created
     * @return a ResponseEntity containing a Response object with the status of the zone creation operation
     */
    ResponseEntity<Response<?>> createNewZone(String id, List<NewZone> zones, ConstantRequestParam param);

    ResponseEntity<Response<List<AllZones>>> getAllZones(String adminId, ConstantRequestParam param);

    ResponseEntity<Response<?>> updateZone(String adminId, String zoneId, NewZone zone);

    ResponseEntity<Response<?>> getSingleZone(String adminId, String zoneId);
}
