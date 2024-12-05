package com.backend.liaison_system.users.lecturer.responses;

import java.util.Map;

public record CompaniesData(
        Map<String, Integer> companies,
        int totalCompanies
) { }
