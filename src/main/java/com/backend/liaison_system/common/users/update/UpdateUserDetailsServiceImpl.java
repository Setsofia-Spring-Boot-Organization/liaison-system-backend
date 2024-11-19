package com.backend.liaison_system.common.users.update;

import com.backend.liaison_system.cloudinary.CloudinaryService;
import com.backend.liaison_system.common.requests.UpdateUserDetails;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UpdateUserDetailsServiceImpl implements UpdateUserService{


    private final CloudinaryService cloudinaryService;

    public UpdateUserDetailsServiceImpl(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ResponseEntity<Response<?>> updateUser(String userId, UpdateUserDetails updateUserDetails, MultipartFile profileImage) throws IOException {
        String userProfileUrl = cloudinaryService.uploadProfileImage(profileImage);

        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(HttpStatus.OK.value(), "update", userProfileUrl));
    }
}
