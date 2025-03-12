-- Positions table
CREATE TABLE positions (
                               position_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               sport_id BIGINT NOT NULL,
                               name VARCHAR(100) NOT NULL,
                               description VARCHAR(MAX),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                               CONSTRAINT FK_positions_sports FOREIGN KEY (sport_id) REFERENCES sports(sport_id)
);