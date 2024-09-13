package com.backend.liaison_system.users.admin.service;

import com.backend.liaison_system.users.admin.dto.AdminPageRequest;
import com.backend.liaison_system.users.admin.entity.Admin;
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
     * This method adds multiple student users to the database extracting them from an Excel sheet
     * @param file an Excel sheet that contains all the students and their details
     * @return a Response Object
     */
    Response<?> uploadStudents(MultipartFile file);

    Response<?> getStudents(AdminPageRequest request);

    /**
     * This method retrieves a list of lecturers based on pagination and filter criteria provided in the AdminPageRequest.
     * The request can include optional filters such as admin ID, name, faculty, and department, as well as pagination settings
     * like the page number and page size to control the result set.
     *
     * @param adminPageRequest the request object containing pagination settings and optional filters to narrow down the search
     * @return a Response object containing the list of lecturers that match the specified criteria
     */
    ResponseEntity<Response<?>> getLecturers(String id, AdminPageRequest adminPageRequest);
}