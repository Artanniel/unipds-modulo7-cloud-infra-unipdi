package br.com.unipds.unipdi.controller;

import br.com.unipds.unipdi.service.S3StorageService;
import io.awspring.cloud.s3.S3Resource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {

    private final S3StorageService s3StorageService;

    public FileStorageController(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String key = s3StorageService.uploadFile(file);
            return ResponseEntity.ok(Map.of(
                    "message", "Arquivo enviado com sucesso para o S3",
                    "fileKey", key
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Erro ao fazer upload do arquivo: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String key) {
        S3Resource s3Resource = s3StorageService.downloadFile(key);
        if (!s3Resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + s3Resource.getFilename() + "\"")
                .body(s3Resource);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String key) {
        if (!s3StorageService.fileExists(key)) {
            return ResponseEntity.notFound().build();
        }
        s3StorageService.deleteFile(key);
        return ResponseEntity.ok(Map.of("message", "Arquivo removido do S3 com sucesso"));
    }
}
