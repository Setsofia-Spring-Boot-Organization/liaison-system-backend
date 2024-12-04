package com.backend.liaison_system.zone.repository;

import com.backend.liaison_system.zone.entity.Zone;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends CrudRepository<Zone, String>, JpaSpecificationExecutor<Zone> {

    default List<Zone> findAllZones(Specification<Zone> spec) {
        return findAll(spec);
    }

    default Optional<Zone> findZoneByRegionAndTown(String r, String t) {
        Specification<Zone> zoneSpecification = (root, query, criteriaBuilder) -> {
            Predicate regionMatch = criteriaBuilder.equal(root.get("region"), r);
            Predicate townMatch = criteriaBuilder.like(root.get("towns").get("towns").as(String.class), "%" + t + "%"); //Checks membership
            return criteriaBuilder.and(regionMatch, townMatch);
        };

        return findOne(zoneSpecification);
    }

    default Optional<Zone> findByLecturerId(String id) {
        Specification<Zone> specification = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("lecturers").get("lecturers").as(String.class), id);

        return findOne(specification);
    }

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS zone", nativeQuery = true)
    void dropZoneTable();
}
