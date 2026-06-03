ALTER TABLE hash_tag_review
    DROP PRIMARY KEY,
    DROP COLUMN id,
    MODIFY COLUMN hash_tag_id bigint NOT NULL,
    MODIFY COLUMN review_id   bigint NOT NULL,
    DROP INDEX idx_hash_tag_review_hash_tag_id,
    ADD PRIMARY KEY (hash_tag_id, review_id);