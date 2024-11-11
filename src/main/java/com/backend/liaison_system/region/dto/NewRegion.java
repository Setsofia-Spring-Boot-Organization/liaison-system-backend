package com.backend.liaison_system.region.dto;

import java.util.Set;

public record NewRegion(
        String region,
        Set<String> towns
) {
}
