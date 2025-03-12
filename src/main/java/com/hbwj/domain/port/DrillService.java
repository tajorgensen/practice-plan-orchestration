package com.hbwj.domain.port;

import com.hbwj.domain.model.Drill;
import com.hbwj.domain.model.DrillDetail;
import com.hbwj.domain.model.DrillEquipment;

import java.util.List;

public interface DrillService {
    List<Drill> getAllDrills();

    List<Drill> getDrillsByFocusAreaId(Long focusAreaId);

    List<Drill> getDrillsBySportId(Long sportId);

    List<Drill> getDrillsByPositionId(Long positionId);

    List<Drill> getDrillsByKpiId(Long kpiId);

    List<Drill> getDrillsBySportIdAndFocusAreaId(Long sportId, Long focusAreaId);

    Drill getDrillById(Long id);

    DrillDetail getDrillDetailsById(Long id);

    Drill createDrill(Drill drill);

    Drill updateDrill(Long id, Drill drill);

    void deleteDrill(Long id);

    void addEquipmentToDrill(Long drillId, DrillEquipment drillEquipment);

    void removeEquipmentFromDrill(Long drillId, Long equipmentId);
}
