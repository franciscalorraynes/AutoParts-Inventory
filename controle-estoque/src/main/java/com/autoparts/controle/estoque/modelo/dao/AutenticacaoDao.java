/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.exception.NegocioException;
import com.autoparts.controle.estoque.view.modelo.LoginDTO;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Lorrayne
 */
public class AutenticacaoDao {
   private final UsuarioDao usuarioDao;

    public AutenticacaoDao() {
        this.usuarioDao = new UsuarioDao();
    }
    
    public boolean temPermissao(Usuario usuario) {
        try {
            permissao(usuario);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Usuario sem permissao", 0);
            return false;
        }
    }
    
    private void permissao(Usuario usuario) {
        if (!Perfil.ADM.equals(usuario.getPerfil())) {
            throw new NegocioException("Sem permissao para realizar essa acao");
        }
    }
    
    //DTO são os valores que irão vir do formulario, como o username, usuario e senha
    public Usuario login(LoginDTO login) {
        System.out.println("Tentando fazer login com o usuario: " + login.getUsuario());
        
        // Buscar usuário no banco de dados
        Usuario usuario = usuarioDao.buscarUsuarioPeloNome(login.getUsuario());
        
        // Verifica se o usuário foi encontrado
        if (usuario == null) {
            System.out.println("Usuario não encontrado.");
            JOptionPane.showMessageDialog(null, "Usuário nao encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Verifica se o usuário está desativado
        if (!usuario.isEstado()) {
            System.out.println("Usuario está desativado.");
            JOptionPane.showMessageDialog(null, "Usuario está desativado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        System.out.println("Usuario encontrado. Verificando senha...");
        
        // Verifica se a senha está correta
        if (validaSenha(usuario.getSenha(), login.getSenha())) {
            System.out.println("Login bem-sucedido!");
            return usuario;
        } else {
            System.out.println("Senha incorreta.");
            JOptionPane.showMessageDialog(null, "Usuario ou senha invalidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    // Usando o SpringSecurity
    private boolean validaSenha(String senhaUsuario, String senhaLogin) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(senhaLogin, senhaUsuario);
    }
    
    
    
}
