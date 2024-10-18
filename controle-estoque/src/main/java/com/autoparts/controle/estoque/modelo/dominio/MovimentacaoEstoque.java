package com.autoparts.controle.estoque.modelo.dominio;

import java.time.LocalDate;
import java.util.Objects;

public class MovimentacaoEstoque {

    private Long id; 
    private Long idPeca;    
    private String tipoMovimentacao; 
    private LocalDate dataMovimentacao;  
    private Long idVenda; 
    private Long idFornecedor;

    public MovimentacaoEstoque() {

    }
    public MovimentacaoEstoque(Long id, Long idPeca, String tipoMovimentacao, LocalDate dataMovimentacao, Long idVenda, Long idFornecedor) {
        this.id = id;
        this.idPeca = idPeca;
        this.tipoMovimentacao = tipoMovimentacao;
        this.dataMovimentacao = dataMovimentacao;
        this.idVenda = idVenda;
        this.idFornecedor = idFornecedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPeca() {
        return idPeca;
    }

    public void setIdPeca(Long idPeca) {
        this.idPeca = idPeca;
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public LocalDate getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDate dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovimentacaoEstoque other = (MovimentacaoEstoque) obj;
        return Objects.equals(this.id, other.id);
    }

}
