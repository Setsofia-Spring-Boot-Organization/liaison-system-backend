package com.backend.liaison_system.users.admin;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.exception.LiaisonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static com.backend.liaison_system.exception.Cause.THE_FOLLOWING_FIELDS_ARE_EMPTY;
import static com.backend.liaison_system.exception.Cause.THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM;
import static com.backend.liaison_system.exception.Error.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<Response<Admin>> creatNewAdmin(NewUserRequest request) {
        Optional<Admin> admin = adminRepository.findByEmail(request.getEmail());
        if (admin.isPresent()) {
            throw new LiaisonException(EMAIL_ALREADY_EXISTS, new Throwable(THE_SUBMITTED_EMAIL_ALREADY_EXISTS_IN_THE_SYSTEM.label));
        };

        //create the admin
        Admin createdAdmin = createNewAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.<Admin>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("admin created successfully")
                        .data(createdAdmin)
                        .build()
        );
    }

    /*
      This section contains helper methods that provide additional utility functions
      to support the main application logic.<br>
      These methods typically perform common tasks such as validation, data formatting,
      or other reusable operations that assist in the overall functionality of the application.
     */

    /**
     * This method validates the fields in the NewUserRequest object to check for any empty or null values.<br>
     * It returns a list of field names that are empty or null.
     *
     * @param request the NewUserRequest object containing the fields to be validated
     */
    private void validateRequestFields(NewUserRequest request) {
        List<String> emptyFields = new ArrayList<>();
        if (request.getEmail() == null || request.getEmail().isEmpty()) emptyFields.add("email");
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) emptyFields.add("firstname");
        if (request.getLastName() == null || request.getLastName().isEmpty()) emptyFields.add("lastname");
        if (request.getPassword() == null || request.getPassword().isEmpty()) emptyFields.add("password");

        if (!emptyFields.isEmpty()) throw new LiaisonException(REQUIRED_FIELDS_ARE_EMPTY, new Throwable(THE_FOLLOWING_FIELDS_ARE_EMPTY.label + emptyFields));
    }

    private Admin createNewAdmin(NewUserRequest request) {

        // validate the input fields
        validateRequestFields(request);

        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        admin.setEmail(request.getEmail());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setOtherName(request.getOtherName());
        admin.setRole(request.getRole());

        // encode the password before setting it
        String encodedPass = passwordEncoder.encode(request.getPassword());
        admin.setPassword(encodedPass);

        try {
            return adminRepository.save(admin);

        } catch (Exception exception) {
            System.out.println("The exception: " + exception);
            throw new LiaisonException(ERROR_SAVING_DATA);
        }
    }
}
