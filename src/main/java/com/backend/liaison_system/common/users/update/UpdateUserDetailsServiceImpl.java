package com.backend.liaison_system.common.users.update;

import com.backend.liaison_system.LiaisonSystemApplication;
import com.backend.liaison_system.cloudinary.CloudinaryService;
import com.backend.liaison_system.common.requests.UpdateUserDetails;
import com.backend.liaison_system.common.users.UserInfo;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import jakarta.transaction.Transactional;
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
    private final StudentRepository studentRepository;

    public UpdateUserDetailsServiceImpl(CloudinaryService cloudinaryService, UserInfo userInfo, AdminRepository adminRepository, LecturerRepository lecturerRepository, StudentRepository studentRepository) {
        this.cloudinaryService = cloudinaryService;
        this.userInfo = userInfo;
        this.adminRepository = adminRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(rollbackOn = {LiaisonSystemApplication.class, Exception.class})
    @Override
    public ResponseEntity<Response<?>> updateUser(String userId, UpdateUserDetails updateUserDetails, MultipartFile profileImage) throws IOException {

        // get the user's details
        UserRoles userRole = userInfo.getUserRole(userId);

        // upload the image to cloudinary and return its url
        String userProfileUrl = profileImage.isEmpty()? "" : cloudinaryService.uploadProfileImage(profileImage);

        var updatedUser = (userRole.equals(UserRoles.ADMIN)) ?
                updateAdmin(userId, updateUserDetails, userProfileUrl) : (userRole.equals(UserRoles.LECTURER)) ?
                updateLecturer(userId, updateUserDetails, userProfileUrl) : (userRole.equals(UserRoles.STUDENT)) ?
                updateStudent(userId, updateUserDetails, userProfileUrl) : null;


        return ResponseEntity.status(HttpStatus.OK).body(
                new Response<>(
                        HttpStatus.OK.value(),
                        "update successful",
                        updatedUser
                ));
    }

    private Admin updateAdmin(String userId, UpdateUserDetails updateUserDetails, String userProfileUrl) {
        Optional<Admin> optionalAdmin = adminRepository.findById(userId);

        assert optionalAdmin.isPresent();
        Admin admin = optionalAdmin.get();

        admin.setUpdatedAt(LocalDateTime.now());
        admin.setFirstName(updateUserDetails.firstname().isEmpty() ? admin.getFirstName() : updateUserDetails.firstname());
        admin.setLastName(updateUserDetails.lastname().isEmpty() ? admin.getLastName() : updateUserDetails.lastname());
        admin.setOtherName(updateUserDetails.middleName().isEmpty() ? admin.getOtherName() : updateUserDetails.middleName());
        admin.setProfilePictureUrl(userProfileUrl.isEmpty() ? admin.getProfilePictureUrl() : userProfileUrl);

        return adminRepository.save(admin);
    }

    private Lecturer updateLecturer(String userId, UpdateUserDetails updateUserDetails, String userProfileUrl) {
        Optional<Lecturer> optionalLecturer = lecturerRepository.findById(userId);

        assert optionalLecturer.isPresent();
        Lecturer lecturer = optionalLecturer.get();

        lecturer.setUpdatedAt(LocalDateTime.now());
        lecturer.setFirstName(updateUserDetails.firstname().isEmpty() ? lecturer.getFirstName() : updateUserDetails.firstname());
        lecturer.setLastName(updateUserDetails.lastname().isEmpty() ? lecturer.getLastName() : updateUserDetails.lastname());
        lecturer.setOtherName(updateUserDetails.middleName().isEmpty() ? lecturer.getOtherName() : updateUserDetails.middleName());
        lecturer.setPhone(updateUserDetails.phone().isEmpty() ? lecturer.getPhone() : updateUserDetails.phone());
        lecturer.setProfilePictureUrl(userProfileUrl.isEmpty() ? lecturer.getProfilePictureUrl() : userProfileUrl);

        return lecturerRepository.save(lecturer);
    }

    private Student updateStudent(String userId, UpdateUserDetails updateUserDetails, String userProfileUrl) {
        Optional<Student> optionalStudent = studentRepository.findById(userId);

        assert optionalStudent.isPresent();
        Student student = optionalStudent.get();

        student.setUpdatedAt(LocalDateTime.now());
        student.setStudentFirstName(updateUserDetails.firstname().isEmpty() ? student.getStudentFirstName() : updateUserDetails.firstname());
        student.setStudentLastName(updateUserDetails.lastname().isEmpty() ? student.getStudentLastName() : updateUserDetails.lastname());
        student.setStudentOtherName(updateUserDetails.middleName().isEmpty() ? student.getStudentOtherName() : updateUserDetails.middleName());
        student.setStudentPhone(updateUserDetails.phone().isEmpty() ? student.getStudentPhone() : updateUserDetails.phone());
        student.setProfilePictureUrl(userProfileUrl.isEmpty() ? student.getProfilePictureUrl() : userProfileUrl);

        return studentRepository.save(student);
    }
}
