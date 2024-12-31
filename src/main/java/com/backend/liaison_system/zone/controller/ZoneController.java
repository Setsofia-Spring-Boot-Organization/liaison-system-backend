package com.backend.liaison_system.zone.controller;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.zone.dao.AllZones;
import com.backend.liaison_system.zone.dto.NewZone;
import com.backend.liaison_system.zone.service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping(path = "/{admin-id}")
    public ResponseEntity<Response<?>> createNewZone(
            @PathVariable("admin-id") String id,
            @RequestBody List<NewZone> zones,
            ConstantRequestParam param
    ) throws LiaisonException {
        return zoneService.createNewZone(id, zones, param);
    }


    @GetMapping(path = "/{admin-id}/zone/{zone-id}")
    public ResponseEntity<Response<?>> getSingleZone(
            @PathVariable("admin-id") String adminId,
            @PathVariable("zone-id") String zoneId
    ) {
        return zoneService.getSingleZone(adminId, zoneId);
    }


    @GetMapping(path = "/{admin-id}")
    public ResponseEntity<Response<List<AllZones>>> getAllZones(
            @PathVariable("admin-id") String adminId,
            ConstantRequestParam param
    ) {
        return zoneService.getAllZones(adminId, param);
    }



    @PutMapping(path = "/{admin-id}/update/{zone-id}")
    public ResponseEntity<Response<?>> updateZone(
            @PathVariable("admin-id") String adminId,
            @PathVariable("zone-id") String zoneId,
            @RequestBody NewZone zone
    ) {
        return zoneService.updateZone(adminId, zoneId, zone);
    }
}
