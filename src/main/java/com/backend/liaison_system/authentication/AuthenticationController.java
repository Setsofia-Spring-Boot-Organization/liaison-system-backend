package com.backend.liaison_system.authentication;

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
    public ResponseEntity<String> loginUser() {
        return new ResponseEntity<>("User logged in successfully!", HttpStatus.OK);
    }
}
