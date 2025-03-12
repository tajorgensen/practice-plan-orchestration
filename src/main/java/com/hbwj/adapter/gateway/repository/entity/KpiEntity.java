package com.hbwj.adapter.gateway.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "kpis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kpi_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategoryEntity subCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "measurement_unit")
    private String measurementUnit;

    @Column(name = "target_value", precision = 10, scale = 2)
    private BigDecimal targetValue;

    @ManyToMany(mappedBy = "kpis")
    private Set<DrillEntity> drills = new HashSet<>();
}
