package com.backend.liaison_system.common.requests;

public record ConstantRequestParam(
    String startOfAcademicYear,
    String endOfAcademicYear,
    int semester,
    boolean internship
) { }
