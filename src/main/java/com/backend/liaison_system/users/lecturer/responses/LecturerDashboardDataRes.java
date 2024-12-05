package com.backend.liaison_system.users.lecturer.responses;

public record LecturerDashboardDataRes(
        StudentsData student,
        CompaniesData company,
        OtherLecturersData lecturer
) { }
