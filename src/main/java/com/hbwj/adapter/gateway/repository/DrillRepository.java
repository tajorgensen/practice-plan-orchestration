package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.DrillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrillRepository extends JpaRepository<DrillEntity, Long> {
    List<DrillEntity> findByFocusAreaId(Long focusAreaId);

    @Query("SELECT DISTINCT d FROM DrillEntity d " +
            "JOIN d.sports s " +
            "WHERE s.id = :sportId")
    List<DrillEntity> findBySportId(@Param("sportId") Long sportId);

    @Query("SELECT DISTINCT d FROM DrillEntity d " +
            "JOIN d.positions p " +
            "WHERE p.id = :positionId")
    List<DrillEntity> findByPositionId(@Param("positionId") Long positionId);

    @Query("SELECT DISTINCT d FROM DrillEntity d " +
            "JOIN d.kpis k " +
            "WHERE k.id = :kpiId")
    List<DrillEntity> findByKpiId(@Param("kpiId") Long kpiId);

    @Query("SELECT DISTINCT d FROM DrillEntity d " +
            "JOIN d.sports s " +
            "JOIN d.focusArea fa " +
            "WHERE s.id = :sportId AND fa.id = :focusAreaId")
    List<DrillEntity> findBySportIdAndFocusAreaId(@Param("sportId") Long sportId, @Param("focusAreaId") Long focusAreaId);
}
