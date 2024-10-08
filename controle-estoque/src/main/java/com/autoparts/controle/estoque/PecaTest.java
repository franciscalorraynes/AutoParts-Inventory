/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.PecasDao;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import java.math.BigDecimal;


/**
 *
 * @author Lorrayne
 */
public class PecaTest {
      public static void main(String[] args) {
        // Criação de um fornecedor para associar à peça
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1L); // Defina um ID válido para o fornecedor

        // Criar uma nova peça
        Pecas peca = new Pecas(null, "Macaco", "Auxilia na remoção de pneu", BigDecimal.valueOf(10), BigDecimal.valueOf(59.90), null, fornecedor);
        
        // Usar o PecasDao para salvar a peça
        PecasDao pecasDao = new PecasDao(); // Certifique-se de ter essa classe
        String mensagem = pecasDao.salvar(peca); // Supondo que você tenha um método salvar em PecasDao
        System.out.println(mensagem);
    } 
}
