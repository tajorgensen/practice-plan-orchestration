package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.PracticePlanDrillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticePlanDrillRepository extends JpaRepository<PracticePlanDrillEntity, Long> {
    List<PracticePlanDrillEntity> findByPracticePlanId(Long practicePlanId);

    List<PracticePlanDrillEntity> findByDrillId(Long drillId);

    List<PracticePlanDrillEntity> findByPracticePlanIdOrderBySequenceOrderAsc(Long practicePlanId);
}
