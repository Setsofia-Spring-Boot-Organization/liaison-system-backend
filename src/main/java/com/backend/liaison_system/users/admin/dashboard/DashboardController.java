package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.ConstantRequestParam;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("liaison/api/v1/admin/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(path = "/{admin-id}")
    public ResponseEntity<Response<Statistics>> getStatistics(
            @PathVariable("admin-id") String id,
            ConstantRequestParam constantRequestParam
    ) throws LiaisonException {
        return dashboardService.getStatistics(id, constantRequestParam);
    }
}
