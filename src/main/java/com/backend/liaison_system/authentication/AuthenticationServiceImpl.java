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
import com.backend.liaison_system.users.lecturer.Lecturer;
import com.backend.liaison_system.users.lecturer.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.backend.liaison_system.exception.Cause.THE_EMAIL_OR_PASSWORD_DO_NOT_MATCH;
import static com.backend.liaison_system.exception.Error.INVALID_USERNAME_OR_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AdminRepository adminRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;

    /**
     * This method retrieves the LiaisonUserDetails for a user based on their email.
     * It checks if the user is an Admin, Lecturer, or Student, and returns the corresponding
     * LiaisonUserDetails if found. If the user is not found in any repository, a LiaisonException is thrown.
     *
     * @param email the email of the user whose details are to be retrieved
     * @return the LiaisonUserDetails of the user corresponding to the provided email
     * @throws LiaisonException if the user is not found in any of the repositories
     */
    private LiaisonUserDetails getLiaisonUserDetails(String email) {
        Optional<? extends LiaisonUserDetails> userDetails;

        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            userDetails = Optional.of(new LiaisonUserDetails(admin.get()));
            return userDetails.get();
        }

        Optional<Lecturer> lecturer = lecturerRepository.findByEmail(email);
        if (lecturer.isPresent()) {
            userDetails = Optional.of(new LiaisonUserDetails(lecturer.get()));
            return userDetails.get();
        }

        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            userDetails = Optional.of(new LiaisonUserDetails(student.get()));
            return userDetails.get();
        }

        throw new LiaisonException(Error.USER_NOT_FOUND);
    }


    @Override
    public ResponseEntity<Response<LoginData>> loginUser(LoginRequest loginRequest) {

        // find the user's account
        LiaisonUserDetails userDetails = getLiaisonUserDetails(loginRequest.email());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            String token = jwtServiceImpl.generateToken(userDetails);

            return ResponseEntity.status(HttpStatus.OK).body(
                    Response.<LoginData>builder()
                            .status(HttpStatus.OK.value())
                            .message("login successful")
                            .data(new LoginData(token))
                            .build()
            );
        } catch (AuthenticationException exception) {
            throw new LiaisonException(INVALID_USERNAME_OR_PASSWORD, new Throwable(THE_EMAIL_OR_PASSWORD_DO_NOT_MATCH.label));
        }
    }
}
