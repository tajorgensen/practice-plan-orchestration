-- Practice Plan Drills (drills included in a practice plan with sequence)
CREATE TABLE practice_plan_drills (
                                  practice_plan_drill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  practice_plan_id BIGINT NOT NULL,
                                  drill_id BIGINT NOT NULL,
                                  sequence_order INT NOT NULL,
                                  duration_minutes INT,
                                  notes VARCHAR(MAX),
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                  CONSTRAINT FK_practice_plan_drills_practice_plans FOREIGN KEY (practice_plan_id) REFERENCES practice_plans(practice_plan_id),
                                  CONSTRAINT FK_practice_plan_drills_drills FOREIGN KEY (drill_id) REFERENCES drills(drill_id),
                                  CONSTRAINT UQ_practice_plan_drills UNIQUE(practice_plan_id, sequence_order)
);