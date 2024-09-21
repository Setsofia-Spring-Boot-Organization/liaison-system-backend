package com.backend.liaison_system.users.admin.dashboard;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.dto.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.users.admin.dashboard.dao.Statistics;
import com.backend.liaison_system.users.admin.util.AdminUtil;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final AdminUtil adminUtil;
    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<Response<Statistics>> getStatistics(String id, ConstantRequestParam constantRequestParam) {

        // verify user role - ADMIN
        adminUtil.verifyUserIsAdmin(id);

        return null;
    }

    private long getTotalStudents(ConstantRequestParam constantRequestParam) throws LiaisonException {

        if (constantRequestParam.internshipType().name().equals(InternshipType.INTERNSHIP.name())) {
            return studentRepository.countStudentsByInternshipTypeEquals(constantRequestParam.internshipType());
        } else if (constantRequestParam.internshipType().name().equals(InternshipType.SEMESTER_OUT.name())) {
            return studentRepository.countStudentsByInternshipTypeEquals(constantRequestParam.internshipType());
        }

        throw new LiaisonException(Error.INVALID_INTERNSHIP_TYPE, new Throwable(Message.THE_INTERNSHIP_TYPE_IS_INCORRECT.label));
    }
}
