-- Drill-Position relation (which positions a drill is for)
CREATE TABLE drill_positions (
                                     drill_position_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     drill_id BIGINT NOT NULL,
                                     position_id BIGINT NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                     CONSTRAINT FK_drill_positions_drills FOREIGN KEY (drill_id) REFERENCES drills(drill_id),
                                     CONSTRAINT FK_drill_positions_positions FOREIGN KEY (position_id) REFERENCES positions(position_id),
                                     CONSTRAINT UQ_drill_positions UNIQUE(drill_id, position_id)
);