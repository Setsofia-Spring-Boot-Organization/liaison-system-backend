package com.backend.liaison_system.user_details;

import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.users.lecturer.entity.Lecturer;
import com.backend.liaison_system.users.admin.entity.Admin;
import com.backend.liaison_system.users.student.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class LiaisonUserDetails implements UserDetails {
    private final String id;
    private String firstName;
    private String lastName;
    private final String email;
    private final String password;
    private final UserRoles role;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserRoles getRole() {
        return role;
    }

    // the student details
    public LiaisonUserDetails(Student student) {
        this.id = student.getId();
        this.firstName = student.getStudentFirstName();
        this.lastName = student.getStudentLastName();
        this.email = student.getStudentEmail();
        this.password = student.getPassword();
        this.role = student.getRole();
    }

    // the lecturer details
    public LiaisonUserDetails(Lecturer lecturer) {
        this.id = lecturer.getId();
        this.firstName = lecturer.getFirstName();
        this.lastName = lecturer.getLastName();
        this.email = lecturer.getEmail();
        this.password = lecturer.getPassword();
        this.role = lecturer.getRole();
    }

    // the liaison operative details
    public LiaisonUserDetails(Admin admin) {
        this.id = admin.getId();
        this.firstName = admin.getFirstName();
        this.lastName = admin.getLastName();
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
