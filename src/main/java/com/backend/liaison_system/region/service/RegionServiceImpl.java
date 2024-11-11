package com.backend.liaison_system.region.service;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.region.dto.NewRegion;
import com.backend.liaison_system.region.entities.Region;
import com.backend.liaison_system.region.entities.Town;
import com.backend.liaison_system.region.repository.RegionRepository;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final AdminUtil adminUtil;
    private final RegionRepository regionRepository;

    @Override
    @Transactional(rollbackOn = {RuntimeException.class, LiaisonException.class})
    public ResponseEntity<Response<?>> creatNewRegion(String id, List<NewRegion> newRegions) {

        // verify the user performing this action
        adminUtil.verifyUserIsAdmin(id);

        // get all the regions
        List<Region> existingRegions = (List<Region>) regionRepository.findAll();

        List<Region> regions = (existingRegions.isEmpty()) ?
                createNewRegion(newRegions) :
                updateExistingRegion(newRegions, existingRegions);

        try {
            List<Region> savedRegions = (List<Region>) regionRepository.saveAll(regions);

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

    private List<Region> createNewRegion(List<NewRegion> newRegions) {

        List<Region> regions = new ArrayList<>();
        for (NewRegion newRegion : newRegions) {
            Region reg = new Region();

            reg.setRegion(newRegion.region());
            reg.setDateCreated(LocalDateTime.now());
            reg.setDateUpdated(LocalDateTime.now());
            reg.setTown(new Town(newRegion.towns()));

            regions.add(reg);
        }

        return regions;
    }

    private List<Region> updateExistingRegion(List<NewRegion> newRegions, List<Region> existingRegions) {

        Set<Region> regions = new HashSet<>();
        Set<NewRegion> newRegs = new HashSet<>();

        for (NewRegion newRegion : newRegions) {
            if (existingRegions.stream().anyMatch(region -> region.getRegion().equals(newRegion.region()))) {

                existingRegions.forEach(region -> {
                    if (region.getRegion().equals(newRegion.region())) {
                        region.getTown().towns().addAll(newRegion.towns());
                        regions.add(region);
                    }
                });

            } else {
                newRegs.add(newRegion);
            }
        }
        List<Region> createNewRegions = createNewRegion(newRegs.stream().toList());
        regions.addAll(createNewRegions);

        return regions.stream().toList();
    }
}
