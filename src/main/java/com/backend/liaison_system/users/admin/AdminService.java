package com.backend.liaison_system.users.admin;

import org.springframework.web.multipart.MultipartFile;
import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.NewUserRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    /**
     * This method creates a new admin user based on the provided NewUser data.
     *
     * @param newUserRequest the data for creating the new admin
     * @return a ResponseEntity containing another ResponseEntity with the created Admin entity
     */
    ResponseEntity<Response<Admin>> creatNewAdmin(NewUserRequest newUserRequest);

    /**
     * This method adds multiple student users to the database extracting them from a pdf
     * @param file an Excel sheet that contains all the students and their details
     * @return a Response Object
     */
    Response<?> uploadStudents(MultipartFile file);
}
