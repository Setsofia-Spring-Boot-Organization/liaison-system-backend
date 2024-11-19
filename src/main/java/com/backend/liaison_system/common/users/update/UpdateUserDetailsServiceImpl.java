package com.backend.liaison_system.common.users.update;

import com.backend.liaison_system.cloudinary.CloudinaryService;
import com.backend.liaison_system.common.requests.UpdateUserDetails;
import com.backend.liaison_system.common.users.UserInfo;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UpdateUserDetailsServiceImpl implements UpdateUserService{


    private final CloudinaryService cloudinaryService;
    private final UserInfo userInfo;
    private final AdminRepository adminRepository;
    private final LecturerRepository lecturerRepository;

    public UpdateUserDetailsServiceImpl(CloudinaryService cloudinaryService, UserInfo userInfo, AdminRepository adminRepository, LecturerRepository lecturerRepository) {
        this.cloudinaryService = cloudinaryService;
        this.userInfo = userInfo;
        this.adminRepository = adminRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public ResponseEntity<Response<?>> updateUser(String userId, UpdateUserDetails updateUserDetails, MultipartFile profileImage) throws IOException {
//        String userProfileUrl = cloudinaryService.uploadProfileImage(profileImage);

        // get the user's details
        UserRoles userRole = userInfo.getUserRole(userId);

        Admin updatedAdmin = new Admin();

        var updatedUser = (userRole.equals(UserRoles.ADMIN)) ?
                updateAdmin(userId, updateUserDetails) : (userRole.equals(UserRoles.LECTURER)) ?
                updateLecturer(userId, updateUserDetails) : (userRole.equals(UserRoles.STUDENT)) ?
                "updateStudent(userId, updateUserDetails)" : "";


        return ResponseEntity.status(HttpStatus.OK).body(
                new Response<>(
                        HttpStatus.OK.value(),
                        "update",
                        updatedUser
                ));
    }

    private Admin updateAdmin(String userId, UpdateUserDetails updateUserDetails) {
        Optional<Admin> optionalAdmin = adminRepository.findById(userId);

        assert optionalAdmin.isPresent();
        Admin admin = optionalAdmin.get();

        admin.setUpdatedAt(LocalDateTime.now());
        admin.setFirstName(updateUserDetails.firstname().isEmpty() ? admin.getFirstName() : updateUserDetails.firstname());
        admin.setLastName(updateUserDetails.lastname().isEmpty() ? admin.getLastName() : updateUserDetails.lastname());
        admin.setOtherName(updateUserDetails.middleName().isEmpty() ? admin.getOtherName() : updateUserDetails.middleName());

        return adminRepository.save(admin);
    }

    private Lecturer updateLecturer(String userId, UpdateUserDetails updateUserDetails) {
        Optional<Lecturer> optionalLecturer = lecturerRepository.findById(userId);

        assert optionalLecturer.isPresent();
        Lecturer lecturer = optionalLecturer.get();

        lecturer.setUpdatedAt(LocalDateTime.now());
        lecturer.setFirstName(updateUserDetails.firstname().isEmpty() ? lecturer.getFirstName() : updateUserDetails.firstname());
        lecturer.setLastName(updateUserDetails.lastname().isEmpty() ? lecturer.getLastName() : updateUserDetails.lastname());
        lecturer.setOtherName(updateUserDetails.middleName().isEmpty() ? lecturer.getOtherName() : updateUserDetails.middleName());

        return lecturerRepository.save(lecturer);
    }
}
