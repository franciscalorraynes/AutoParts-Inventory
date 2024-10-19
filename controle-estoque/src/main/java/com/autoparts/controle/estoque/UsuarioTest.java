package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;

public class UsuarioTest {
    public static void main(String[] args) {
        Usuario usuario = new Usuario(null, "Francisca Lorrayne", "franciscalorrayne", "980423", "84981751522", Perfil.ADM, null, null);


        
        UsuarioDao usuarioDao = new UsuarioDao();
        String mensagem = usuarioDao.salvar(usuario);

        // Exibindo a mensagem de sucesso ou erro
        System.out.println(mensagem);
    }   
}
