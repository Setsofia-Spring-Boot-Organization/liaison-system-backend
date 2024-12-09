package com.backend.liaison_system.users.admin.dao;

public record StudentLocationData(
        String lat,
        String lng,
        StudentDetails studentDetails,
        CompanyDetails companyDetails
) {
}
