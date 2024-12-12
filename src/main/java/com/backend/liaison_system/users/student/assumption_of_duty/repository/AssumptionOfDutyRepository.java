package com.backend.liaison_system.users.student.assumption_of_duty.repository;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import com.backend.liaison_system.util.UAcademicYear;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssumptionOfDutyRepository extends CrudRepository<AssumptionOfDuty, String>, JpaSpecificationExecutor<AssumptionOfDuty> {

    default Optional<AssumptionOfDuty> findAssumptionOfDutyByStudentId(String id) {
        return findOne((root, query, criteriaBuilder) -> {
            // Create a subquery to find the maximum dateCreated
            assert query != null;
            Subquery<LocalDateTime> subquery = query.subquery(LocalDateTime.class);
            Root<AssumptionOfDuty> subRoot = subquery.from(AssumptionOfDuty.class);
            subquery.select(criteriaBuilder.greatest(subRoot.get("dateCreated").as(LocalDateTime.class)))
                    .where(criteriaBuilder.equal(subRoot.get("studentId"), id));

            // Main query: Find the entity where dateCreated matches the maximum
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("studentId"), id),
                    criteriaBuilder.equal(root.get("dateCreated"), subquery)
            );
        });
    }


    @Override
    List<AssumptionOfDuty> findAll();

    default List<AssumptionOfDuty> findUpdatedDuties(ConstantRequestParam param, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime startOfYear = UAcademicYear.startOfAcademicYear(param.startOfAcademicYear());
        LocalDateTime endOfYear = UAcademicYear.endOfAcademicYear(param.endOfAcademicYear());

        Specification<AssumptionOfDuty> specification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("endOfAcademicYear"), startOfYear),
                criteriaBuilder.lessThanOrEqualTo(root.get("endOfAcademicYear"), endOfYear),
                criteriaBuilder.equal(root.get("isInternship"), param.internship()),
                criteriaBuilder.isTrue(root.get("isUpdated"))
        );

        return findAll(specification, pageable).getContent();
    }

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS assumption_of_duty", nativeQuery = true)
    void dropAssumptionOfDutyTable();
}
