package br.com.unipds.unipdi.service;

import br.com.unipds.unipdi.dto.PessoaRequestDto;
import br.com.unipds.unipdi.dto.PessoaResponseDto;
import br.com.unipds.unipdi.model.Pessoa;
import br.com.unipds.unipdi.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final S3StorageService s3StorageService;

    public PessoaService(PessoaRepository pessoaRepository, S3StorageService s3StorageService) {
        this.pessoaRepository = pessoaRepository;
        this.s3StorageService = s3StorageService;
    }

    public PessoaResponseDto cadastrarPessoa(PessoaRequestDto dto) {
        if (pessoaRepository.existsByMatricula(dto.matricula())) {
            throw new IllegalArgumentException("Matrícula já cadastrada: " + dto.matricula());
        }

        Pessoa pessoa = new Pessoa(dto.matricula(), dto.nome());
        Pessoa salva = pessoaRepository.save(pessoa);

        return new PessoaResponseDto(salva.getId(), salva.getMatricula(), salva.getNome(), salva.getCurriculo());
    }

    public PessoaResponseDto buscarPorMatricula(String matricula) {
        Pessoa pessoa = pessoaRepository.findByMatricula(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com matrícula " + matricula));

        return new PessoaResponseDto(pessoa.getId(), pessoa.getMatricula(), pessoa.getNome(), pessoa.getCurriculo());
    }

    public List<PessoaResponseDto> buscarTodos() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return pessoas.stream().map(p ->
                new PessoaResponseDto(p.getId(), p.getMatricula(), p.getNome(), p.getCurriculo()))
                .toList();
    }

    public PessoaResponseDto atualizarCurriculo(String matricula, String fileKey) {
        Pessoa pessoa = pessoaRepository.findByMatricula(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com matrícula " + matricula));

        pessoa.setCurriculo(fileKey);
        Pessoa salva = pessoaRepository.save(pessoa);
        return new PessoaResponseDto(salva.getId(), salva.getMatricula(), salva.getNome(), salva.getCurriculo());
    }

    public PessoaResponseDto uploadCurriculo(String matricula, MultipartFile file) throws IOException {
        Pessoa pessoa = pessoaRepository.findByMatricula(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com matrícula " + matricula));

        if (pessoa.getCurriculo() != null && !pessoa.getCurriculo().isBlank()) {
            try {
                s3StorageService.deleteFile(pessoa.getCurriculo());
            } catch (Exception ignored) {}
        }

        String fileKey = s3StorageService.uploadFile(file);
        pessoa.setCurriculo(fileKey);
        Pessoa salva = pessoaRepository.save(pessoa);
        return new PessoaResponseDto(salva.getId(), salva.getMatricula(), salva.getNome(), salva.getCurriculo());
    }

    public PessoaResponseDto removerCurriculo(String matricula) {
        Pessoa pessoa = pessoaRepository.findByMatricula(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com matrícula " + matricula));

        if (pessoa.getCurriculo() != null && !pessoa.getCurriculo().isBlank()) {
            try {
                s3StorageService.deleteFile(pessoa.getCurriculo());
            } catch (Exception ignored) {}
            pessoa.setCurriculo(null);
            Pessoa salva = pessoaRepository.save(pessoa);
            return new PessoaResponseDto(salva.getId(), salva.getMatricula(), salva.getNome(), salva.getCurriculo());
        }

        return new PessoaResponseDto(pessoa.getId(), pessoa.getMatricula(), pessoa.getNome(), pessoa.getCurriculo());
    }
}
