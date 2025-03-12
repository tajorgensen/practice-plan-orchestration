-- Focus areas (Fundamentals, Offense, Defense)
CREATE TABLE focus_areas (
                                 focus_area_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL,
                                 description VARCHAR(MAX),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);
