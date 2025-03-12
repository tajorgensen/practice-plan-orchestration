package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.DrillEquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrillEquipmentRepository extends JpaRepository<DrillEquipmentEntity, Long> {
    List<DrillEquipmentEntity> findByDrillId(Long drillId);

    List<DrillEquipmentEntity> findByEquipmentId(Long equipmentId);

    @Query("SELECT de FROM DrillEquipmentEntity de " +
            "JOIN de.drill d " +
            "JOIN d.sports s " +
            "WHERE s.id = :sportId")
    List<DrillEquipmentEntity> findBySportId(@Param("sportId") Long sportId);

    @Query("SELECT DISTINCT e FROM EquipmentEntity e " +
            "JOIN e.drillEquipments de " +
            "JOIN de.drill d " +
            "JOIN d.sports s " +
            "WHERE s.id = :sportId")
    List<EquipmentEntity> findEquipmentBySportId(@Param("sportId") Long sportId);
}
