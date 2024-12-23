package com.backend.liaison_system.zone.specification;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.util.UAcademicYear;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.repository.ZoneRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ZoneSpecification {

    private final ZoneRepository zoneRepository;

    public ZoneSpecification(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> findZonesUsingZoneTypeAndAcademicDates(ConstantRequestParam param) {
        LocalDateTime startDate = UAcademicYear.startOfAcademicYear(param.startOfAcademicYear());
        LocalDateTime endDate = UAcademicYear.endOfAcademicYear(param.endOfAcademicYear());

        Specification<Zone> zoneSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("startOfAcademicYear"), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get("endOfAcademicYear"), endDate),
                criteriaBuilder.equal(root.get("semester"), param.semester()),
                criteriaBuilder.equal(root.get("internshipType"), (param.internship())? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT)
        );

        return zoneRepository.findAllZones(zoneSpecification);
    }
}
