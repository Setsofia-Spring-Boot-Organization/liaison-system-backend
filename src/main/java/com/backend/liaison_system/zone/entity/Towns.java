package com.backend.liaison_system.zone.entity;

import jakarta.persistence.Embeddable;

import java.util.List;
import java.util.Set;

@Embeddable
public record Towns(
        Set<String> towns
) { }
