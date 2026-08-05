ALTER TABLE review ADD COLUMN member_id BIGINT;
ALTER TABLE review ADD INDEX idx_review_member_id (member_id);
ALTER TABLE review ADD CONSTRAINT fk_review_member
    FOREIGN KEY (member_id) REFERENCES member (id);