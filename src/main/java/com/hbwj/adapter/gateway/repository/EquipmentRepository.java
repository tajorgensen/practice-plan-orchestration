package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    Optional<EquipmentEntity> findByName(String name);
}
