package com.hbwj.adapter.gateway.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "drills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrillEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drill_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    // Equipment is now tracked in the DrillEquipment entity
    @OneToMany(mappedBy = "drill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DrillEquipmentEntity> drillEquipments = new HashSet<>();

    @OneToMany(mappedBy = "drill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PracticePlanDrillEntity> practicePlanDrills = new HashSet<>();

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "focus_area_id", nullable = false)
    private FocusAreaEntity focusArea;

    @ManyToMany
    @JoinTable(
            name = "drill_sports",
            joinColumns = @JoinColumn(name = "drill_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    private Set<SportEntity> sports = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "drill_positions",
            joinColumns = @JoinColumn(name = "drill_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private Set<PositionEntity> positions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "drill_kpis",
            joinColumns = @JoinColumn(name = "drill_id"),
            inverseJoinColumns = @JoinColumn(name = "kpi_id")
    )
    private Set<KpiEntity> kpis = new HashSet<>();
}
