package com.backend.liaison_system.users.admin.dao;

public record StudentLocationData(
        StudentDetails studentDetails,
        CompanyDetails companyDetails,
        int lat,
        int lng
) {
}
