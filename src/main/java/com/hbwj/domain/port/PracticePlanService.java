package com.hbwj.domain.port;

import com.hbwj.domain.model.PracticePlan;
import com.hbwj.domain.model.PracticePlanDrill;
import com.hbwj.domain.model.PracticePlanEquipmentSummary;

import java.util.List;

public interface PracticePlanService {
    List<PracticePlan> getAllPracticePlans();

    List<PracticePlan> getPracticePlansBySportId(Long sportId);

    List<PracticePlan> getPracticePlansByFocusAreaId(Long focusAreaId);

    List<PracticePlan> getPracticePlansBySportIdAndFocusAreaId(Long sportId, Long focusAreaId);

    PracticePlan getPracticePlanById(Long id);

    PracticePlan createPracticePlan(PracticePlan practicePlan);

    PracticePlan updatePracticePlan(Long id, PracticePlan practicePlan);

    void deletePracticePlan(Long id);

    void addDrillToPracticePlan(Long practicePlanId, PracticePlanDrill practicePlanDrill);

    void removeDrillFromPracticePlan(Long practicePlanId, Long practicePlanDrillId);

    void reorderDrillsInPracticePlan(Long practicePlanId, List<PracticePlanDrill> orderedDrills);

    List<PracticePlanEquipmentSummary> getEquipmentSummaryForPracticePlan(Long practicePlanId);
}
