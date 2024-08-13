CREATE TABLE IF NOT EXISTS game_quiz (
    game_id BIGINT NOT NULL COMMENT '게임 ID',
    quiz_id BIGINT NOT NULL COMMENT '퀴즈 ID'
);

CREATE TABLE IF NOT EXISTS room (
    room_id       BIGINT NOT NULL auto_increment PRIMARY KEY COMMENT '퀴즈방 ID',
    max_capacity  INT NOT NULL COMMENT '수용 가능 인원수',
    curr_capacity INT NOT NULL COMMENT '현재 방 인원수',
    status        VARCHAR(255) NOT NULL COMMENT '게임 진행 상태(대기 중, 게임 중)',
    created_at    TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    updated_at    TIMESTAMP NOT NULL COMMENT '테이블 수정 시간',
    deleted_at    TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS room_join (
    room_join_id BIGINT NOT NULL auto_increment PRIMARY KEY COMMENT '퀴즈방 참가 ID',
    room_id      BIGINT NOT NULL COMMENT '퀴즈방 ID',
    member_id    BIGINT NOT NULL COMMENT '퀴즈방 참여자 ID',
    joined_at    TIMESTAMP NOT NULL COMMENT '입장 시간'
);

CREATE TABLE IF NOT EXISTS quiz (
    quiz_id    BIGINT NOT NULL auto_increment PRIMARY KEY COMMENT '퀴즈 ID',
    member_id  BIGINT NOT NULL COMMENT '퀴즈 문항을 만든사람',
    question   TEXT NOT NULL COMMENT '퀴즈 내용',
    answer     VARCHAR(255) NOT NULL COMMENT '퀴즈 정답',
    category   VARCHAR(255) NOT NULL COMMENT '퀴즈 유형(네트워크, 운영체제 . . .)',
    created_at TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    updated_at TIMESTAMP NOT NULL COMMENT '테이블 수정 시간',
    deleted_at TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS game (
    game_id          BIGINT NOT NULL auto_increment PRIMARY KEY COMMENT '게임 ID',
    room_id          BIGINT NOT NULL COMMENT '퀴즈 방 ID',
    member_id        BIGINT NOT NULL comment '"게임 승리자 ID"',
    total_quiz_count INT NOT NULL COMMENT '전체 퀴즈 문항 수',
    started_at       TIMESTAMP NOT NULL COMMENT '게임 시작 시간',
    created_at       TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    updated_at       TIMESTAMP NOT NULL COMMENT '테이블 수정 시간',
    deleted_at       TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS member_game_result (
    member_game_result_id BIGINT NOT NULL auto_increment PRIMARY KEY COMMENT '회원 게임 결과 ID',
    game_id               BIGINT NOT NULL COMMENT '게임 ID',
    member_id             BIGINT NOT NULL COMMENT '참여자 ID',
    total_count           INT NOT NULL COMMENT '전체 문항 수',
    correct_count         INT NOT NULL COMMENT '맞힌 문항 수',
    ranking               INT NOT NULL COMMENT '게임 결과 등수',
    created_at            TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    updated_at            TIMESTAMP NOT NULL COMMENT '테이블 수정 시간',
    deleted_at            TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS member (
    member_id  BIGINT NOT NULL auto_increment PRIMARY KEY COMMENT '회원 ID',
    email      VARCHAR(255) NOT NULL COMMENT '이메일',
    password   VARCHAR(255) NOT NULL COMMENT '비밀번호',
    nickname   VARCHAR(255) NOT NULL COMMENT '닉네임',
    created_at TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    updated_at TIMESTAMP NOT NULL COMMENT '테이블 수정 시간',
    deleted_at TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);