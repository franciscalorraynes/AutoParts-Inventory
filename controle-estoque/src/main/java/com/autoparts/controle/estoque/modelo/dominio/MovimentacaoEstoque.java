package com.autoparts.controle.estoque.modelo.dominio;

import java.time.LocalDateTime;

public class MovimentacaoEstoque {

    private Long id; 
    private Pecas pecas;    
    private String tipoMovimentacao; 
    private Integer quantidade;
    private LocalDateTime dataMovimentacao;  
    private Venda venda; 
    private Fornecedor fornecedor;

    public MovimentacaoEstoque() {

    }

    public MovimentacaoEstoque(Long id, Pecas pecas, String tipoMovimentacao, Integer quantidade, LocalDateTime dataMovimentacao, Venda venda, Fornecedor fornecedor) {
        this.id = id;
        this.pecas = pecas;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.dataMovimentacao = dataMovimentacao;
        this.venda = venda;
        this.fornecedor = fornecedor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pecas getPecas() {
        return pecas;
    }

    public void setPecas(Pecas pecas) {
        this.pecas = pecas;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    
    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

   
}