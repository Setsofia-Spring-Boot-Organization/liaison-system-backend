package com.backend.liaison_system.users.admin;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.NewUserRequest;
import com.backend.liaison_system.exception.LiaisonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "liaison/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    @PostMapping
    public ResponseEntity<Response<Admin>> createNewAdmin(
            @Validated NewUserRequest newUserRequest
    ) throws LiaisonException {
        return adminServiceImpl.creatNewAdmin(newUserRequest);
    }
}
