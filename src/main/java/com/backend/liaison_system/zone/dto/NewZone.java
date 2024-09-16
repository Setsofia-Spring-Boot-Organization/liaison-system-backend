package com.backend.liaison_system.zone.dto;

import java.util.List;

public record NewZone (
    String name,
    String region,
    String zoneLead,
    List<String> lecturerIds
) {}
