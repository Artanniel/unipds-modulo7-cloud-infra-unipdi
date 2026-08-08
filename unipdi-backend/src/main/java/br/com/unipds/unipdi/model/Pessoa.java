package br.com.unipds.unipdi.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Pessoa {

    private String matricula;
    private String nome;
    private String curriculo;

    public Pessoa() {
    }

    public Pessoa(String matricula, String nome) {
        this.matricula = matricula;
        this.nome = nome;
    }

    @DynamoDbPartitionKey
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }

    public String getId() {
        return matricula;
    }
}

