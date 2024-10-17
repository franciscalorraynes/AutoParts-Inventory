package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.exception.NegocioException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsuarioDao {
    private final Conexao conexao;

    public UsuarioDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(Usuario usuario) {
        return usuario.getId() == null ? adicionar(usuario) : editar(usuario);
    }

    private String adicionar(Usuario usuario) {
        String sql = "INSERT INTO usuario(nome, usuario, senha, telefone, perfil, estado) VALUES (?,?,?,?,?,?)";
        
        Usuario usuarioTemp = buscarUsuarioPeloNome(usuario.getNomeUsuario());
        if (usuarioTemp != null) {
            throw new NegocioException(String.format("Erro: username %s já existe no banco de dados", usuario.getNomeUsuario()));
        }

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preencherValoresDePreparedStatement(preparedStatement, usuario);
            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getLong(1));
                }
                return "Usuario adicionado com sucesso!";
            } else {
                return "Nao foi possivel adicionar o usuario";
            }
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome=?, usuario=?, senha=?, telefone=?, perfil=?, estado=? WHERE id=?";
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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrypto = passwordEncoder.encode(usuario.getSenha());

        preparedStatement.setString(1, usuario.getNome());
        preparedStatement.setString(2, usuario.getNomeUsuario());
        preparedStatement.setString(3, senhaCrypto);
        preparedStatement.setString(4, usuario.getTelefone());
        preparedStatement.setString(5, usuario.getPerfil().name());
        preparedStatement.setBoolean(6, usuario.isEstado());

        if (usuario.getId() != null) {
            preparedStatement.setLong(7, usuario.getId());
        }
    }

    public List<Usuario> buscarUsuarios() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            while (result.next()) {
                usuarios.add(getUsuario(result));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return usuarios;
    }

    private Usuario getUsuario(ResultSet result) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        usuario.setNomeUsuario(result.getString("usuario"));
        usuario.setSenha(result.getString("senha"));
        usuario.setTelefone(result.getString("telefone"));

        String perfilStr = result.getString("perfil");
        if (perfilStr != null) {
            usuario.setPerfil(Perfil.valueOf(perfilStr));
        } else {
            usuario.setPerfil(null);
        }

        usuario.setEstado(result.getBoolean("estado"));
        usuario.setDataHoraCriacao(result.getObject("data_hora_criacao", LocalDateTime.class));
        usuario.setUltimoLogin(result.getObject("ultimo_login", LocalDateTime.class));

        return usuario;
    }

    public Usuario buscarUsuarioPeloId(Long id) {
        String sql = String.format("SELECT * FROM usuario WHERE id = %d", id);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            if (result.next()) {
                return getUsuario(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public Usuario buscarUsuarioPeloNome(String nomeUsuario) {
        String sql = "SELECT * FROM usuario WHERE usuario = ?";
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

    public boolean verificarSenha(String senhaSalva, String senhaInformada) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(senhaInformada, senhaSalva);
    }
}
