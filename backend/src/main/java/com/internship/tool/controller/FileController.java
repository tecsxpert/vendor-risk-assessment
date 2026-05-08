package com.internship.tool.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@Tag(name = "File Management", description = "APIs for file upload and download")
public class FileController {

    private final Path storageLocation = Paths.get("uploads");

    public FileController() throws IOException {
        if (!Files.exists(storageLocation)) {
            Files.createDirectories(storageLocation);
        }
    }

    // ✅ POST /upload
    @PostMapping("/upload")
    @Operation(summary = "Upload a file", description = "Uploads a file with validation (max 10MB, specific types allowed)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                content = @Content(mediaType = "text/plain",
                        schema = @Schema(example = "File uploaded successfully with ID: uuid_filename.pdf"))),
        @ApiResponse(responseCode = "400", description = "Invalid file size or type",
                content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<String> uploadFile(@Parameter(description = "File to upload") @RequestParam("file") MultipartFile file) throws IOException {
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
    @Operation(summary = "Download a file", description = "Downloads a previously uploaded file by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File retrieved successfully",
                content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "404", description = "File not found",
                content = @Content)
    })
    public ResponseEntity<Resource> getFile(@Parameter(description = "File ID", example = "uuid_filename.pdf") @PathVariable String id) throws IOException {
        Path filePath = storageLocation.resolve(id);
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok(resource);
    }
}
