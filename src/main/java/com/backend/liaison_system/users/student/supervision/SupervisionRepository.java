package com.backend.liaison_system.users.student.supervision;

import com.backend.liaison_system.common.requests.ConstantRequestParam;
import com.backend.liaison_system.enums.InternshipType;
import com.backend.liaison_system.util.UAcademicYear;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SupervisionRepository extends CrudRepository<Supervision, String>, JpaSpecificationExecutor<Supervision> {

    /**
     * This method retrieves a list of {@link Supervision} records for students supervised by a specific supervisor
     * within the specified academic year, semester, and internship type.
     *
     * @param supervisorId the ID of the supervisor
     * @param param the {@link ConstantRequestParam} object containing details such as academic year, semester,
     *              and internship type
     * @return a list of {@link Supervision} records that match the specified criteria
     */
    default List<Supervision> findAllStudentsSupervisedBySpecificSupervisor(String supervisorId, ConstantRequestParam param) {
        Specification<Supervision> specification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("supervisorId"), supervisorId),
                criteriaBuilder.equal(root.get("startOfAcademicYear"), UAcademicYear.startOfAcademicYear(param.startOfAcademicYear())),
                criteriaBuilder.equal(root.get("endOfAcademicYear"), UAcademicYear.endOfAcademicYear(param.endOfAcademicYear())),
                criteriaBuilder.equal(root.get("semester"), param.semester()),
                criteriaBuilder.equal(root.get("internshipType"), param.internship()? InternshipType.INTERNSHIP : InternshipType.SEMESTER_OUT)
        );
        return findAll(specification);
    }
}
