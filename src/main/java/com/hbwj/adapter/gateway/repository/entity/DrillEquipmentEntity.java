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
@Table(name = "drill_equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrillEquipmentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drill_equipment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drill_id", nullable = false)
    private DrillEntity drill;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipment;

    @Column(name = "quantity")
    private Integer quantity;
}
