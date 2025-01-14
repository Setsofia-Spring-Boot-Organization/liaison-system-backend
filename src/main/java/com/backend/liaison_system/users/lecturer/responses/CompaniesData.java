package com.backend.liaison_system.users.lecturer.responses;

import com.backend.liaison_system.users.admin.dao.CompanyDetails;

import java.util.Map;
import java.util.Set;

public record CompaniesData(
        Set<CompanyDetails> companyDetails,
        Map<String, Integer> companies,
        int totalCompanies
) { }
