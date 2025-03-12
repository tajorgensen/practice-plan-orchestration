package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.KpiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KpiRepository extends JpaRepository<KpiEntity, Long> {
    List<KpiEntity> findBySubCategoryId(Long subcategoryId);

    @Query("SELECT k FROM KpiEntity k " +
            "JOIN k.subCategory s " +
            "JOIN s.category c " +
            "WHERE c.sport.id = :sportId")
    List<KpiEntity> findBySportId(@Param("sportId") Long sportId);

    @Query("SELECT k FROM KpiEntity k " +
            "JOIN k.subCategory s " +
            "JOIN s.category c " +
            "WHERE c.focusArea.id = :focusAreaId")
    List<KpiEntity> findByFocusAreaId(@Param("focusAreaId") Long focusAreaId);
}
