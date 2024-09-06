package com.autoparts.modelo.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venda {
    private Long id;
    private Cliente cliente;
    private Usuario usuario;
    private BigDecimal totalDaVenda;
    private BigDecimal desconto;
    private BigDecimal troco;
    private String observacao;
    private LocalDateTime dataVenda;

    public Venda() {
    }

    public Venda(Long id, Cliente cliente, Usuario usuario, BigDecimal totalDaVenda, BigDecimal desconto, BigDecimal troco, String observacao, LocalDateTime dataVenda) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.totalDaVenda = totalDaVenda;
        this.desconto = desconto;
        this.troco = troco;
        this.observacao = observacao;
        this.dataVenda = dataVenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getTotalDaVenda() {
        return totalDaVenda;
    }

    public void setTotalDaVenda(BigDecimal totalDaVenda) {
        this.totalDaVenda = totalDaVenda;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

}
