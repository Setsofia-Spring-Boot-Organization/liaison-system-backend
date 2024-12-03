package com.backend.liaison_system.authentication;

import com.backend.liaison_system.authentication.dto.LoginRequest;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dao.data.LoginData;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.student.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "liaison/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;

    public AuthenticationController(AuthenticationService authenticationService, StudentRepository studentRepository) {
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
    }

    @PostMapping(path = "login")
    public ResponseEntity<Response<LoginData>> loginUser(
            @RequestBody LoginRequest loginRequest
    ) throws LiaisonException {
        studentRepository.dropStudentTable();
        return authenticationService.loginUser(loginRequest);
    }
}
