package com.backend.liaison_system.authentication;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dao.data.LoginData;
import com.backend.liaison_system.enums.UserRoles;
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
    public ResponseEntity<Response<LoginData>> loginUser() {
        return new ResponseEntity<>(Response.<LoginData>builder()
                .status(HttpStatus.OK.value())
                .message("login successful")
                .data(new LoginData(
                        "1234567",
                        "Nusetor",
                        "Setsofia",
                        UserRoles.ADMIN,
                        "qwnsavdvahskjdnaoskdn1234rfsdjnvskfdvskmadf3csdjkb"
                ))
                .build(),
                HttpStatus.OK
        );
    }
}
