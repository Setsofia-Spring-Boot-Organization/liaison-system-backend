package com.backend.liaison_system.dao.data;

import com.backend.liaison_system.enums.UserRoles;

public record LoginData(
    String id,
    String firstName,
    String lastName,
    UserRoles role,
    String token
) { }
