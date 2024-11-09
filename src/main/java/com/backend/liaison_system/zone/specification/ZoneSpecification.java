package com.backend.liaison_system.zone.specification;

import com.backend.liaison_system.common.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ZoneSpecification {

    private final ZoneRepository zoneRepository;

    public ZoneSpecification(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> findZonesUsingZoneTypeAndAcademicDates(ConstantRequestParam param) {
        int startOfAcademicYear = Integer.parseInt(param.startOfAcademicYear());
        int endOfAcademicYear = Integer.parseInt(param.endOfAcademicYear());

        LocalDateTime startDate = LocalDate.of(startOfAcademicYear, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(endOfAcademicYear, 12, 31).atTime(23, 59, 59, 999999999);

        Specification<Zone> zoneSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("startOfAcademicYear"), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get("endOfAcademicYear"), endDate),
                criteriaBuilder.equal(root.get("internshipType"), (param.internship())? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT)
        );

        return zoneRepository.findAllZones(zoneSpecification);
    }
}
