package com.backend.liaison_system.users.student;

import com.backend.liaison_system.enums.Status;
import com.backend.liaison_system.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String password;

    private String studentFirstName;
    private String studentLastName;
    private String studentOtherName;
    private String studentDepartment;
    private String studentFaculty;
    private String studentAge;
    private String studentEmail;
    private String studentGender;
    private String studentPhone;
    private String studentAbout;
    private String studentCourse;
    private String placeOfInternship;
    private String startDate;
    private String endDate;
    private Status status;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Student student = (Student) o;
        return getId() != null && Objects.equals(getId(), student.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
