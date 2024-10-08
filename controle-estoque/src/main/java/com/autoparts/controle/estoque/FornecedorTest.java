/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.FornecedorDao;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor;

/**
 *
 * @author Lorrayne
 */
public class FornecedorTest {
    public static void main(String[] args) {
    Fornecedor fornecedor = new Fornecedor(6L, "Josefina Andrade", "001.002.003-00", "1234567", "Rua Sem Fim, Nº 10");
    
    FornecedorDao fornecedorDao = new FornecedorDao();
    String menssaString = fornecedorDao.salvar(fornecedor);
        System.out.println(menssaString);
    }
    

    
}
