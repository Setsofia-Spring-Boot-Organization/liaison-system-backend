package com.backend.liaison_system.zone.repository;

import com.backend.liaison_system.zone.entity.Zone;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ZoneRepository extends CrudRepository<Zone, String>, JpaSpecificationExecutor<Zone> {

    default List<Zone> findAllZones(Specification<Zone> spec) {
        return findAll(spec);
    }

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS zone", nativeQuery = true)
    void dropZoneTable();
}
