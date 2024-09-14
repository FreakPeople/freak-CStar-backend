CREATE TABLE IF NOT EXISTS category
(
    category_id BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 ID',
    category    VARCHAR(255) NOT NULL COMMENT '퀴즈 유형(네트워크, 운영체제 . . .)'
);

ALTER TABLE quiz
    DROP COLUMN category;

ALTER TABLE quiz
    ADD COLUMN category_id BIGINT;

ALTER TABLE game
    ADD COLUMN category_id BIGINT;
