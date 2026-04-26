CREATE TABLE IF NOT EXISTS hash_tag_facility_stat
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    university_id BIGINT   NOT NULL,
    hash_tag_id   BIGINT   NOT NULL,
    facility_id   BIGINT   NOT NULL,
    `rank`        INT      NOT NULL,
    updated_at    DATETIME NOT NULL,
    INDEX idx_hashtag_stats_university_hashtag (university_id, hash_tag_id),
    CONSTRAINT fk_hashtag_stats_university FOREIGN KEY (university_id) REFERENCES university (id),
    CONSTRAINT fk_hashtag_stats_hashtag FOREIGN KEY (hash_tag_id) REFERENCES hash_tag (id),
    CONSTRAINT fk_hashtag_stats_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
);
