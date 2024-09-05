package com.backend.liaison_system.authentication;

import com.backend.liaison_system.authentication.dto.LoginRequest;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dao.data.LoginData;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.jwt.JwtServiceImpl;
import com.backend.liaison_system.user_details.LiaisonUserDetails;
import com.backend.liaison_system.users.admin.Admin;
import com.backend.liaison_system.users.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AdminRepository adminRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Response<LoginData>> loginUser(LoginRequest loginRequest) {
        Optional<? extends LiaisonUserDetails> userDetails = Optional.empty();

        // find the admin account
        Optional<Admin> admin = adminRepository.findByEmail(loginRequest.email());

        if (admin.isPresent()) {
            userDetails = Optional.of(new LiaisonUserDetails(admin.get()));
        }

        if (userDetails.isPresent()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            String token = jwtServiceImpl.generateToken(userDetails.get());

            return ResponseEntity.status(HttpStatus.OK).body(
                    Response.<LoginData>builder()
                            .status(HttpStatus.OK.value())
                            .message("login successful")
                            .data(new LoginData(
                                    userDetails.get().getId(),
                                    userDetails.get().getFirstName(),
                                    userDetails.get().getLastName(),
                                    userDetails.get().getRole(),
                                    token
                            ))
                            .build()
            );
        }
        throw new LiaisonException(Error.USER_NOT_FOUND);
    }
}
