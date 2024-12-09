package com.backend.liaison_system.users.admin.dao;

public record CompanyDetails(
        String name,
        String email,
        String phone,
        String region,
        String exactLocation
) {
}
