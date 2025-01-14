package com.backend.liaison_system.users.lecturer.responses;

import com.backend.liaison_system.users.lecturer.entity.Lecturer;

import java.util.Set;

public record OtherLecturersData(
    Set<Lecturer> lecturers,
    int totalLecturers
) { }
