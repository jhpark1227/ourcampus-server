DELETE FROM live_talk;

ALTER TABLE live_talk DROP FOREIGN KEY fk_live_talk_facility;
ALTER TABLE live_talk DROP KEY idx_live_talk_facility_id;
ALTER TABLE live_talk DROP COLUMN facility_id;

ALTER TABLE live_talk ADD COLUMN building_id BIGINT;
ALTER TABLE live_talk ADD KEY idx_live_talk_building_id (building_id);
ALTER TABLE live_talk ADD CONSTRAINT fk_live_talk_building
    FOREIGN KEY (building_id) REFERENCES building (id);