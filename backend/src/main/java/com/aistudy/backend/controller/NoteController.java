package com.aistudy.backend.controller;

import com.aistudy.backend.common.Result;
import com.aistudy.backend.dto.NoteRequest;
import com.aistudy.backend.dto.NoteSummary;
import com.aistudy.backend.entity.Note;
import com.aistudy.backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    private Long getCurrentUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }

    //创建笔记
    @PostMapping
    public Result<Note> create(@RequestBody NoteRequest request){
        return Result.success(noteService.create(request,getCurrentUserId()));

    }

    //更新笔记
    @PostMapping({"/{id}"})
    public Result<Note> update(@PathVariable Long id, @RequestBody NoteRequest request){
        return Result.success(noteService.update(id,request, getCurrentUserId()));
    }

    //获取笔记详情
    @GetMapping("/{id}")
    public Result<Note> detail(@PathVariable Long id){
        return Result.success(noteService.getById(id));
    }

    //笔记列表(支持搜索和标签过滤)
    @GetMapping("/list")
    public Result<List<NoteSummary>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag) {
        return Result.success(noteService.listByUser(getCurrentUserId(),keyword,tag));
    }

    //删除笔记

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        noteService.deleteById(id,getCurrentUserId());
        return Result.success("删除成功");
    }


}
