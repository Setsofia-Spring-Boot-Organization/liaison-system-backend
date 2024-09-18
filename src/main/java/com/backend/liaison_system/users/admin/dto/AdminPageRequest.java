package com.backend.liaison_system.users.admin.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPageRequest {
    @Builder.Default
    Integer page = 1;
    @Builder.Default
    Integer size = 10;
    String find;
    String adminId;
    String name;
    String faculty;
    String department;
    Boolean internship;
}
