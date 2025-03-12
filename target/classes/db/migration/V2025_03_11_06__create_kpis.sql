-- Key Performance Indicators (KPIs)
CREATE TABLE kpis (
                          kpi_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          subcategory_id BIGINT NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(MAX),
                          measurement_unit VARCHAR(50),
                          target_value DECIMAL(10,2),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                          CONSTRAINT FK_kpis_subcategories FOREIGN KEY (subcategory_id) REFERENCES subcategories(subcategory_id)
);