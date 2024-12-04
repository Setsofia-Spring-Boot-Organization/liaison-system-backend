package com.backend.liaison_system.zone.dto;

import java.util.Set;

public record NewZone (
    String name,
    String region,
    Set<String> towns,
    String zoneLead,
    Set<String> lecturerIds
) {}
