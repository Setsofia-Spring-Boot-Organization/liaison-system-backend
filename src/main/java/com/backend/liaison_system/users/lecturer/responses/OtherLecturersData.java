package com.backend.liaison_system.users.lecturer.responses;

import java.util.Set;

public record OtherLecturersData(
    Set<String> lecturers,
    int totalLecturers
) { }
