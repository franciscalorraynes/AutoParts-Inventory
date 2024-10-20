/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Lorrayne
 */
public class Pecas {
    private Long id;
    private String nome;
    private String descricao ;
    private Integer quantidade;
    private BigDecimal preco ;
    private LocalDateTime dataCriacao;
    Fornecedor fornecedor;  

    public Pecas() {
    }

    public Pecas(Long id, String nome, String descricao, Integer quantidade, BigDecimal preco, LocalDateTime dataCriacao, Fornecedor fornecedor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataCriacao = dataCriacao;
        this.fornecedor = fornecedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
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

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    public Long getIdFornecedor() {
    return fornecedor != null ? fornecedor.getId() : null; // Retorna o ID do fornecedor se existir
}

    
 @Override
    public String toString() {
        return nome;  // Isso fará com que o JComboBox mostre apenas o nome da peça
    }
}
