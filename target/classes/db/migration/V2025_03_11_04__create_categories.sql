-- Categories table
CREATE TABLE categories (
                                category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                sport_id BIGINT NOT NULL,
                                focus_area_id BIGINT NOT NULL,
                                name VARCHAR(100) NOT NULL,
                                description VARCHAR(MAX),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                CONSTRAINT FK_categories_sports FOREIGN KEY (sport_id) REFERENCES sports(sport_id),
                                CONSTRAINT FK_categories_focus_areas FOREIGN KEY (focus_area_id) REFERENCES focus_areas(focus_area_id)
);
