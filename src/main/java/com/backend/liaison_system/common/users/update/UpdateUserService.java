package com.backend.liaison_system.common.users.update;

import com.backend.liaison_system.common.requests.UpdateUserDetails;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface UpdateUserService {

    ResponseEntity<Response<?>> updateUser(String userId, UpdateUserDetails updateUserDetails, MultipartFile profileImage);
}
