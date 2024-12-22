package com.backend.liaison_system.users.admin.dashboard.dao;

import com.backend.liaison_system.users.admin.dao.Lecturers;

import java.util.List;

public record UnassignedLecturers(
        List<Lecturers> unAssignedLecturers,
        int count
) {
}
