package com.aistudy.backend.service.Impl;

import com.aistudy.backend.config.FileUploadConfig;
import com.aistudy.backend.entity.Document;
import com.aistudy.backend.mapper.DocumentMapper;
import com.aistudy.backend.service.DocumentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentMapper documentMapper;
    private final FileUploadConfig fileUploadConfig;


    @Override
    public Document uploadFile(MultipartFile file, Long userId) {
        //1.校验文件类型
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        validateFileType(extension);

        //2.生成唯一文件名:uuid.扩展名
        String storedFilename = UUID.randomUUID() + "." + extension;

        //3.按日期创建子目录:uploads/日期
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path uploadDir = Paths.get(fileUploadConfig.getPath(), dateDir);
        try {
            Files.createDirectories(uploadDir);
        }catch (IOException e){
            throw new RuntimeException("创建上传目录失败");
        }
        //4.保存文件到磁盘
        Path targetPath = uploadDir.resolve(storedFilename);
        try{
            Files.copy(file.getInputStream(),targetPath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new RuntimeException("文件保存失败");

        }

        //5.在数据库中记录文件信息
        Document document = new Document();
        document.setUserId(userId);
        document.setFilename(originalFilename);
        document.setFilePath(targetPath.toString());
        document.setFileType(extension);
        document.setFileSize(file.getSize());

        documentMapper.insert(document);
        return document;


    }

    @Override
    public List<Document> listByUser(Long userId) {
        LambdaQueryWrapper<Document> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Document::getUserId, userId).orderByDesc(Document::getUploadTime);
        return documentMapper.selectList(wrapper);
    }

    @Override
    public Document getById(Long id) {
        return documentMapper.selectById(id);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        Document document = documentMapper.selectById(id);
        if(document == null){
            throw new RuntimeException("文件不存在");
        }
        if(!document.getUserId().equals(userId)){
            throw new RuntimeException("无权删除他人文件");
        }

        //删除磁盘文件
        try {
            Files.deleteIfExists(Paths.get(document.getFilePath()));
        } catch(IOException e){
            //磁盘文件不存在也算删除成功
        }

        //删除数据库记录
        documentMapper.deleteById(id);
    }

    @Override
    public String getFilePath(Long id) {
        Document document = documentMapper.selectById(id);
        if(document == null){
            throw new RuntimeException("文件不存在");
        }
        return document.getFilePath();
    }


    //辅助方法
    private String getFileExtension(String filename){
        if (filename == null || !filename.contains(".")){
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private void validateFileType(String extension){
        List<String> allowed = Arrays.asList(fileUploadConfig.getAllowedTypes().split(","));
        if(!allowed.contains(extension)){
            throw new RuntimeException("不支持的文件类型: " + extension + "允许: " + fileUploadConfig.getAllowedTypes());
        }

    }


}
