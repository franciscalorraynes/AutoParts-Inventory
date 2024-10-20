/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Samira
 */
public class OrdemServico {
    private Long id;
    private LocalDateTime dataOs;
    private String equipamento;
    private String defeito;
    private String servicoPrestado;
    private String funcionarioResponsavel;
    private BigDecimal valor;
    Cliente cliente;

    public OrdemServico() {

    }

    public OrdemServico(Long id, LocalDateTime dataOs, String equipamento, String defeito, String servicoPrestado,
            String funcionarioResponsavel, BigDecimal valor, Cliente cliente) {
        this.id = id;
        this.dataOs = dataOs;
        this.equipamento = equipamento;
        this.defeito = defeito;
        this.servicoPrestado = servicoPrestado;
        this.funcionarioResponsavel = funcionarioResponsavel;
        this.valor = valor;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataOs() {
        return dataOs;
    }

    public void setDataOs(LocalDateTime dataOs) {
        this.dataOs = dataOs;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getDefeito() {
        return defeito;
    }

    public void setDefeito(String defeito) {
        this.defeito = defeito;
    }

    public String getServicoPrestado() {
        return servicoPrestado;
    }

    public void setServicoPrestado(String servicoPrestado) {
        this.servicoPrestado = servicoPrestado;
    }

    public String getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }

    public void setFuncionarioResponsavel(String funcionarioResponsavel) {
        this.funcionarioResponsavel = funcionarioResponsavel;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    

}
