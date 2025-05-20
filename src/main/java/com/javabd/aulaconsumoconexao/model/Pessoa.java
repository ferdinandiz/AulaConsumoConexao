package com.javabd.aulaconsumoconexao.model;

public class Pessoa {
    private Integer id;
    private String nome;
    private String cpf;
    private String endereco;
    private String data_nasc;

    public Pessoa(String nome, String cpf, String endereco, String data_nasc) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.data_nasc = data_nasc;
    }

    public Pessoa(Integer id, String nome, String cpf, String endereco, String data_nasc) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.data_nasc = data_nasc;
    }

    public Pessoa() {
    }

    //Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }
}
