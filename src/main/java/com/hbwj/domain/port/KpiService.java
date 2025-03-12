package com.hbwj.domain.port;

import com.hbwj.domain.model.Kpi;

import java.util.List;

public interface KpiService {
    List<Kpi> getAllKpis();

    List<Kpi> getKpisBySubCategoryId(Long subcategoryId);

    List<Kpi> getKpisBySportId(Long sportId);

    List<Kpi> getKpisByFocusAreaId(Long focusAreaId);

    Kpi getKpiById(Long id);

    Kpi createKpi(Kpi kpi);

    Kpi updateKpi(Long id, Kpi kpi);

    void deleteKpi(Long id);
}
