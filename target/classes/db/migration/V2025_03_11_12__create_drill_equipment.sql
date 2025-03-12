-- Drill-Equipment relation (tracks which equipment is needed for each drill)
CREATE TABLE drill_equipment (
                                     drill_equipment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     drill_id BIGINT NOT NULL,
                                     equipment_id BIGINT NOT NULL,
                                     quantity INT DEFAULT 1,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                     CONSTRAINT FK_drill_equipment_drills FOREIGN KEY (drill_id) REFERENCES drills(drill_id),
                                     CONSTRAINT FK_drill_equipment_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
                                     CONSTRAINT UQ_drill_equipment UNIQUE(drill_id, equipment_id)
);