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
@Table(name = "drill_kpis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrillKpiEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drill_kpi_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drill_id", nullable = false)
    private DrillEntity drill;

    @ManyToOne
    @JoinColumn(name = "kpi_id", nullable = false)
    private KpiEntity kpi;

    @Column(name = "impact_level")
    private Integer impactLevel; // Scale of 1-10
}
