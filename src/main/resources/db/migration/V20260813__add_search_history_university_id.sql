ALTER TABLE search_history ADD COLUMN university_id BIGINT;
ALTER TABLE search_history ADD INDEX idx_search_history_university_id (university_id);
ALTER TABLE search_history ADD CONSTRAINT fk_search_history_university
    FOREIGN KEY (university_id) REFERENCES university (id);