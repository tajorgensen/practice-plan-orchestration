package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
    List<PositionEntity> findBySportId(Long sportId);

    Optional<PositionEntity> findByNameAndSportId(String name, Long sportId);
}
