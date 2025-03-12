package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.DrillKpiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrillKpiRepository extends JpaRepository<DrillKpiEntity, Long> {

    /**
     * Find all DrillKpi records for a specific drill
     *
     * @param drillId The ID of the drill
     * @return List of DrillKpi records
     */
    List<DrillKpiEntity> findByDrillId(Long drillId);

    /**
     * Find all DrillKpi records for a specific KPI
     *
     * @param kpiId The ID of the KPI
     * @return List of DrillKpi records
     */
    List<DrillKpiEntity> findByKpiId(Long kpiId);

    /**
     * Find a specific DrillKpi record by drill and KPI IDs
     *
     * @param drillId The ID of the drill
     * @param kpiId The ID of the KPI
     * @return Optional containing the DrillKpi if found
     */
    Optional<DrillKpiEntity> findByDrillIdAndKpiId(Long drillId, Long kpiId);

    /**
     * Delete all DrillKpi records for a specific drill
     *
     * @param drillId The ID of the drill
     */
    void deleteByDrillId(Long drillId);

    /**
     * Delete all DrillKpi records for a specific KPI
     *
     * @param kpiId The ID of the KPI
     */
    void deleteByKpiId(Long kpiId);
}
