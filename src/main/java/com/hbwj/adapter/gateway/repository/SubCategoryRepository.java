package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {
    List<SubCategoryEntity> findByCategoryId(Long categoryId);

    Optional<SubCategoryEntity> findByNameAndCategoryId(String name, Long categoryId);
}
