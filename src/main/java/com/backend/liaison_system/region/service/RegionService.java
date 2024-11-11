package com.backend.liaison_system.region.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.region.dto.NewRegion;
import com.backend.liaison_system.region.entities.Region;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RegionService {
    ResponseEntity<Response<?>> creatNewRegion(String id, List<NewRegion> regions);

    ResponseEntity<Response<List<Region>>> getAllRegions(String id);
}
