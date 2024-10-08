/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;

/**
 *
 * @author Lorrayne
 */
public class ClienteTest {
    public static void main(String[] args) {
    
        ClienteDao clienteDao = new ClienteDao();
    
    String nomeParaBuscar = "Maria Silva"; // Substitua pelo ID do cliente que deseja buscar
        Cliente cliente = clienteDao.buscarClientePeloNome(nomeParaBuscar);
    
    if (cliente != null) {
        System.out.println("Cliente encontrado: ");
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("Telefone: " + cliente.getTelefone());
        System.out.println("Endereço: " + cliente.getEndereco());
    } else {
        System.out.println("Cliente não encontrado.");
    }

    }
}
