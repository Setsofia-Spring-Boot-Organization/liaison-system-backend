package com.backend.liaison_system.common;

public record ConstantRequestParam(
    String startOfAcademicYear,
    String endOfAcademicYear,
    boolean internship,
    int page,
    int size
) {
    @Override
    public int page() {
        return 1;
    }

    @Override
    public int size() {
        return 10;
    }
}
