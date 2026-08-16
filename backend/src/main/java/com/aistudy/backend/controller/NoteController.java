package com.aistudy.backend.controller;

import com.aistudy.backend.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteMapper noteMapper;

    private Long getCurrentUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }




}
