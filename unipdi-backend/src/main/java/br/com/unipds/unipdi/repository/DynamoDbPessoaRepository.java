package br.com.unipds.unipdi.repository;

import br.com.unipds.unipdi.model.Pessoa;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.List;
import java.util.Optional;

@Repository
public class DynamoDbPessoaRepository implements PessoaRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public DynamoDbPessoaRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        return dynamoDbTemplate.save(pessoa);
    }

    @Override
    public Optional<Pessoa> findByMatricula(String matricula) {
        Key key = Key.builder().partitionValue(matricula).build();
        Pessoa pessoa = dynamoDbTemplate.load(key, Pessoa.class);
        return Optional.ofNullable(pessoa);
    }

    @Override
    public boolean existsByMatricula(String matricula) {
        return findByMatricula(matricula).isPresent();
    }

    @Override
    public List<Pessoa> findAll() {
        PageIterable<Pessoa> scan = dynamoDbTemplate.scanAll(Pessoa.class);
        return scan.items().stream().toList();
    }
}
