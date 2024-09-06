package com.autoparts.modelo.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Pecas {
    private Long id;
    private Fornecedor fornecedor;
    private String nome;
    private String descricao;
    private int quantidade;
    private BigDecimal preco;
    private LocalDateTime dataCriacao;

    public Pecas() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Pecas(Long id, Fornecedor fornecedor, String nome, String descricao, int quantidade, BigDecimal preco, LocalDateTime dataCriacao) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }   
    
    public void setId(Long id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pecas pecas = (Pecas) o;
        return Objects.equals(id, pecas.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
