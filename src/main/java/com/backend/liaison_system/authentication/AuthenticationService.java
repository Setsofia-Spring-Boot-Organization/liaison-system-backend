package com.backend.liaison_system.authentication;

import com.backend.liaison_system.authentication.dto.LoginRequest;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dao.data.LoginData;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    /**
     * This method handles user login based on the provided login request data.
     * It authenticates the user and returns a ResponseEntity containing a Response object
     * with the login data if the authentication is successful.
     *
     * @param loginRequest the request data containing the user's login credentials
     * @return a ResponseEntity containing a Response object with the login data
     */
    ResponseEntity<Response<LoginData>> loginUser(LoginRequest loginRequest);
}
