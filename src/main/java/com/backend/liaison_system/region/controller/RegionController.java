package com.backend.liaison_system.region.controller;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.region.dto.NewRegion;
import com.backend.liaison_system.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping(path = "/{admin-id}")
    public ResponseEntity<Response<?>> createNewRegion(
            @PathVariable("admin-id") String id,
            @RequestBody List<NewRegion> regions
    ) {
        return regionService.creatNewRegion(id, regions);
    }
}
