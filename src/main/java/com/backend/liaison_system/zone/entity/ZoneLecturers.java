package com.backend.liaison_system.zone.entity;

import jakarta.persistence.Embeddable;

import java.util.Set;

@Embeddable
public record ZoneLecturers(
        Set<String> lecturers
) { }
