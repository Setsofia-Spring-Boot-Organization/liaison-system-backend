package com.backend.liaison_system.region.dto;

import java.util.List;

public record NewRegion(
        String region,
        List<String> towns
) {
}
