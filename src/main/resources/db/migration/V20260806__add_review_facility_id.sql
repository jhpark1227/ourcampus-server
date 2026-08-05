ALTER TABLE review ADD COLUMN facility_id BIGINT;
ALTER TABLE review ADD INDEX idx_review_facility_id (facility_id);
ALTER TABLE review ADD CONSTRAINT fk_review_facility
    FOREIGN KEY (facility_id) REFERENCES facility (id);