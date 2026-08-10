package com.aistudy.backend.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private String title;
    private String content;
    private String tags;
    private Integer isPinned;

}
