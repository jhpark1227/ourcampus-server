ALTER TABLE facility DROP FOREIGN KEY fk_facility_school;

ALTER TABLE facility RENAME COLUMN school_id TO university_id;
ALTER TABLE facility RENAME INDEX idx_facility_school_id TO idx_facility_university_id;

ALTER TABLE facility ADD CONSTRAINT fk_facility_university
    FOREIGN KEY (university_id) REFERENCES university (id);