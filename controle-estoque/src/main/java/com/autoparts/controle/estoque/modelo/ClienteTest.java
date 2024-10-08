package com.autoparts.controle.estoque.modelo;
import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;

public class ClienteTest {

    public static void main(String[] args) {
        Cliente cliente = new Cliente(null, "Samira Franca", "84981327173", "Rua Jose Daniel de Souza");
 
        ClienteDao clienteDao = new ClienteDao();
        String menssagem = clienteDao.salvar(cliente);
        System.out.println(menssagem);
    }   
    
}

