package com.backend.liaison_system.users.student;

import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<Response<?>> getDashboardData(String studentId);
}
