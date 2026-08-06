ALTER TABLE search_history MODIFY COLUMN university_id BIGINT NOT NULL;
ALTER TABLE search_history DROP FOREIGN KEY fk_search_history_member;
ALTER TABLE search_history DROP COLUMN member_id;