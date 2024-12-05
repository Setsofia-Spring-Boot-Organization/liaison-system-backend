package com.backend.liaison_system.users.lecturer.util;

import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.lecturer.repository.LecturerRepository;
import org.springframework.stereotype.Component;

import static com.backend.liaison_system.exception.Error.USER_NOT_FOUND;

@Component
public class LecturerUtil {


    private final LecturerRepository lecturerRepository;

    public LecturerUtil(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public void verifyUserIsLecturer(String id) {
        lecturerRepository.findById(id).ifPresentOrElse((lecturer -> {
                    if (!lecturer.getRole().equals(UserRoles.LECTURER)) {
                        throw new LiaisonException(Error.UNAUTHORIZED_USER, new Throwable(Message.THE_USER_IS_NOT_AUTHORIZED.label));
                    }
                }),
                () -> {throw new LiaisonException(USER_NOT_FOUND, new Throwable(Message.USER_NOT_FOUND_CAUSE.label));}
        );
    }
}
