USE ai_study_assistant;

CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);