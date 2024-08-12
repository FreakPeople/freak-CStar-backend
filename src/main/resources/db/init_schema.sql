CREATE TABLE IF NOT EXISTS game_quiz
(
    game_quiz_id BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    game_id      BIGINT NOT NULL,
    quiz_id      BIGINT NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    deleted_at   TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS room
(
    room_id    BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS room_join
(
    room_join_id BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    room_id      BIGINT NOT NULL,
    member_id    BIGINT NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    deleted_at   TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS quiz
(
    quiz_id    BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    question   BLOB NOT NULL,
    answer     VARCHAR(255) NOT NULL,
    category   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS game
(
    game_id    BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    room_id    BIGINT NOT NULL,
    started_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS member_game_result
(
    member_game_result_id BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    game_id               BIGINT NOT NULL,
    member_id             BIGINT NOT NULL,
    is_win                BOOLEAN NOT NULL,
    correct_count         INT NOT NULL,
    total_count           INT NOT NULL,
    created_at            TIMESTAMP NOT NULL,
    updated_at            TIMESTAMP NOT NULL,
    deleted_at            TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS member
(
    member_id  BIGINT UNSIGNED NOT NULL auto_increment PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);