package com.backend.liaison_system.region.entities;

import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public record Town(
        List<String> towns
) { }
