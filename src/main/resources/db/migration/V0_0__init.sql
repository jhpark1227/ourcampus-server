CREATE TABLE email_verification
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    email      varchar(255) NOT NULL,
    code       varchar(255) NOT NULL,
    type       enum ('REGISTER','PASSWORD_RESET') DEFAULT NULL,
    created_at datetime(6)  NOT NULL,
    updated_at datetime(6)  NOT NULL,
    deleted_at datetime(6)                        DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE faq
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6)                       DEFAULT NULL,
    question   varchar(255)                      DEFAULT NULL,
    answer     varchar(255)                      DEFAULT NULL,
    type       enum ('RESERVATION','JOIN','ETC') DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE hash_tag
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6)  DEFAULT NULL,
    name       varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE university
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6)  DEFAULT NULL,
    name       varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_university_name (name)
);

CREATE TABLE admin
(
    id            bigint                                 NOT NULL AUTO_INCREMENT,
    created_at    datetime(6)                            NOT NULL,
    updated_at    datetime(6)                            NOT NULL,
    deleted_at    datetime(6)                                     DEFAULT NULL,
    login_id      varchar(255)                           NOT NULL,
    name          varchar(255)                           NOT NULL,
    password      varchar(255)                           NOT NULL,
    status        enum ('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    university_id bigint                                 NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_login_id (login_id),
    KEY idx_admin_university_id (university_id),
    CONSTRAINT fk_admin_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE building
(
    id              bigint      NOT NULL AUTO_INCREMENT,
    created_at      datetime(6) NOT NULL,
    updated_at      datetime(6) NOT NULL,
    deleted_at      datetime(6)   DEFAULT NULL,
    label           varchar(255)  DEFAULT NULL,
    latitude        double      NOT NULL,
    longitude       double      NOT NULL,
    name            varchar(255)  DEFAULT NULL,
    thumbnail_image varchar(2048) DEFAULT NULL,
    university_id   bigint        DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_building_university_id (university_id),
    CONSTRAINT fk_building_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE department
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6)  DEFAULT NULL,
    name          varchar(255) DEFAULT NULL,
    university_id bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_department_university_id (university_id),
    CONSTRAINT fk_department_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE notice
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6)                        DEFAULT NULL,
    content       text,
    title         varchar(255)                       DEFAULT NULL,
    type          enum ('GENERAL','RECRUIT','EVENT') DEFAULT NULL,
    university_id bigint                             DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_notice_university_id (university_id),
    CONSTRAINT fk_notice_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE popular_keyword_stat
(
    id            bigint       NOT NULL AUTO_INCREMENT,
    university_id bigint       NOT NULL,
    keyword       varchar(255) NOT NULL,
    `rank`        int          NOT NULL,
    updated_at    datetime(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_popular_keyword_stat_university_rank (university_id, `rank`),
    CONSTRAINT fk_popular_keyword_stat_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE search_history
(
    id            bigint       NOT NULL AUTO_INCREMENT,
    keyword       varchar(255) NOT NULL,
    created_at    datetime(6)  NOT NULL,
    updated_at    datetime(6)  NOT NULL,
    deleted_at    datetime(6) DEFAULT NULL,
    university_id bigint       NOT NULL,
    PRIMARY KEY (id),
    KEY idx_search_history_university_id (university_id),
    CONSTRAINT fk_search_history_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE theme
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6)  DEFAULT NULL,
    name          varchar(255) DEFAULT NULL,
    university_id bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_theme_university_id (university_id),
    CONSTRAINT fk_theme_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE usage_status
(
    id             bigint      NOT NULL AUTO_INCREMENT,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) NOT NULL,
    deleted_at     datetime(6)  DEFAULT NULL,
    total_seats    int          DEFAULT NULL,
    occupied_seats int          DEFAULT NULL,
    facility_name  varchar(255) DEFAULT NULL,
    university_id  bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_usage_status_university_id (university_id),
    CONSTRAINT fk_usage_status_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE building_images
(
    building_id bigint NOT NULL,
    images      varchar(2048) DEFAULT NULL,
    KEY idx_building_images_building_id (building_id),
    CONSTRAINT fk_building_images_building FOREIGN KEY (building_id) REFERENCES building (id)
);

CREATE TABLE building_operation_times
(
    building_id bigint NOT NULL,
    name        varchar(255) DEFAULT NULL,
    start_time  int          DEFAULT NULL,
    end_time    int          DEFAULT NULL,
    KEY idx_building_operation_times_building_id (building_id),
    CONSTRAINT fk_building_operation_times_building FOREIGN KEY (building_id) REFERENCES building (id)
);

CREATE TABLE facility
(
    id              bigint      NOT NULL AUTO_INCREMENT,
    created_at      datetime(6) NOT NULL,
    updated_at      datetime(6) NOT NULL,
    deleted_at      datetime(6)                                            DEFAULT NULL,
    category        enum ('STUDY','SEMINAR','SPORTS','CULTURE','PRINT_PC') DEFAULT NULL,
    caution         varchar(255)                                           DEFAULT NULL,
    description     varchar(255)                                           DEFAULT NULL,
    equipment       varchar(255)                                           DEFAULT NULL,
    location        varchar(255)                                           DEFAULT NULL,
    name            varchar(255)                                           DEFAULT NULL,
    purpose         varchar(255)                                           DEFAULT NULL,
    thumbnail_image varchar(2048)                                          DEFAULT NULL,
    reservable      tinyint(1)                                             DEFAULT NULL,
    building_id     bigint                                                 DEFAULT NULL,
    university_id   bigint                                                 DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_facility_building_id (building_id),
    KEY idx_facility_university_id (university_id),
    CONSTRAINT fk_facility_building FOREIGN KEY (building_id) REFERENCES building (id),
    CONSTRAINT fk_facility_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE member
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) NOT NULL,
    deleted_at    datetime(6)   DEFAULT NULL,
    email         varchar(255)  DEFAULT NULL,
    name          varchar(255)  DEFAULT NULL,
    password      varchar(255)  DEFAULT NULL,
    profile_image varchar(2048) DEFAULT NULL,
    student_id    varchar(255)  DEFAULT NULL,
    university_id bigint        DEFAULT NULL,
    department_id bigint        DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_email (email),
    KEY idx_member_university_id (university_id),
    KEY idx_member_department_id (department_id),
    CONSTRAINT fk_member_department FOREIGN KEY (department_id) REFERENCES department (id),
    CONSTRAINT fk_member_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE facility_available_times
(
    facility_id bigint NOT NULL,
    start_time  int DEFAULT NULL,
    end_time    int DEFAULT NULL,
    KEY idx_facility_available_times_facility_id (facility_id),
    CONSTRAINT fk_facility_available_times_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
);

CREATE TABLE facility_images
(
    facility_id bigint NOT NULL,
    images      varchar(2048) DEFAULT NULL,
    KEY idx_facility_images_facility_id (facility_id),
    CONSTRAINT fk_facility_images_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
);

CREATE TABLE facility_operation_times
(
    facility_id bigint NOT NULL,
    name        varchar(255) DEFAULT NULL,
    start_time  int          DEFAULT NULL,
    end_time    int          DEFAULT NULL,
    KEY idx_facility_operation_times_facility_id (facility_id),
    CONSTRAINT fk_facility_operation_times_facility FOREIGN KEY (facility_id) REFERENCES facility (id)
);

CREATE TABLE facility_theme
(
    id          bigint NOT NULL AUTO_INCREMENT,
    facility_id bigint DEFAULT NULL,
    theme_id    bigint DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_facility_theme_facility_id (facility_id),
    KEY idx_facility_theme_theme_id (theme_id),
    CONSTRAINT fk_facility_theme_facility FOREIGN KEY (facility_id) REFERENCES facility (id),
    CONSTRAINT fk_facility_theme_theme FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE TABLE hash_tag_facility_stat
(
    id            bigint   NOT NULL AUTO_INCREMENT,
    university_id bigint   NOT NULL,
    hash_tag_id   bigint   NOT NULL,
    facility_id   bigint   NOT NULL,
    `rank`        int      NOT NULL,
    updated_at    datetime NOT NULL,
    PRIMARY KEY (id),
    KEY idx_hashtag_stats_university_hashtag (university_id, hash_tag_id),
    KEY fk_hashtag_stats_hashtag (hash_tag_id),
    KEY fk_hashtag_stats_facility (facility_id),
    CONSTRAINT fk_hashtag_stats_facility FOREIGN KEY (facility_id) REFERENCES facility (id),
    CONSTRAINT fk_hashtag_stats_hashtag FOREIGN KEY (hash_tag_id) REFERENCES hash_tag (id),
    CONSTRAINT fk_hashtag_stats_university FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE inquiry
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6)                 DEFAULT NULL,
    title      varchar(255)                DEFAULT NULL,
    content    varchar(255)                DEFAULT NULL,
    status     enum ('PENDING','ANSWERED') DEFAULT NULL,
    member_id  bigint                      DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_inquiry_member_id (member_id),
    CONSTRAINT fk_inquiry_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE live_talk
(
    id          bigint      NOT NULL AUTO_INCREMENT,
    created_at  datetime(6) NOT NULL,
    updated_at  datetime(6) NOT NULL,
    deleted_at  datetime(6)  DEFAULT NULL,
    message     varchar(255) DEFAULT NULL,
    member_id   bigint       DEFAULT NULL,
    building_id bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_live_talk_member_id (member_id),
    KEY idx_live_talk_building_id (building_id),
    CONSTRAINT fk_live_talk_building FOREIGN KEY (building_id) REFERENCES building (id),
    CONSTRAINT fk_live_talk_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE refresh_token
(
    id        bigint NOT NULL AUTO_INCREMENT,
    value     varchar(255) DEFAULT NULL,
    member_id bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_refresh_token_member_id (member_id),
    CONSTRAINT fk_refresh_token_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE reservation
(
    id          bigint                       NOT NULL AUTO_INCREMENT,
    created_at  datetime(6)                  NOT NULL,
    updated_at  datetime(6)                  NOT NULL,
    deleted_at  datetime(6) DEFAULT NULL,
    head_count  int                          NOT NULL,
    status      enum ('RESERVED','RETURNED') NOT NULL,
    end_time    datetime(6) DEFAULT NULL,
    start_time  datetime(6) DEFAULT NULL,
    facility_id bigint      DEFAULT NULL,
    member_id   bigint      DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_reservation_facility_id (facility_id),
    KEY idx_reservation_member_id (member_id),
    CONSTRAINT fk_reservation_facility FOREIGN KEY (facility_id) REFERENCES facility (id),
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE alarm
(
    id             bigint      NOT NULL AUTO_INCREMENT,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) NOT NULL,
    deleted_at     datetime(6)                                                                              DEFAULT NULL,
    alarm_timing   enum ('THREE_DAYS_BEFORE','ONE_DAY_BEFORE','THIRTY_MINUTES_BEFORE','TEN_MINUTES_BEFORE') DEFAULT NULL,
    title          varchar(255)                                                                             DEFAULT NULL,
    message        varchar(255)                                                                             DEFAULT NULL,
    scheduled_time datetime(6)                                                                              DEFAULT NULL,
    checked        bit(1)      NOT NULL,
    dtype          varchar(30)                                                                              DEFAULT NULL,
    member_id      bigint                                                                                   DEFAULT NULL,
    reservation_id bigint                                                                                   DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_alarm_reservation_id (reservation_id),
    KEY idx_alarm_member_id (member_id),
    CONSTRAINT fk_alarm_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_alarm_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id)
);

CREATE TABLE inquiry_answer
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) NOT NULL,
    deleted_at datetime(6)  DEFAULT NULL,
    title      varchar(255) DEFAULT NULL,
    content    varchar(255) DEFAULT NULL,
    inquiry_id bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_inquiry_answer_inquiry_id (inquiry_id),
    CONSTRAINT fk_inquiry_answer_inquiry FOREIGN KEY (inquiry_id) REFERENCES inquiry (id)
);

CREATE TABLE reservation_images
(
    reservation_id bigint NOT NULL,
    images         varchar(2048) DEFAULT NULL,
    KEY idx_reservation_images_reservation_id (reservation_id),
    CONSTRAINT fk_reservation_images_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id)
);

CREATE TABLE review
(
    id             bigint      NOT NULL AUTO_INCREMENT,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) NOT NULL,
    deleted_at     datetime(6)  DEFAULT NULL,
    content        varchar(255) DEFAULT NULL,
    start_rating   int          DEFAULT NULL,
    reservation_id bigint       DEFAULT NULL,
    facility_id    bigint      NOT NULL,
    member_id      bigint      NOT NULL,
    PRIMARY KEY (id),
    KEY idx_review_reservation_id (reservation_id),
    KEY idx_review_facility_id (facility_id),
    KEY idx_review_member_id (member_id),
    CONSTRAINT fk_review_facility FOREIGN KEY (facility_id) REFERENCES facility (id),
    CONSTRAINT fk_review_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_review_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id)
);

CREATE TABLE hash_tag_review
(
    review_id   bigint NOT NULL,
    hash_tag_id bigint NOT NULL,
    PRIMARY KEY (hash_tag_id, review_id),
    KEY idx_hash_tag_review_review_id (review_id),
    CONSTRAINT fk_hash_tag_review_hash_tag FOREIGN KEY (hash_tag_id) REFERENCES hash_tag (id),
    CONSTRAINT fk_hash_tag_review_review FOREIGN KEY (review_id) REFERENCES review (id)
);

CREATE TABLE review_images
(
    review_id bigint NOT NULL,
    images    varchar(2048) DEFAULT NULL,
    KEY idx_review_images_review_id (review_id),
    CONSTRAINT fk_review_images_review FOREIGN KEY (review_id) REFERENCES review (id)
);
