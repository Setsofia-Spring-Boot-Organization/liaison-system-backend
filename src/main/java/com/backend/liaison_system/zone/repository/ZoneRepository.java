package com.backend.liaison_system.zone.repository;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.util.UAcademicYear;
import com.backend.liaison_system.zone.entity.Zone;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends CrudRepository<Zone, String>, JpaSpecificationExecutor<Zone> {

    default List<Zone> findAllZones(Specification<Zone> spec) {
        return findAll(spec);
    }

    default Optional<Zone> findZoneByRegionAndTown(String r, String t, ConstantRequestParam param) {
        LocalDateTime startDate = UAcademicYear.startOfAcademicYear(param.startOfAcademicYear());
        LocalDateTime endDate = UAcademicYear.endOfAcademicYear(param.endOfAcademicYear());

        Specification<Zone> zoneSpecification = (root, query, criteriaBuilder) -> {
            Predicate regionMatch = criteriaBuilder.equal(root.get("region"), r);
            Predicate townMatch = criteriaBuilder.like(root.get("towns").get("towns").as(String.class), "%" + t + "%"); //Checks membership
            Predicate academicYearMatch = criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startOfAcademicYear"), startDate),
                    criteriaBuilder.lessThanOrEqualTo(root.get("endOfAcademicYear"), endDate),
                    criteriaBuilder.equal(root.get("semester"), param.semester()),
                    criteriaBuilder.equal(root.get("internshipType"), (param.internship())? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT)
            );
            return criteriaBuilder.and(regionMatch, townMatch, academicYearMatch);
        };

        return findOne(zoneSpecification);
    }

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS zone", nativeQuery = true)
    void dropZoneTable();
}
