-- Drill-KPI relation (which KPIs a drill improves)
CREATE TABLE drill_kpis (
                                drill_kpi_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                drill_id BIGINT NOT NULL,
                                kpi_id BIGINT NOT NULL,
                                impact_level INT, -- Scale of 1-10 for how much this drill impacts this KPI
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                                CONSTRAINT FK_drill_kpis_drills FOREIGN KEY (drill_id) REFERENCES drills(drill_id),
                                CONSTRAINT FK_drill_kpis_kpis FOREIGN KEY (kpi_id) REFERENCES kpis(kpi_id),
                                CONSTRAINT UQ_drill_kpis UNIQUE(drill_id, kpi_id)
);