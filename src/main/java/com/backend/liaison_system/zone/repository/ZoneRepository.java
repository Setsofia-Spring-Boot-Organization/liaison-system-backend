package com.backend.liaison_system.zone.repository;

import com.backend.liaison_system.zone.entity.Zone;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ZoneRepository extends CrudRepository<Zone, String>, JpaSpecificationExecutor<Zone> {

}
