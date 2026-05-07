package com.internship.tool.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    private final Path storageLocation = Paths.get("uploads");

    public FileController() throws IOException {
        if (!Files.exists(storageLocation)) {
            Files.createDirectories(storageLocation);
        }
    }

    // ✅ POST /upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Validate size (< 10 MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("File size exceeds 10 MB limit");
        }

        // Validate type (basic check by content type)
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/")
                && !contentType.equals("application/pdf")
                && !contentType.equals("text/plain"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid file type. Only images, PDF, and text files allowed.");
        }

        // Generate UUID filename
        String uuid = UUID.randomUUID().toString();
        String filename = uuid + "_" + file.getOriginalFilename();
        Path target = storageLocation.resolve(filename);

        Files.copy(file.getInputStream(), target);

        return ResponseEntity.ok("File uploaded successfully with ID: " + filename);
    }

    // ✅ GET /files/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) throws IOException {
        Path filePath = storageLocation.resolve(id);
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok(resource);
    }
}
