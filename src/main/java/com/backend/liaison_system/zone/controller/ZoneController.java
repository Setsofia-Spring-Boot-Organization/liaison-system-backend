package com.backend.liaison_system.zone.controller;

import com.backend.liaison_system.common.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.zone.dao.AllZones;
import com.backend.liaison_system.zone.dto.NewZone;
import com.backend.liaison_system.zone.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping(path = "/{admin-id}")
    public ResponseEntity<Response<?>> createNewZone(
            @PathVariable("admin-id") String id,
            @RequestBody List<NewZone> zones,
            ConstantRequestParam param
    ) throws LiaisonException {
        return zoneService.createNewZone(id, zones, param);
    }

    @GetMapping(path = "/{admin-id}")
    public ResponseEntity<Response<List<AllZones>>> getAllZones(
            @PathVariable("admin-id") String adminId,
            ConstantRequestParam param
    ) {
        return zoneService.getAllZones(adminId, param);
    }
}
