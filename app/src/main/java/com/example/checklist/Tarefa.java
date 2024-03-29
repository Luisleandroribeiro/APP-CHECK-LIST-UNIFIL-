package com.example.checklist;

public class Tarefa {
    private int id;
    private String titulo;
    private String subtitulo;
    private String data;
    private String hora;
    private String descricao;

    public Tarefa() {
        // Construtor vazio
    }

    public Tarefa(String titulo, String subtitulo, String data, String hora, String descricao) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
    }

    // Getters e setters para os campos da tarefa

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
