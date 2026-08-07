package br.com.unipds.unipdi.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3StorageService {

    private final S3Template s3Template;

    @Value("${aws.s3.bucket-name:unipdi-bucket}")
    private String bucketName;

    public S3StorageService(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    private synchronized void ensureBucketExists() {
        try {
            if (!s3Template.bucketExists(bucketName)) {
                s3Template.createBucket(bucketName);
            }
        } catch (Exception e) {
            // Log ou relança caso seja erro de permissão/conectividade
            System.err.println("Aviso: Não foi possível verificar/criar o bucket " + bucketName + ": " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        ensureBucketExists();
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            s3Template.upload(bucketName, fileName, inputStream);
            return fileName;
        }
    }

    public S3Resource downloadFile(String key) {
        ensureBucketExists();
        return s3Template.download(bucketName, key);
    }

    public void deleteFile(String key) {
        ensureBucketExists();
        s3Template.deleteObject(bucketName, key);
    }

    public boolean fileExists(String key) {
        ensureBucketExists();
        return s3Template.objectExists(bucketName, key);
    }

    public String getBucketName() {
        return bucketName;
    }
}
