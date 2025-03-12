-- Subcategories table
CREATE TABLE subcategories (
                                   subcategory_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   category_id BIGINT NOT NULL,
                                   name VARCHAR(100) NOT NULL,
                                   description VARCHAR(MAX),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                   CONSTRAINT FK_subcategories_categories FOREIGN KEY (category_id) REFERENCES categories(category_id)
);