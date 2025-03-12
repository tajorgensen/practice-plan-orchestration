package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.FocusAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FocusAreaRepository extends JpaRepository<FocusAreaEntity, Long> {
    Optional<FocusAreaEntity> findByName(String name);
}
