package com.backend.liaison_system.authentication;

import com.backend.liaison_system.dao.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "liaison/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @PostMapping(path = "login")
    public ResponseEntity<Response<String>> loginUser() {
        return new ResponseEntity<>(Response.<String>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data("What are the details needed for a student?")
                .build(),
                HttpStatus.OK
        );
    }
}
