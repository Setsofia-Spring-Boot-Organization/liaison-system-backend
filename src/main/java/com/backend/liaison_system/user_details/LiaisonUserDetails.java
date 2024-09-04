package com.backend.liaison_system.user_details;

import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.users.lecturer.Lecturer;
import com.backend.liaison_system.users.admin.Admin;
import com.backend.liaison_system.users.student.Student;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class LiaisonUserDetails implements UserDetails {
    @Getter
    private String id;
    private String email;
    private String password;
    private UserRoles role;

    // the student details
    LiaisonUserDetails(Student student) {
        this.id = student.getId();
        this.email = student.getEmail();
        this.password = student.getPassword();
        this.role = student.getRole();
    }

    // the lecturer details
    LiaisonUserDetails(Lecturer lecturer) {
        this.id = lecturer.getId();
        this.email = lecturer.getEmail();
        this.password = lecturer.getPassword();
        this.role = lecturer.getRole();
    }

    // the liaison operative details
    LiaisonUserDetails(Admin admin) {
        this.id = admin.getId();
        this.email = admin.getEmail();
        this.password = admin.getPassword();
        this.role = admin.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
