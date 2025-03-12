package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import com.hbwj.adapter.gateway.repository.entity.PracticePlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticePlanRepository extends JpaRepository<PracticePlanEntity, Long> {
    List<PracticePlanEntity> findBySportId(Long sportId);

    List<PracticePlanEntity> findByFocusAreaId(Long focusAreaId);

    List<PracticePlanEntity> findBySportIdAndFocusAreaId(Long sportId, Long focusAreaId);

    @Query("SELECT pp FROM PracticePlanEntity pp " +
            "JOIN pp.practicePlanDrills ppd " +
            "JOIN ppd.drill d " +
            "WHERE d.id = :drillId")
    List<PracticePlanEntity> findByDrillId(@Param("drillId") Long drillId);

    @Query("SELECT DISTINCT e FROM EquipmentEntity e " +
            "JOIN e.drillEquipments de " +
            "JOIN de.drill d " +
            "JOIN d.practicePlanDrills ppd " +
            "JOIN ppd.practicePlan pp " +
            "WHERE pp.id = :practicePlanId")
    List<EquipmentEntity> findEquipmentByPracticePlanId(@Param("practicePlanId") Long practicePlanId);

    @Query("SELECT SUM(de.quantity) FROM DrillEquipmentEntity de " +
            "JOIN de.drill d " +
            "JOIN d.practicePlanDrills ppd " +
            "JOIN ppd.practicePlan pp " +
            "WHERE pp.id = :practicePlanId AND de.equipment.id = :equipmentId")
    Integer getTotalEquipmentQuantityForPlan(@Param("practicePlanId") Long practicePlanId,
                                             @Param("equipmentId") Long equipmentId);
}
