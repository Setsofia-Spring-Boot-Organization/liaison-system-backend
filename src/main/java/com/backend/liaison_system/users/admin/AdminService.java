package com.backend.liaison_system.users.admin;

import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    public String uploadStudents(MultipartFile file);
}
