package com.aistudy.backend.controller;

import com.aistudy.backend.common.Result;
import com.aistudy.backend.entity.Document;
import com.aistudy.backend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }

    @PostMapping("/upload")
    public Result<Document> upload(@RequestParam("file") MultipartFile file) {
        Long userId = getCurrentUserId();
        Document document = documentService.uploadFile(file, userId);
        return Result.success(document);
    }

    @GetMapping("/list")
    public Result<List<Document>> list() {
        Long userId = getCurrentUserId();
        List<Document> documents = documentService.listByUser(userId);
        return Result.success(documents);
    }

    @GetMapping("/{id}")
    public Result<Document> detail(@PathVariable Long id) {
        return Result.success(documentService.getById(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Document document = documentService.getById(id);
        String filePath = document.getFilePath();

        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            throw new RuntimeException("file not found");
        }

        String encodedFilename = URLEncoder.encode(document.getFilename(),
                StandardCharsets.UTF_8).replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        documentService.deleteById(id, userId);
        return Result.success("deleted");
    }
}