package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("liaison/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping
    public ResponseEntity<Response<?>> getStatistics() throws LiaisonException {

    }
}
