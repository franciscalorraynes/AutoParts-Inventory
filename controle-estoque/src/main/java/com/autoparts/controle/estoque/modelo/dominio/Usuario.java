/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dominio;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 *
 * @author fllsa
 */
public class Usuario {
    private Long id;
    private String nome;
    private String senha;
    private String nomeUsuario;
    private String telefone;
    private Perfil perfil;
    private boolean estado;
    private LocalDateTime dataHoraCriacao; 
    private LocalDateTime ultimoLogin;  

    public Usuario() {
        this.estado = true;
    }

    public Usuario(Long id, String nome, String senha, String nomeUsuario, String telefone, Perfil perfil, LocalDateTime dataHoraCriacao, LocalDateTime ultimoLogin) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.nomeUsuario = nomeUsuario;
        this.telefone = telefone;
        this.perfil = perfil;
        this.dataHoraCriacao = dataHoraCriacao;
        this.ultimoLogin = ultimoLogin;
        this.estado = true;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.id, other.id);
    }
    
    
    public void reset(){
        this.estado = true;
    }
    public void mudarEstado(){
        this.estado = !this.estado;
    }

}
