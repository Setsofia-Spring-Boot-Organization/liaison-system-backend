package com.backend.liaison_system.users.admin.dashboard.dao;

import com.backend.liaison_system.users.admin.dao.Lecturers;
import java.util.List;

public record AssignedLecturers(
        List<Lecturers> assignedLecturers,
        int count
) {
}
