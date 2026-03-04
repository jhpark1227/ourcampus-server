CREATE TABLE IF NOT EXISTS admin
(
    id            bigint                                NOT NULL AUTO_INCREMENT,
    created_at    datetime(6)                           NOT NULL,
    updated_at    datetime(6)                           NOT NULL,
    deleted_at    datetime(6),
    login_id      varchar(255)                          NOT NULL,
    name          varchar(255)                          NOT NULL,
    password      varchar(255)                          NOT NULL,
    status        enum ('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    university_id bigint NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_login_id (login_id),
    KEY idx_admin_university_id (university_id),
    CONSTRAINT fk_admin_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;