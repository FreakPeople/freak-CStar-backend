-- 1. 기존 category 테이블을 quiz_category로 변경
RENAME TABLE category TO quiz_category;
ALTER TABLE quiz_category MODIFY category_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '퀴즈 카테고리 ID';
ALTER TABLE quiz_category CHANGE category category_name VARCHAR(100) NOT NULL COMMENT '카테고리명';
ALTER TABLE quiz_category ADD COLUMN created_at TIMESTAMP NOT NULL COMMENT '테이블 생성 시간';
ALTER TABLE quiz_category ADD COLUMN updated_at TIMESTAMP NULL COMMENT '테이블 수정 시간';
ALTER TABLE quiz_category ADD COLUMN deleted_at TIMESTAMP NULL COMMENT '테이블 삭제 시간';

-- 2. quiz 테이블 변경
ALTER TABLE quiz CHANGE category_id quiz_category_id BIGINT NOT NULL COMMENT '퀴즈 카테고리 ID';
ALTER TABLE quiz CHANGE member_id writer_id BIGINT NOT NULL COMMENT '퀴즈 생성자 ID';
ALTER TABLE quiz MODIFY question TEXT NOT NULL COMMENT '퀴즈 질문';
ALTER TABLE quiz MODIFY answer TEXT NOT NULL COMMENT '퀴즈 정답';
ALTER TABLE quiz ADD COLUMN workbook_id BIGINT NOT NULL COMMENT '워크북 ID';

-- 3. workbook 테이블 생성
CREATE TABLE IF NOT EXISTS workbook
(
    workbook_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '워크북 ID',
    writer_id BIGINT NOT NULL COMMENT '워크북 작성자 ID',
    title VARCHAR(255) NOT NULL COMMENT '워크북 제목',
    description TEXT NOT NULL COMMENT '워크북 설명',
    type VARCHAR(255) NOT NULL COMMENT '주관식, 서술형',
    created_at TIMESTAMP NOT NULL COMMENT '테이블 생성 시간',
    updated_at TIMESTAMP NULL COMMENT '테이블 수정 시간'
);

-- 4. friend 테이블 생성
CREATE TABLE IF NOT EXISTS friend
(
  member_one_id BIGINT NOT NULL COMMENT '친구 관계의 첫 번째 회원 ID',
  member_two_id BIGINT NOT NULL COMMENT '친구 관계의 두 번째 회원 ID',
  created_at TIMESTAMP NOT NULL COMMENT '테이블 생성 시간'
);

-- 5. room 테이블 변경
ALTER TABLE room MODIFY room_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게임방 ID';
ALTER TABLE room ADD COLUMN owner_id BIGINT NOT NULL COMMENT '방장 ID';
ALTER TABLE room CHANGE status room_status VARCHAR(255) NOT NULL COMMENT '게임 진행 상태(대기 중, 게임 중)';

-- 6. game 테이블 변경
ALTER TABLE game MODIFY room_id BIGINT NOT NULL COMMENT '게임방 ID';
ALTER TABLE game CHANGE member_id winner_id BIGINT NOT NULL COMMENT '승리자 ID';
ALTER TABLE game ADD COLUMN game_type VARCHAR(255) NOT NULL COMMENT '1인 플레이, 다인 플레이';
ALTER TABLE game DROP COLUMN category_id;
ALTER TABLE game DROP COLUMN updated_at;

-- 7. member_game_result → member_game_history 테이블로 변경
ALTER TABLE member_game_result RENAME TO member_game_history;
ALTER TABLE member_game_history CHANGE member_game_result_id member_game_history_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '회원 게임 결과 ID';
ALTER TABLE member_game_history MODIFY total_count INT NOT NULL COMMENT '총 풀이한 문항 수';
ALTER TABLE member_game_history DROP COLUMN updated_at;

-- 8. game_quiz → game_quiz_mapping 테이블로 변경
ALTER TABLE game_quiz RENAME TO game_quiz_mapping;
ALTER TABLE game_quiz_mapping ADD COLUMN game_quiz_mapping_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '게임 퀴즈 ID';

-- 9. room_join → room_member_mapping 테이블로 변경
ALTER TABLE room_join RENAME TO room_member_mapping;
ALTER TABLE room_member_mapping CHANGE room_join_id room_member_mapping_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게임방 참가 ID';
ALTER TABLE room_member_mapping CHANGE room_id room_id BIGINT NOT NULL COMMENT '게임방 ID';
ALTER TABLE room_member_mapping CHANGE member_id member_id BIGINT NOT NULL COMMENT '게임방 참여자 ID';
