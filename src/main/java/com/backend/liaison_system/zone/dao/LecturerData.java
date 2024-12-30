package com.backend.liaison_system.zone.dao;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;

import java.util.Set;

public record LecturerData(
        Set<Lecturer> lecturers,
        int totalLecturers
) { }
