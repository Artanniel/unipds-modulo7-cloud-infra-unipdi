package br.com.unipds.unipdi.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DynamoDbBean
public class Pdi {
    private String id;
    private String pessoaMatricula;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;
    private List<Meta> metas = new ArrayList<>();

    public Pdi() {
    }

    public Pdi(String pessoaMatricula, LocalDate dataInicio, LocalDate dataFim, String descricao) {
        this.id = UUID.randomUUID().toString();
        this.pessoaMatricula = pessoaMatricula;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.descricao = descricao;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPessoaMatricula() {
        return pessoaMatricula;
    }

    public void setPessoaMatricula(String pessoaMatricula) {
        this.pessoaMatricula = pessoaMatricula;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Meta> getMetas() {
        return metas;
    }

    public void setMetas(List<Meta> metas) {
        this.metas = metas;
    }

    public void addMeta(Meta meta) {
        if (this.metas == null) {
            this.metas = new ArrayList<>();
        }
        this.metas.add(meta);
    }
}

