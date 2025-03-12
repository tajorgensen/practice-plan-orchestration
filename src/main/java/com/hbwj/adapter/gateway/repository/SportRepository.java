package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.SportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<SportEntity, Long> {
    Optional<SportEntity> findByName(String name);
}

