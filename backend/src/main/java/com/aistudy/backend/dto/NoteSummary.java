package com.aistudy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoteSummary {
    private Long id;

    private String title;

    private String summary;

    private String tags;

    private Integer isPinned;

    private Integer viewCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
