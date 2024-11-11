package com.backend.liaison_system.region.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String region;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    @Embedded
    private Town town;
}
