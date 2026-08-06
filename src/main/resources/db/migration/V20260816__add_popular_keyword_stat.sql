CREATE TABLE IF NOT EXISTS popular_keyword_stat
(
    id            bigint       NOT NULL AUTO_INCREMENT,
    university_id bigint       NOT NULL,
    keyword       varchar(255) NOT NULL,
    `rank`        int          NOT NULL,
    updated_at    datetime(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_popular_keyword_stat_university_rank (university_id, `rank`),
    CONSTRAINT fk_popular_keyword_stat_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;