package br.com.unipds.unipdi.repository;

import br.com.unipds.unipdi.model.Pdi;

import java.util.List;
import java.util.Optional;

public interface PdiRepository {
    Pdi save(Pdi pdi);
    Optional<Pdi> findById(String id);
    List<Pdi> findByPessoaMatricula(String matricula);
    List<Pdi> findAll();
}

