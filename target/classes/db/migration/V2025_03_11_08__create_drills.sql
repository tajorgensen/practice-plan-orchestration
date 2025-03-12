-- Drills table
CREATE TABLE drills (
                            drill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description VARCHAR(MAX),
                            instructions VARCHAR(MAX),
                            duration_minutes INT,
                            difficulty_level VARCHAR(20),
                            focus_area_id BIGINT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                            CONSTRAINT FK_drills_focus_areas FOREIGN KEY (focus_area_id) REFERENCES focus_areas(focus_area_id)
);