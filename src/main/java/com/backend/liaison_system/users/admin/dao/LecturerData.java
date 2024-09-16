package com.backend.liaison_system.users.admin.dao;

import java.util.List;

public record LecturerData (
    String id,
    String dp,
    String name,
    String department,
    String phone,
    String email,
    String age,
    String faculty,
    String gender,
    List<String> others
) { }
