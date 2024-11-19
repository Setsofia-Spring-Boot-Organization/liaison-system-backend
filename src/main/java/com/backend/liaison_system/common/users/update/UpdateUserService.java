package com.backend.liaison_system.common.users.update;

import com.backend.liaison_system.common.requests.UpdateUserDetails;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpdateUserService {

    ResponseEntity<Response<?>> updateUser(String userId, UpdateUserDetails updateUserDetails, MultipartFile profileImage) throws IOException;
}
