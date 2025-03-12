package com.hbwj.adapter.gateway.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "focus_areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusAreaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "focus_area_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "focusArea")
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "focusArea")
    private Set<DrillEntity> drills = new HashSet<>();
}
