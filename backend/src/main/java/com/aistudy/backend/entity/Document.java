package com.aistudy.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_document")
public class Document {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String filename;

    private String filePath;

    private String fileType;

    private Long fileSize;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;
}