package com.backend.liaison_system.region.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.region.dto.NewRegion;
import com.backend.liaison_system.region.entities.Region;
import com.backend.liaison_system.region.repository.RegionRepository;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final AdminUtil adminUtil;
    private final RegionRepository regionRepository;

    @Override
    @Transactional(rollbackOn = {RuntimeException.class, LiaisonException.class})
    public ResponseEntity<Response<?>> creatNewRegion(String id, List<NewRegion> newRegionsRequest) {
        adminUtil.verifyUserIsAdmin(id);

        // get all the regions
        List<Region> existingRegions = (List<Region>) regionRepository.findAll();

        List<Region> newRegions = new ArrayList<>();
        for (NewRegion newRegion : newRegionsRequest) {
            for (Region region : existingRegions) {

                // when a regions already exists, update the towns in it
                if (region.getRegion().equalsIgnoreCase(newRegion.region())) {

                    newRegion.towns().removeAll(region.getTown().towns());
                    region.getTown().towns().addAll(newRegion.towns());
                    region.setDateUpdated(LocalDateTime.now());

                    newRegions.add(region);
                } else {

                    // create a new region
                    Region newRegion1 = new Region();

                    newRegion1.setRegion(newRegion.region());
                    newRegion1.setDateCreated(LocalDateTime.now());
                    newRegion1.setDateUpdated(LocalDateTime.now());
                    newRegion1.setTown(region.getTown());

                    newRegions.add(newRegion1);
                }
            }
        }

        try {
            List<Region> savedRegions = (List<Region>) regionRepository.saveAll(newRegions);

            Response<List<Region>> response = Response.<List<Region>>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("created/updated regions")
                    .data(savedRegions)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new LiaisonException(Error.ERROR_SAVING_DATA);
        }
    }
}
