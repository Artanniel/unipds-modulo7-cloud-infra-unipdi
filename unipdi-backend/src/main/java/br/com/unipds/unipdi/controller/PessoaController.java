package br.com.unipds.unipdi.controller;

import br.com.unipds.unipdi.service.PessoaService;
import br.com.unipds.unipdi.dto.PessoaRequestDto;
import br.com.unipds.unipdi.dto.PessoaResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDto> cadastrar(@RequestBody PessoaRequestDto dto) {
        return ResponseEntity.ok(pessoaService.cadastrarPessoa(dto));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<PessoaResponseDto> buscar(@PathVariable String matricula) {
        return ResponseEntity.ok(pessoaService.buscarPorMatricula(matricula));
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDto>> buscarTodos() {
        return ResponseEntity.ok(pessoaService.buscarTodos());
    }

    @PostMapping("/{matricula}/curriculo")
    public ResponseEntity<PessoaResponseDto> uploadCurriculo(@PathVariable String matricula, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(pessoaService.uploadCurriculo(matricula, file));
    }

    @PutMapping("/{matricula}/curriculo")
    public ResponseEntity<PessoaResponseDto> atualizarCurriculoKey(@PathVariable String matricula, @RequestBody Map<String, String> body) {
        String fileKey = body.get("fileKey");
        return ResponseEntity.ok(pessoaService.atualizarCurriculo(matricula, fileKey));
    }

    @DeleteMapping("/{matricula}/curriculo")
    public ResponseEntity<PessoaResponseDto> removerCurriculo(@PathVariable String matricula) {
        return ResponseEntity.ok(pessoaService.removerCurriculo(matricula));
    }
}
