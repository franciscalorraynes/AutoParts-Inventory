/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
//import java.time.LocalDateTime;
//import java.time.LocalDateTime;

/**
 *
 * @author Lorrayne
 */
public class UsuarioTest {
    public static void main(String[] args) {
        Usuario usuario = new Usuario(3L, "Samira Aquino", "samiraaquino", "samira2017", "84981751522", Perfil.ADM, null, null);


        
        UsuarioDao usuarioDao = new UsuarioDao();
        String menssagem = usuarioDao.salvar(usuario);
        System.out.println(menssagem);
    }   
    
}
