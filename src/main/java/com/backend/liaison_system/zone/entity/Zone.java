package com.backend.liaison_system.zone.entity;

import com.backend.liaison_system.enums.InternshipType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String region;
    private String zoneLead;

    /**
     * The start and end dates are the start
     * and end of the academic years in which the ZONE
     * was created
     */
    private LocalDateTime startOfAcademicYear;
    private LocalDateTime endOfAcademicYear;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    private InternshipType internshipType;

    @Embedded
    private ZoneLecturers lecturers;

    @Embedded
    private Towns towns;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Zone zone = (Zone) o;
        return getId() != null && Objects.equals(getId(), zone.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
