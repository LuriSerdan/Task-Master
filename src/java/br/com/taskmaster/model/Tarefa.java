package br.com.taskmaster.model;

import java.util.Date;

/**
 * Classe Modelo que representa a tabela 'tarefa' no banco de dados.
 */
public class Tarefa {
    private int id;
    private String nome;
    private String descricao;
    private Date dataCriacao;
    private Date dataEntrega;
    private Integer projetoId;
    private Integer responsavelId;
    private Integer statusId;

    // Construtor padrão
    public Tarefa() {}

    // Construtor completo (pode ser útil para testes ou DAO)
    public Tarefa(int id, String nome, String descricao, Date dataCriacao, Date dataEntrega, Integer projetoId, Integer responsavelId, Integer statusId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataEntrega = dataEntrega;
        this.projetoId = projetoId;
        this.responsavelId = responsavelId;
        this.statusId = statusId;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Integer getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(Integer projetoId) {
        this.projetoId = projetoId;
    }

    public Integer getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Integer responsavelId) {
        this.responsavelId = responsavelId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    // Método toString (opcional, mas útil para debug)
    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataEntrega=" + dataEntrega +
                ", statusId=" + statusId +
                '}';
    }
}