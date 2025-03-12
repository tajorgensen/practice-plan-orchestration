package com.hbwj.adapter.gateway.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "practice_plan_drills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlanDrillEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "practice_plan_drill_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "practice_plan_id", nullable = false)
    private PracticePlanEntity practicePlan;

    @ManyToOne
    @JoinColumn(name = "drill_id", nullable = false)
    private DrillEntity drill;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
