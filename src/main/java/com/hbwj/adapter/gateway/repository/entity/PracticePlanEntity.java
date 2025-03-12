package com.hbwj.adapter.gateway.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "practice_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "practice_plan_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private SportEntity sport;

    @Column(name = "total_duration_minutes")
    private Integer totalDurationMinutes;

    @ManyToOne
    @JoinColumn(name = "focus_area_id")
    private FocusAreaEntity focusArea;

    @OneToMany(mappedBy = "practicePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceOrder ASC")
    private Set<PracticePlanDrillEntity> practicePlanDrills = new HashSet<>();
}
