package br.com.unipds.unipdi.repository;

import br.com.unipds.unipdi.model.Pdi;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DynamoDbPdiRepository implements PdiRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public DynamoDbPdiRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public Pdi save(Pdi pdi) {
        if (pdi.getId() == null || pdi.getId().isBlank()) {
            pdi.setId(UUID.randomUUID().toString());
        }
        return dynamoDbTemplate.save(pdi);
    }

    @Override
    public Optional<Pdi> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        Pdi pdi = dynamoDbTemplate.load(key, Pdi.class);
        return Optional.ofNullable(pdi);
    }

    @Override
    public List<Pdi> findByPessoaMatricula(String matricula) {
        PageIterable<Pdi> scan = dynamoDbTemplate.scanAll(Pdi.class);
        return scan.items().stream()
                .filter(p -> matricula.equals(p.getPessoaMatricula()))
                .toList();
    }

    @Override
    public List<Pdi> findAll() {
        PageIterable<Pdi> scan = dynamoDbTemplate.scanAll(Pdi.class);
        return scan.items().stream().toList();
    }
}
