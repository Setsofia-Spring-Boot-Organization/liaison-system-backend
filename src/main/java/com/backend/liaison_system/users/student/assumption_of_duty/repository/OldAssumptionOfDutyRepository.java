package com.backend.liaison_system.users.student.assumption_of_duty.repository;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.OldAssumptionOfDuty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OldAssumptionOfDutyRepository extends CrudRepository<OldAssumptionOfDuty, String> {
    List<OldAssumptionOfDuty> findByUpdatedAssumptionOfDutyIdOrderByDateCreatedDesc(String assumptionId);

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS old_assumption_of_duty", nativeQuery = true)
    void dropOldAssumptionOfDutyTable();
}
