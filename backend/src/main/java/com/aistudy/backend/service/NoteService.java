package com.aistudy.backend.service;

import com.aistudy.backend.dto.NoteRequest;
import com.aistudy.backend.dto.NoteSummary;
import com.aistudy.backend.entity.Note;

import java.util.List;

public interface NoteService {
    Note create(NoteRequest request, Long userId);

    Note update(Long id,NoteRequest request, Long userId);

    Note getById(Long id);

    List<NoteSummary> listByUser(Long userId, String keyword, String tag);

    void deleteById(Long id, Long userId);

}
