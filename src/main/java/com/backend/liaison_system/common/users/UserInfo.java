package com.backend.liaison_system.common.users;

import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfo {

    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final AdminRepository adminRepository;

    public UserInfo(StudentRepository studentRepository, LecturerRepository lecturerRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.adminRepository = adminRepository;
    }

    public UserRoles getUserRole(String userId) {

        Optional<Student> student;
        Optional<Lecturer> lecturer;
        Optional<Admin> admin;

        student = studentRepository.findById(userId);
        if (student.isPresent()) {
            return student.get().getRole();
        }

        lecturer = lecturerRepository.findById(userId);
        if (lecturer.isPresent()) {
            return lecturer.get().getRole();
        }

        admin = adminRepository.findById(userId);
        if (admin.isPresent()) {
            return admin.get().getRole();
        }

        throw new LiaisonException(Error.USER_NOT_FOUND, new Throwable(Message.USER_NOT_FOUND_CAUSE.label));
    }


}
