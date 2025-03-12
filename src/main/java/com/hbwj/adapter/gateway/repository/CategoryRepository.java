package com.hbwj.adapter.gateway.repository;

import com.hbwj.adapter.gateway.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findBySportId(Long sportId);

    List<CategoryEntity> findByFocusAreaId(Long focusAreaId);

    List<CategoryEntity> findBySportIdAndFocusAreaId(Long sportId, Long focusAreaId);

    Optional<CategoryEntity> findByNameAndSportIdAndFocusAreaId(String name, Long sportId, Long focusAreaId);
}
