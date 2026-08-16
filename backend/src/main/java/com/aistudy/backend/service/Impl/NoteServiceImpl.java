package com.aistudy.backend.service.Impl;

import com.aistudy.backend.dto.NoteRequest;
import com.aistudy.backend.dto.NoteSummary;
import com.aistudy.backend.entity.Note;
import com.aistudy.backend.mapper.NoteMapper;
import com.aistudy.backend.service.NoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteMapper noteMapper;


    @Override
    public Note create(NoteRequest request, Long userId) {
        Note note = new Note();
        note.setUserId(userId);
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setSummary(generateSummary(request.getContent()));
        note.setTags(request.getTags() != null ? request.getTags() : "");
        note.setIsPinned(request.getIsPinned() != null ? request.getIsPinned() : 0);
        note.setViewCount(0);

        noteMapper.insert(note);
        return note;
    }

    @Override
    public Note update(Long id, NoteRequest request, Long userId) {
        Note note = noteMapper.selectById(id);
        if(note == null){
            throw new RuntimeException("笔记不存在");
        }
        if(!note.getUserId().equals(userId)){
            throw new RuntimeException("无权修改他人笔记");
        }

        note.setTitle(request.getTitle());

        note.setContent(request.getContent());

        note.setSummary(generateSummary(request.getContent()));

        note.setTags(request.getTags() != null ? request.getTags() : note.getTags());

        //如果请求中没有传 isPinned，保持原值不变
        if(request.getIsPinned() != null){
            note.setIsPinned(request.getIsPinned());
        }

        noteMapper.updateById(note);
        return noteMapper.selectById(id);

    }

    @Override
    public Note getById(Long id) {
        Note note = noteMapper.selectById(id);
        if(note == null){
            throw new RuntimeException("笔记不存在");
        }
        //阅读次数+1
        note.setViewCount(note.getViewCount() + 1);
        noteMapper.updateById(note);
        return note;

    }

    @Override
    public List<NoteSummary> listByUser(Long userId,String keyWord, String tag){
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Note::getUserId, userId);

        if(StringUtils.hasText(keyWord)){
            wrapper.and(w -> w
                    .like(Note::getTitle, keyWord)
                    .or()
                    .like(Note::getContent, keyWord)
            );
        }

        //标签过滤
        if(StringUtils.hasText(tag)){
            wrapper.like(Note::getTags, tag);
        }

        //置顶优先+最近更新
        wrapper.orderByDesc(Note::getIsPinned)
                .orderByDesc(Note::getUpdateTime);

        List<Note> notes = noteMapper.selectList(wrapper);

        //转换为 NoteSummary, 列表不返回全文
        return notes.stream().map(note -> new NoteSummary(
                note.getId(),
                note.getTitle(),
                note.getSummary(),
                note.getTags(),
                note.getIsPinned(),
                note.getViewCount(),
                note.getCreateTime(),
                note.getUpdateTime()
        )).collect(Collectors.toList());

    }

    @Override
    public void deleteById(Long id, Long userId) {

    }

    //辅助方法

    private String generateSummary(String content){
        if(!StringUtils.hasText(content)) {
            return "";
        }
        // 去掉 Markdown 标记后取前 200 字
        String plainText = content
                .replaceAll("#+ ", "")
                .replaceAll("\\*\\*", "")
                .replaceAll("", "")
                .replaceAll("\\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        return plainText.length() > 200 ? plainText.substring(0,200) + "..." : plainText;

    }



}
