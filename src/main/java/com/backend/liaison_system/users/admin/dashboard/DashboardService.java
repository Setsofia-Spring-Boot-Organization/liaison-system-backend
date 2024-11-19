package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import org.springframework.http.ResponseEntity;

public interface DashboardService {

    /**
     * This method retrieves statistical data about the system, including the count of lectures, students,
     * and attachments. It returns a {@link ResponseEntity} containing a {@link Response} with a {@link Statistics} object.
     *
     * @return A {@link ResponseEntity} containing a {@link Response} with the {@link Statistics}.
     */
    ResponseEntity<Response<Statistics>> getStatistics(String id, ConstantRequestParam param);
}
