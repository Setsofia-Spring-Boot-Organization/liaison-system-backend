package com.backend.liaison_system.users.student.util;

import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.student.StudentRepository;
import org.springframework.stereotype.Component;

import static com.backend.liaison_system.exception.Error.USER_NOT_FOUND;

@Component
public class StudentUtil {

    private final StudentRepository studentRepository;

    public StudentUtil(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void verifyUserIsStudent(String id) {
        studentRepository.findById(id).ifPresentOrElse((admin -> {
                    if (!admin.getRole().equals(UserRoles.STUDENT)) {
                        throw new LiaisonException(Error.UNAUTHORIZED_USER, new Throwable(Message.THE_USER_IS_NOT_AUTHORIZED.label));
                    }
                }),
                () -> {throw new LiaisonException(USER_NOT_FOUND, new Throwable(Message.USER_NOT_FOUND_CAUSE.label));}
        );
    }
}
