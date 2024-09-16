package com.backend.liaison_system.zone.entity;

import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public record ZoneLecturers(
        List<String> lecturers
) { }