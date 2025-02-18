CREATE TABLE IF NOT EXISTS quiz_category
(
    `category_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '퀴즈 카테고리 ID',
    `category_name` VARCHAR(100) NOT NULL COMMENT '카테고리명',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `updated_at` TIMESTAMP NULL COMMENT '테이블 수정 시간',
    `deleted_at` TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS quiz
(
    `quiz_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '퀴즈 ID',
    `quiz_category_id` BIGINT NOT NULL COMMENT '퀴즈 카테고리 ID',
    `workbook_id` BIGINT NOT NULL COMMENT '워크북 ID',
    `writer_id` BIGINT NOT NULL COMMENT '퀴즈 생성자 ID',
    `question` TEXT NOT NULL COMMENT '질문',
    `answer` TEXT NOT NULL COMMENT '답변',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `updated_at` TIMESTAMP NULL COMMENT '테이블 수정 시간',
    `deleted_at` TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS workbook
(
    `workbook_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '워크북 ID',
    `writer_id` BIGINT NOT NULL COMMENT '워크북 작성자 ID',
    `title` VARCHAR(255) NOT NULL COMMENT '워크북 제목',
    `description` TEXT NOT NULL COMMENT '워크북 설명',
    `type` VARCHAR(255) NOT NULL COMMENT '주관식, 서술형',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `updated_at` TIMESTAMP NULL COMMENT '테이블 수정 시간'
);

CREATE TABLE IF NOT EXISTS friend
(
    `member_one_id` BIGINT NOT NULL COMMENT '친구 관계의 첫 번째 회원 ID',
    `member_two_id` BIGINT NOT NULL COMMENT '친구 관계의 두 번째 회원 ID',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간'
);

CREATE TABLE IF NOT EXISTS room
(
    `room_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '게임방 ID',
    `owner_id` BIGINT NOT NULL COMMENT '방장 ID',
    `max_capacity` INT NOT NULL COMMENT '수용 가능 인원수',
    `curr_capacity` INT NOT NULL COMMENT '현재 방 인원수',
    `room_status` VARCHAR(255) NOT NULL COMMENT '게임 진행 상태(대기 중, 게임 중)',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `updated_at` TIMESTAMP NULL COMMENT '테이블 수정 시간',
    `deleted_at` TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS game
(
    `game_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '게임 ID',
    `room_id` BIGINT NOT NULL COMMENT '게임방 ID',
    `winner_id` BIGINT NOT NULL COMMENT '승리자 ID',
    `game_type` VARCHAR(255) NOT NULL COMMENT '1인 플레이, 다인 플레이',
    `total_quiz_count` INT NOT NULL COMMENT '전체 퀴즈 수',
    `started_at` TIMESTAMP NOT NULL COMMENT '게임 시작 시간',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `deleted_at` TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS member_game_history
(
    `member_game_history_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '회원 게임 결과 ID',
    `game_id` BIGINT NOT NULL COMMENT '게임 ID',
    `member_id` BIGINT NOT NULL COMMENT '참여자 ID',
    `correct_count` INT NOT NULL COMMENT '맞힌 문항 수',
    `total_count` INT NOT NULL COMMENT '풀이를 진행한 전체 문항 수',
    `ranking` INT NOT NULL COMMENT '게임 결과 등수',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `deleted_at` TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS game_quiz_mapping
(
    `game_quiz_mapping_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '게임 퀴즈 ID' ,
    `game_id` BIGINT NOT NULL COMMENT '게임 ID',
    `quiz_id` BIGINT NOT NULL COMMENT '퀴즈 ID'
);

CREATE TABLE IF NOT EXISTS member
(
    `member_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '회원 ID',
    `email` VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일',
    `password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `nickname` VARCHAR(100) NOT NULL UNIQUE COMMENT '닉네임',
    `created_at` TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    `updated_at` TIMESTAMP NULL COMMENT '테이블 수정 시간',
    `deleted_at` TIMESTAMP NULL COMMENT '테이블 삭제 시간'
);

CREATE TABLE IF NOT EXISTS room_member_mapping
(
    `room_member_mapping_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '게임방 참가 ID',
    `room_id` BIGINT NOT NULL COMMENT '게임방 ID',
    `member_id` BIGINT NOT NULL COMMENT '게임방 참여자 ID'
);
