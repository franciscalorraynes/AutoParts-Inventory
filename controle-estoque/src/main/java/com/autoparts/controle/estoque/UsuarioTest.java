package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;

public class UsuarioTest {
    public static void main(String[] args) {
        // Criando o usuário com ID nulo
        Usuario usuario = new Usuario(null, "Samira Aquino", "samiraaqquino", "samira2017", "84981525647", Perfil.ADM, null, null);

        // Salvando o usuário no banco de dados
        UsuarioDao usuarioDao = new UsuarioDao();
        String mensagem = usuarioDao.salvar(usuario);

        // Exibindo a mensagem de sucesso ou erro
        System.out.println(mensagem);
    }   
}
