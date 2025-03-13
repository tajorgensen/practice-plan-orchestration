package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
    List<PositionEntity> findBySportId(Long sportId);

    Optional<PositionEntity> findByNameAndSportId(String name, Long sportId);

    List<PositionEntity> findBySportIdAndPositionType(Long sportId, String positionType);

    @Query("SELECT p FROM PositionEntity p WHERE p.sport.id = :sportId AND (p.positionType = :positionType OR p.positionType = 'BOTH')")
    List<PositionEntity> findBySportIdAndPositionTypeIncludingBoth(@Param("sportId") Long sportId, @Param("positionType") String positionType);
}
