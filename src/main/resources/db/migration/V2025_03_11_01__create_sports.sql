-- Sports table
CREATE TABLE sports (
                        sport_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        description VARCHAR(MAX),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);