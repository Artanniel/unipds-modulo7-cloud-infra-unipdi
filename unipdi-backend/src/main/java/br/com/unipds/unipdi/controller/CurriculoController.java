package br.com.unipds.unipdi.controller;

import br.com.unipds.unipdi.model.Pessoa;
import br.com.unipds.unipdi.repository.PessoaRepository;
import br.com.unipds.unipdi.service.CurriculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/curriculos")
public class CurriculoController {

    private final CurriculoService curriculoService;
    private final PessoaRepository pessoaRepository;

    public CurriculoController(CurriculoService curriculoService, PessoaRepository pessoaRepository) {
        this.curriculoService = curriculoService;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping("/{matricula}/upload")
    public ResponseEntity<String> uploadCurriculo(@PathVariable String matricula, @RequestParam("file")MultipartFile file) {
        try {
            Pessoa pessoa = pessoaRepository.findByMatricula(matricula).orElseThrow(() -> new IllegalArgumentException(("Pessoa não econtrada!")));
            curriculoService.uploadCurriculo(matricula, file);
            String url = curriculoService.gerarPresignedUrl(matricula, 5);
            pessoa.setCurriculo(url);
            pessoaRepository.save(pessoa);
            return ResponseEntity.ok("Curriculo enviado com sucesso, link: " + url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{matricula}/url")
    public ResponseEntity<String> getCurriculoUrl(@PathVariable String matricula) {
        Pessoa pessoa = pessoaRepository.findByMatricula(matricula).orElseThrow(() -> new IllegalArgumentException("Pessoa não econtrada!"));
        if(pessoa.getCurriculo() == null){
            return null;
        }
        var url = gravarUrl(pessoa);
        return ResponseEntity.ok(url.toString());
    }

    private Object gravarUrl(Pessoa pessoa) {
        String url = curriculoService.gerarPresignedUrl(pessoa.getMatricula(), 15);
        pessoa.setCurriculo(url);
        pessoaRepository.save(pessoa);
        return url;
    }
}
