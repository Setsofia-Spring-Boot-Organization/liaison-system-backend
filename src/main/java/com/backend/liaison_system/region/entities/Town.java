package com.backend.liaison_system.region.entities;

import jakarta.persistence.Embeddable;

import java.util.Set;

@Embeddable
public record Town(
        Set<String> towns
) { }
