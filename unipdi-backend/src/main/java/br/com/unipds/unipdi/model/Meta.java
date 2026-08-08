package br.com.unipds.unipdi.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.UUID;

@DynamoDbBean
public class Meta {
    private String id;
    private String descricao;
    private boolean concluida;

    public Meta() {}

    public Meta(String descricao, boolean concluida) {
        this.id = UUID.randomUUID().toString();
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}

