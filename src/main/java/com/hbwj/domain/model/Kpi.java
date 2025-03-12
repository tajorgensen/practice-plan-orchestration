package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kpi {

    private Long id;
    private Long subCategoryId;
    private String name;
    private String description;
    private String measurementUnit;
    private BigDecimal targetValue;
    private String subCategoryName; // For display purposes
    private String categoryName; // For display purposes
    private String sportName; // For display purposes
    private String focusAreaName; // For display purposes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
