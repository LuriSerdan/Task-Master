package br.com.taskmaster.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Projeto {
    private int id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int liderId;

    private List<Integer> membrosIds = new ArrayList<>();

    public Projeto() {
        this.membrosIds = new ArrayList<>();
    }

    public Projeto(int id, String nome, LocalDate dataInicio, LocalDate dataFim, int liderId) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.liderId = liderId;
        this.membrosIds = new ArrayList<>();
    }

    // --- Getters e Setters existentes ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public int getLiderId() { return liderId; }
    public void setLiderId(int liderId) { this.liderId = liderId; }

    public List<Integer> getMembrosIds() {
        return membrosIds;
    }

    public void setMembrosIds(List<Integer> membrosIds) {
        this.membrosIds = membrosIds;
    }

    public void adicionarMembro(int idUsuario) {
        this.membrosIds.add(idUsuario);
    }
}