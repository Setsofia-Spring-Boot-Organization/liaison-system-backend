package com.backend.liaison_system.region.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.region.dto.NewRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RegionService {
    ResponseEntity<Response<?>> creatNewRegion(String id, List<NewRegion> regions);

}
