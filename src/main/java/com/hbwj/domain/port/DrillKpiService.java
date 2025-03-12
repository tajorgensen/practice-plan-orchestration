package com.hbwj.domain.port;

import com.hbwj.domain.model.DrillKpi;

import java.util.List;

public interface DrillKpiService {
    List<DrillKpi> getAllDrillKpis();

    List<DrillKpi> getDrillKpisByDrillId(Long drillId);

    List<DrillKpi> getDrillKpisByKpiId(Long kpiId);

    DrillKpi getDrillKpiById(Long id);

    DrillKpi createDrillKpi(DrillKpi drillKpi);

    DrillKpi updateDrillKpi(Long id, DrillKpi drillKpi);

    void deleteDrillKpi(Long id);
}
