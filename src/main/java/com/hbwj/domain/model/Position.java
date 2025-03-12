package com.hbwj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    private Long id;
    private Long sportId;
    private String name;
    private String description;
    private String sportName; // For display purposes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
