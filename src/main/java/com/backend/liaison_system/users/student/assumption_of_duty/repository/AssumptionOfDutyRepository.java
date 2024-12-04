package com.backend.liaison_system.users.student.assumption_of_duty.repository;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AssumptionOfDutyRepository extends CrudRepository<AssumptionOfDuty, String>, JpaSpecificationExecutor<AssumptionOfDuty> {

    Optional<AssumptionOfDuty> findAssumptionOfDutyByStudentId(String id);

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS assumption_of_duty", nativeQuery = true)
    void dropZoneTable();
}
