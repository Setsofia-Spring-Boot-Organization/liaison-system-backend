package com.backend.liaison_system.user_details;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.admin.repository.AdminRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiaisonUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return new LiaisonUserDetails(student.get());
        }

        Optional<Lecturer> lecturer = lecturerRepository.findByEmail(email);
        if (lecturer.isPresent()) {
            return new LiaisonUserDetails(lecturer.get());
        }

        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return new LiaisonUserDetails(admin.get());
        }

        throw new UsernameNotFoundException("user email not found");
    }
}
