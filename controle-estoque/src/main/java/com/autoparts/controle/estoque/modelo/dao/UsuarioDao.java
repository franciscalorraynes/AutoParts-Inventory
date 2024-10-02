/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lorrayne
 */
public class UsuarioDao {

    private final Conexao conexao;

    public UsuarioDao() {
        //inversao de controle 
        this.conexao = new ConexaoMySql();
    }

    //
    public String salvar(Usuario usuario) {
        //quando for 0 novo cadastro, !0 editar
        return usuario.getId() == 0L ? adicionar(usuario) : editar(usuario);
    }

    private String adicionar(Usuario usuario) {
        String sql = "insert into usuario(nome, usuario, senha, telefone, perfil, estado) VALUES (?,?,?,?,?,?)";
        //vai analisar os parametros passados e inserir o PreparedStatement (para criar objetos que pré-compilam instruções SQL)  
        
        //validação para caso o usuario já exista
        Usuario usuarioTemp = buscarUsuarioPeloNome(usuario.getNomeUsuario());
        if(usuarioTemp != null){
            return String.format("Erro: username %s já existe no banco de dados", usuario.getNomeUsuario());
        }
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, usuario);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Usuario adicionado com sucesso!" : "Nao foi possivel adicionar o usuario";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editar(Usuario usuario) {
        String sql = "update usuario set nome=?, usuario=?, senha=?,telefone=?, perfil=?, estado=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, usuario);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Usuario editado com sucesso!" : "Nao foi possivel editar o usuario";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }

    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, Usuario usuario) throws SQLException {
        preparedStatement.setString(1, usuario.getNome());
        preparedStatement.setString(2, usuario.getNomeUsuario());
        preparedStatement.setString(3, usuario.getSenha());
        preparedStatement.setString(4, usuario.getTelefone());        
        preparedStatement.setString(5, usuario.getPerfil().name());
        preparedStatement.setBoolean(6, usuario.isEstado());

        //se o usuario for diferente de zero, ele vai cadastrar 
        if (usuario.getId() != 0L) {
            preparedStatement.setLong(7, usuario.getId());
        }
    }
    public List<Usuario> buscarUsuarios(){
        String sql = "select *from usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try {
          
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery(sql);
            
            while (result.next()) {
               usuarios.add(getUsuario(result));    
                
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return usuarios;
    }
    
    private Usuario getUsuario(ResultSet result) throws SQLException{
        Usuario usuario = new Usuario();
        
        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        usuario.setNomeUsuario(result.getString("usuario"));
        usuario.setSenha(result.getString("senha"));
        usuario.setTelefone(result.getString("telefone"));
        usuario.setPerfil(result.getObject("perfil", Perfil.class));
        usuario.setEstado(result.getBoolean("estado"));
        usuario.setDataHoraCriacao(result.getObject("data_hora_criacao", LocalDateTime.class));
        usuario.setUltimoLogin(result.getObject("ultimo_login", LocalDateTime.class));
        
        return usuario;
    }
    
    public Usuario buscarUsuarioPeloId(Long id){
        String sql = String.format("select *from usuario where id =%d", id);
           try {
          
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery(sql);
            
            if (result.next()) {
               return getUsuario(result);    
                
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }
 public Usuario buscarUsuarioPeloNome(String nomeUsuario) {
    String sql = "select * from usuario where nome = ?";
    try {
        PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
        preparedStatement.setString(1, nomeUsuario);
        ResultSet result = preparedStatement.executeQuery();
        
        if (result.next()) {
            return getUsuario(result);    
        }
    } catch (SQLException e) {
        System.out.println(String.format("Erro:  %s", e.getMessage()));
    }
    return null;
}
    
}
