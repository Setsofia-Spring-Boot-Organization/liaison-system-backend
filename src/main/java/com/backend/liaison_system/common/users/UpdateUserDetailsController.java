package com.backend.liaison_system.common.users;

import com.backend.liaison_system.common.requests.UpdateUserDetails;
import com.backend.liaison_system.common.users.update.UpdateUserService;
import com.backend.liaison_system.dao.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "liaison/api/v1/user")
public class UpdateUserDetailsController {

    private final UpdateUserService userService;

    public UpdateUserDetailsController(UpdateUserService userService) {
        this.userService = userService;
    }


    @PatchMapping(path = "/{user_id}/update")
    public ResponseEntity<Response<?>> updateUserDetails(
            @PathVariable String user_id,
            @ModelAttribute UpdateUserDetails updateUserDetails,
            @RequestPart(name = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {
        return userService.updateUser(user_id, updateUserDetails, profileImage);
    }
}
