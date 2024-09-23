package com.backend.liaison_system.dto;

import com.backend.liaison_system.enums.InternshipType;

public record ConstantRequestParam (
    String internshipType,
    String startYear,
    String endYear
) { }
