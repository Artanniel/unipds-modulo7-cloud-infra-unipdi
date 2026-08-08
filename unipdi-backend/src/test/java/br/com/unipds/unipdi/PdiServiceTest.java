package br.com.unipds.unipdi;

import br.com.unipds.unipdi.dto.PdiRequestDto;
import br.com.unipds.unipdi.model.Pessoa;
import br.com.unipds.unipdi.repository.PdiRepository;
import br.com.unipds.unipdi.repository.PessoaRepository;
import br.com.unipds.unipdi.service.PdiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PdiServiceTest {

    @Test
    public void testCriarPdiComPessoaInexistente() {
        PdiRepository pdiRepo = Mockito.mock(PdiRepository.class);
        PessoaRepository pessoaRepo = Mockito.mock(PessoaRepository.class);
        when(pessoaRepo.findByMatricula("123456")).thenReturn(Optional.empty());

        PdiService service = new PdiService(pdiRepo, pessoaRepo);
        PdiRequestDto dto = new PdiRequestDto("123456", "Cloud e Infra", LocalDate.now(), LocalDate.now().plusDays(1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.criarPdi(dto));
        assertEquals("Pessoa não encontrada para a matrícula 123456. Cadastre a pessoa antes de criar o PDI.", ex.getMessage());
    }

    @Test
    public void testCriarPdiComSucesso() {
        PdiRepository pdiRepo = Mockito.mock(PdiRepository.class);
        PessoaRepository pessoaRepo = Mockito.mock(PessoaRepository.class);

        Pessoa pessoa = new Pessoa("123456", "Artanniel Fortes");
        when(pessoaRepo.findByMatricula("123456")).thenReturn(Optional.of(pessoa));

        PdiService service = new PdiService(pdiRepo, pessoaRepo);
        PdiRequestDto dto = new PdiRequestDto("123456", "Cloud e Infra", LocalDate.now(), LocalDate.now().plusDays(1));

        var response = service.criarPdi(dto);
        assertNotNull(response);
        assertEquals("123456", response.pessoaMatricula());
        assertEquals("Cloud e Infra", response.descricao());
    }
}
