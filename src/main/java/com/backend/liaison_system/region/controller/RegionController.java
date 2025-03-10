package com.backend.liaison_system.region.controller;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.region.dao.Regions;
import com.backend.liaison_system.region.dto.NewRegion;
import com.backend.liaison_system.region.entities.Region;
import com.backend.liaison_system.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "liaison/api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping(path = "/{admin-id}")
    public ResponseEntity<Response<?>> createNewRegion(
            @PathVariable("admin-id") String id,
            @RequestBody List<NewRegion> regions
    ) {
        return regionService.creatNewRegion(id, regions);
    }



    @GetMapping(path = "/{admin-id}")
    public ResponseEntity<Response<Regions>> getAllRegions(
            @PathVariable("admin-id") String id
    ) {
        return regionService.getAllRegions(id);
    }
}
