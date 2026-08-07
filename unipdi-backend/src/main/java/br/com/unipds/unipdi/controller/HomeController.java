package br.com.unipds.unipdi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
                "application", "unipdi",
                "status", "UP",
                "message", "API do UniPDI rodando com sucesso!",
                "endpoints", Map.of(
                        "pessoas", "/pessoas",
                        "pdis", "/pdis",
                        "s3Files", "/api/files"
                )
        ));
    }
}
