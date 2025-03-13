-- Create a new migration file src/main/resources/db/migration/V2025_03_12_01__add_position_type.sql
ALTER TABLE positions
    ADD COLUMN position_type VARCHAR(20) NOT NULL DEFAULT 'BOTH';

-- Update existing positions with appropriate position types
-- For football
UPDATE positions SET position_type = 'OFFENSE' WHERE name IN ('Quarterback', 'Running Back', 'Wide Receiver', 'Tight End', 'Offensive Line');
UPDATE positions SET position_type = 'DEFENSE' WHERE name IN ('Defensive Line', 'Linebacker', 'Cornerback', 'Safety');
UPDATE positions SET position_type = 'SPECIAL_TEAMS' WHERE name IN ('Kicker', 'Punter', 'Long Snapper');

-- For basketball (all positions play both offense and defense)
UPDATE positions SET position_type = 'BOTH' WHERE sport_id = 1;