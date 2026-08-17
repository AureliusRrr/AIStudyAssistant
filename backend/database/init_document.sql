CREATE TABLE tb_document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '上传用户',
    filename VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '存储路径',
    file_type VARCHAR(20) NOT NULL COMMENT '文件类型: pdf/md/docx',
    file_size BIGINT NOT NULL COMMENT '文件大小（字节）',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;