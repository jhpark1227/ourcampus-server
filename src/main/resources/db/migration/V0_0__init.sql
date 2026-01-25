CREATE TABLE IF NOT EXISTS university
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6),
    name       varchar(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_university_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS department
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6),
    name          varchar(255),
    university_id bigint,
    PRIMARY KEY (id),
    UNIQUE KEY uk_department_name (name),
    KEY idx_building_university_id (university_id),
    CONSTRAINT fk_department_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS building
(
    id              bigint      NOT NULL AUTO_INCREMENT,
    created_at      datetime(6) NOT NULL,
    updated_at      datetime(6) NOT NULL,
    deleted_at      datetime(6),
    label           varchar(255),
    latitude        double      NOT NULL,
    longitude       double      NOT NULL,
    name            varchar(255),
    thumbnail_image varchar(2048),
    university_id   bigint,

    PRIMARY KEY (id),
    KEY idx_building_university_id (university_id),
    CONSTRAINT fk_building_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS building_images
(
    building_id bigint NOT NULL,
    images      varchar(2048),
    KEY idx_building_images_building_id (building_id),
    CONSTRAINT fk_building_images_building FOREIGN KEY (building_id) REFERENCES building (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS building_operation_times
(
    building_id bigint NOT NULL,
    name        varchar(255),
    start_time  int,
    end_time    int,
    KEY idx_building_operation_times_building_id (building_id),
    CONSTRAINT fk_building_operation_times_building FOREIGN KEY (building_id) REFERENCES building (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS facility
(
    id              bigint      NOT NULL AUTO_INCREMENT,
    created_at      datetime(6) NOT NULL,
    updated_at      datetime(6) NOT NULL,
    deleted_at      datetime(6),
    category        enum ('STUDY','SEMINAR','SPORTS','CULTURE','PRINT_PC'),
    caution         varchar(255),
    description     varchar(255),
    equipment       varchar(255),
    location        varchar(255),
    name            varchar(255),
    purpose         varchar(255),
    thumbnail_image varchar(2048),
    reservable      boolean,
    building_id     bigint,
    school_id       bigint,
    PRIMARY KEY (id),
    KEY idx_facility_building_id (building_id),
    KEY idx_facility_school_id (school_id),
    CONSTRAINT fk_facility_school FOREIGN KEY (school_id) REFERENCES university (id),
    CONSTRAINT fk_facility_building FOREIGN KEY (building_id) REFERENCES building (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS usage_status
(
    id             bigint      NOT NULL AUTO_INCREMENT,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) NOT NULL,
    deleted_at     datetime(6),
    total_seats    int,
    occupied_seats int,
    facility_name  varchar(255),
    university_id  bigint,
    PRIMARY KEY (id),
    KEY idx_usage_status_university_id (university_id),
    CONSTRAINT fk_usage_status_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS facility_images
(
    facility_id bigint NOT NULL,
    images      varchar(2048),
    KEY idx_facility_images_facility_id (facility_id),
    CONSTRAINT fk_facility_images_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS facility_available_times
(
    facility_id bigint NOT NULL,
    start_time  int,
    end_time    int,
    KEY idx_facility_available_times_facility_id (facility_id),
    CONSTRAINT fk_facility_available_times_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS facility_operation_times
(
    facility_id bigint NOT NULL,
    name        varchar(255),
    start_time  int,
    end_time    int,
    KEY idx_facility_operation_times_facility_id (facility_id),
    CONSTRAINT fk_facility_operation_times_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS faq
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6),
    question   varchar(255),
    answer     varchar(255),
    type       enum ('RESERVATION','JOIN','ETC'),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS hash_tag
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6),
    name       varchar(255),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS theme
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6),
    name          varchar(255),
    university_id bigint,
    PRIMARY KEY (id),
    KEY idx_theme_university_id (university_id),
    CONSTRAINT fk_theme_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS facility_theme
(
    id          bigint NOT NULL AUTO_INCREMENT,
    facility_id bigint,
    theme_id    bigint,
    PRIMARY KEY (id),
    KEY idx_facility_theme_facility_id (facility_id),
    KEY idx_facility_theme_theme_id (theme_id),
    CONSTRAINT fk_facility_theme_theme FOREIGN KEY (theme_id) REFERENCES theme (id),
    CONSTRAINT fk_facility_theme_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS member
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6),
    email         varchar(255),
    name          varchar(255),
    password      varchar(255),
    profile_image varchar(2048),
    student_id    varchar(255),
    university_id bigint,
    department_id bigint,
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_email (email),
    UNIQUE KEY uk_member_student_id (student_id),
    KEY idx_member_university_id (university_id),
    KEY idx_member_department_id (department_id),
    CONSTRAINT fk_member_university FOREIGN KEY (university_id) REFERENCES university (id),
    CONSTRAINT fk_member_department FOREIGN KEY (department_id) REFERENCES department (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS inquiry
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6),
    title      varchar(255),
    content    varchar(255),
    status     enum ('PENDING', 'ANSWERED'),
    member_id  bigint,
    PRIMARY KEY (id),
    KEY idx_inquiry_member_id (member_id),
    CONSTRAINT fk_inquiry_member FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS inquiry_answer
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6),
    title      varchar(255),
    content    varchar(255),
    inquiry_id bigint,
    PRIMARY KEY (id),
    KEY idx_inquiry_answer_inquiry_id (inquiry_id),
    CONSTRAINT fk_inquiry_answer_inquiry FOREIGN KEY (inquiry_id) REFERENCES inquiry (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS live_talk
(
    id          bigint      NOT NULL AUTO_INCREMENT,
    created_at  datetime(6) NOT NULL,
    updated_at  datetime(6) NOT NULL,
    deleted_at  datetime(6),
    message     varchar(255),
    member_id   bigint,
    facility_id bigint,
    PRIMARY KEY (id),
    KEY idx_live_talk_member_id (member_id),
    KEY idx_live_talk_facility_id (facility_id),
    CONSTRAINT fk_live_talk_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_live_talk_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS notice
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6),
    content       text,
    title         varchar(255),
    type          enum ('GENERAL','RECRUIT','EVENT'),
    university_id bigint,
    PRIMARY KEY (id),
    KEY idx_notice_university_id (university_id),
    CONSTRAINT fk_notice_university FOREIGN KEY (university_id) REFERENCES university (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS refresh_token
(
    id        bigint NOT NULL AUTO_INCREMENT,
    value     varchar(255),
    member_id bigint,
    PRIMARY KEY (id),
    KEY idx_refresh_token_member_id (member_id),
    CONSTRAINT fk_refresh_token_member FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS reservation
(
    id          bigint                        NOT NULL AUTO_INCREMENT,
    created_at  datetime(6)                   NOT NULL,
    updated_at  datetime(6)                   NOT NULL,
    deleted_at  datetime(6),
    head_count  int                           NOT NULL,
    status      enum ('RESERVED', 'RETURNED') NOT NULL,
    end_time    datetime(6),
    start_time  datetime(6),
    facility_id bigint,
    member_id   bigint,
    PRIMARY KEY (id),
    KEY idx_reservation_facility_id (facility_id),
    KEY idx_reservation_member_id (member_id),
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_reservation_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS alarm
(
    id             bigint      NOT NULL AUTO_INCREMENT,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) NOT NULL,
    deleted_at     datetime(6),
    alarm_timing   enum ('THREE_DAYS_BEFORE','ONE_DAY_BEFORE','THIRTY_MINUTES_BEFORE','TEN_MINUTES_BEFORE'),
    title          varchar(255),
    message        varchar(255),
    scheduled_time datetime(6),
    checked        bit(1)      NOT NULL,
    dtype          varchar(30),
    member_id      bigint,
    reservation_id bigint,
    PRIMARY KEY (id),
    KEY idx_alarm_reservation_id (reservation_id),
    KEY idx_alarm_member_id (reservation_id),
    CONSTRAINT fk_alarm_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id),
    CONSTRAINT fk_alarm_member FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS reservation_images
(
    reservation_id bigint NOT NULL,
    images         varchar(2048),
    KEY idx_reservation_images_reservation_id (reservation_id),
    CONSTRAINT fk_reservation_images_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS review
(
    id             bigint      NOT NULL AUTO_INCREMENT,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) NOT NULL,
    deleted_at     datetime(6),
    content        varchar(255),
    start_rating   int,
    reservation_id bigint,
    PRIMARY KEY (id),
    KEY idx_review_reservation_id (reservation_id),
    CONSTRAINT fk_review_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS review_images
(
    review_id bigint NOT NULL,
    images    varchar(2048),
    KEY idx_review_images_review_id (review_id),
    CONSTRAINT fk_review_images_review FOREIGN KEY (review_id) REFERENCES review (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS hash_tag_review
(
    id          bigint NOT NULL AUTO_INCREMENT,
    review_id   bigint,
    hash_tag_id bigint,
    PRIMARY KEY (id),
    KEY idx_hash_tag_review_review_id (review_id),
    KEY idx_hash_tag_review_hash_tag_id (hash_tag_id),
    CONSTRAINT fk_hash_tag_review_hash_tag FOREIGN KEY (hash_tag_id) REFERENCES hash_tag (id),
    CONSTRAINT fk_hash_tag_review_review FOREIGN KEY (review_id) REFERENCES review (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS search_history
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    keyword    varchar(255) NOT NULL,
    member_id  bigint,
    created_at datetime(6)  NOT NULL,
    updated_at datetime(6)  NOT NULL,
    deleted_at datetime(6),
    PRIMARY KEY (id),
    KEY idx_search_history_member_id (member_id),
    CONSTRAINT fk_search_history_member FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS email_verification
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    email      varchar(255) NOT NULL,
    code       varchar(255) NOT NULL,
    type       enum ('REGISTER', 'PASSWORD_RESET'),
    created_at datetime(6)  NOT NULL,
    updated_at datetime(6)  NOT NULL,
    deleted_at datetime(6),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;