-- Practice Plans table (for grouping drills into practice plans)
CREATE TABLE practice_plans (
                                    practice_plan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL,
                                    description VARCHAR(MAX),
                                    sport_id BIGINT NOT NULL,
                                    total_duration_minutes INT,
                                    focus_area_id BIGINT,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                    CONSTRAINT FK_practice_plans_sports FOREIGN KEY (sport_id) REFERENCES sports(sport_id),
                                    CONSTRAINT FK_practice_plans_focus_areas FOREIGN KEY (focus_area_id) REFERENCES focus_areas(focus_area_id)
);