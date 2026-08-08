package br.com.unipds.unipdi.config;

import br.com.unipds.unipdi.model.Pdi;
import br.com.unipds.unipdi.model.Pessoa;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

@Configuration
public class DynamoDbConfig {

    private static final Logger log = LoggerFactory.getLogger(DynamoDbConfig.class);

    @Bean
    public DynamoDbTableNameResolver dynamoDbTableNameResolver() {
        return new DynamoDbTableNameResolver() {
            @Override
            public <T> String resolve(Class<T> clazz) {
                // Preserva o nome exato da classe (ex: "Pessoa", "Pdi")
                // evitando conversão automática para minúsculo ("pessoa")
                return clazz.getSimpleName();
            }
        };
    }

    @Bean
    public CommandLineRunner initializeDynamoDbTables(DynamoDbEnhancedClient enhancedClient) {
        return args -> {
            createTableIfNotExists(enhancedClient, Pessoa.class);
            createTableIfNotExists(enhancedClient, Pdi.class);
        };
    }

    private <T> void createTableIfNotExists(DynamoDbEnhancedClient enhancedClient, Class<T> clazz) {
        String tableName = clazz.getSimpleName();
        try {
            DynamoDbTable<T> table = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
            table.createTable();
            log.info("Tabela '{}' criada com sucesso no DynamoDB.", tableName);
        } catch (ResourceInUseException e) {
            log.info("Tabela '{}' já existe no DynamoDB.", tableName);
        } catch (Exception e) {
            log.warn("Erro ao verificar/criar tabela '{}' no DynamoDB: {}", tableName, e.getMessage());
        }
    }
}
