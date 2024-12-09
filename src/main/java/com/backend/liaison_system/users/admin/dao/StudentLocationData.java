package com.backend.liaison_system.users.admin.dao;

public record StudentLocationData(
        StudentDetails studentDetails,
        CompanyDetails companyDetails,
        String lat,
        String lng
) {
}
