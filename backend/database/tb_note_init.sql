CREATE TABLE tb_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '笔记作者',
    title VARCHAR(200) NOT NULL COMMENT '笔记标题',
    content LONGTEXT COMMENT '笔记内容（Markdown）',
    summary VARCHAR(500) COMMENT '摘要（前200字自动生成）',
    tags VARCHAR(500) DEFAULT '' COMMENT '标签，逗号分隔',
    is_pinned TINYINT DEFAULT 0 COMMENT '是否置顶: 0否/1是',
    view_count INT DEFAULT 0 COMMENT '阅读次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;