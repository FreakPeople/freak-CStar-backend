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

INSERT INTO category (category_id, category)
VALUES (1, 'ALGORITHM'),
       (2, 'DATABASE'),
       (3, 'DESIGN_PATTERN'),
       (4, 'NETWORK'),
       (5, 'OPERATING_SYSTEM');
