package com.backend.liaison_system.users.admin.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPageRequest {
    @Builder.Default
    Integer pageNumber = 1;
    @Builder.Default
    Integer pageSize = 10;
    String adminId;
}
