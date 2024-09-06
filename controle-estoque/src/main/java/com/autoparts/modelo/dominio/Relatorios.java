package com.autoparts.modelo.dominio;

import java.time.LocalDateTime;

public class Relatorios {
    private Long id;
    private String nomeRelatorio;
    private String conteudoRelatorio;
    private LocalDateTime dataCriacao;
    private Usuario usuario;

    public Relatorios() {
    }

    public Relatorios(Long id, String nomeRelatorio, String conteudoRelatorio, LocalDateTime dataCriacao,
            Usuario usuario) {
        this.id = id;
        this.nomeRelatorio = nomeRelatorio;
        this.conteudoRelatorio = conteudoRelatorio;
        this.dataCriacao = dataCriacao;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }

    public String getConteudoRelatorio() {
        return conteudoRelatorio;
    }

    public void setConteudoRelatorio(String conteudoRelatorio) {
        this.conteudoRelatorio = conteudoRelatorio;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
