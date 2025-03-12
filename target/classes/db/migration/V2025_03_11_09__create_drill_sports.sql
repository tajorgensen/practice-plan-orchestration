-- Drill-Sport relation (which sports a drill applies to)
CREATE TABLE drill_sports (
                                  drill_sport_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  drill_id BIGINT NOT NULL,
                                  sport_id BIGINT NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                  CONSTRAINT FK_drill_sports_drills FOREIGN KEY (drill_id) REFERENCES drills(drill_id),
                                  CONSTRAINT FK_drill_sports_sports FOREIGN KEY (sport_id) REFERENCES sports(sport_id),
                                  CONSTRAINT UQ_drill_sports UNIQUE(drill_id, sport_id)
);