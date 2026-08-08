package br.com.unipds.unipdi.repository;

import br.com.unipds.unipdi.model.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {
    Pessoa save(Pessoa pessoa);
    Optional<Pessoa> findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
    List<Pessoa> findAll();
}

