package com.backend.liaison_system.users.student.assumption_of_duty.repository;

import com.backend.liaison_system.users.student.assumption_of_duty.entities.AssumptionOfDuty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AssumptionOfDutyRepository extends CrudRepository<AssumptionOfDuty, String>, JpaSpecificationExecutor<AssumptionOfDuty> {

}
