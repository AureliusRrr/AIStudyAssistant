package com.aistudy.backend.service;

import com.aistudy.backend.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    Document uploadFile(MultipartFile file,Long userId);
    List<Document> listByUser(Long userId);
    Document getById(Long id);
    void deleteById(Long id,Long userId);
    String getFilePath(Long id);

}
