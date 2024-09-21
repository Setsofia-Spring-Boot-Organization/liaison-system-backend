package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final AdminUtil adminUtil;

    @Override
    public ResponseEntity<Response<Statistics>> getStatistics(String id) {

        // verify user role - ADMIN
        adminUtil.verifyUserIsAdmin(id);

        return null;
    }
}
