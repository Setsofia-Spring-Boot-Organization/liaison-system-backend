package com.backend.liaison_system.region.util;

import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.region.repository.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class RegionUtil {

    private final RegionRepository regionRepository;

    public RegionUtil(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Transactional(rollbackOn = LiaisonException.class)
    public void addNewTowns(String regionName, String town) {
        regionRepository.findAll().forEach(region -> {
            if (region.getRegion().equals(regionName)) {
                region.getTown().towns().add(town);
                regionRepository.save(region);
            }
        });
    }
}
