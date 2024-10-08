/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lorrayne
 */
public class ClienteDao {

    private final Conexao conexao;

    public ClienteDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(Cliente cliente) {
        // quando for 0 o novo cadastro =, !0 editar

        return (cliente.getId() == null || cliente.getId() == 0L) ? adicionar(cliente) : editar(cliente);

    }

    private String adicionar(Cliente cliente) {
        String sql = "insert into cliente(nome, telefone, endereco) VALUES (?,?, ?)";
        // vai analisar os parametros passados e inserir o PreparedStatement (para criar
        // objetos que pré-compilam instruções SQL)

        // validação para caso o usuario já existe
        Cliente clienteTemp = buscarClientePeloNome(cliente.getNome());
        if (clienteTemp != null) {
            return String.format("Erro: cliente já existe no banco de dados", cliente);

        }
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, cliente);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Cliente adicionado com sucesso!" : "Nao foi possivel adicionar o usuario";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editar(Cliente cliente) {
        String sql = "update cliente set nome=?, telefone=?, endereco=?  WHERE id=?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, cliente);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Cliente editado com sucesso!" : "Nao foi possivel editar o cliente";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, Cliente cliente)
            throws SQLException {

        preparedStatement.setString(1, cliente.getNome());
        preparedStatement.setString(2, cliente.getTelefone());
        preparedStatement.setString(3, cliente.getEndereco());

        if (cliente.getId() != null && cliente.getId() != 0L) {
            preparedStatement.setLong(4, cliente.getId());
        }

    }

    public List<Cliente> buscarClientes() {

        String sql = "select * from cliente";
        List<Cliente> clientes = new ArrayList<>();

        try {

            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery(sql);

            while (result.next()) {
                clientes.add(getCliente(result));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return clientes;
    }

    private Cliente getCliente(ResultSet result) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(result.getLong("id"));
        cliente.setNome(result.getString("nome"));
        cliente.setTelefone(result.getString("telefone"));
        cliente.setEndereco(result.getString("endereco"));

        return cliente;
    }

    public Cliente buscarClientePeloId(Long id) {
        String sql = String.format("select *from cliente where id =%d", id);
        try {

            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery(sql);

            if (result.next()) {
                return getCliente(result);

            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public Cliente buscarClientePeloNome(String nomeCliente) {
        String sql = "select * from cliente where nome = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, nomeCliente);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getCliente(result);

            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public String deletarPeloId(Long id) {
        // Verifica se o cliente com o ID fornecido existe
        Cliente cliente = buscarClientePeloId(id);
        if (cliente == null) {
            return "Erro: Cliente não encontrado no banco de dados.";
        }

        String sql = "delete from cliente WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id); // Define o ID do cliente a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado == 1 ? "Cliente deletado com sucesso!" : "Erro ao deletar o cliente.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public String deletarPorNome(String nome) {
        // ve se o nome do cliente existe
        Cliente clienteTemp = buscarClientePeloNome(nome);
        if (clienteTemp == null) {
            return "Erro: Cliente não encontrado no banco de dados.";
        }

        String sql = "delete from cliente WHERE nome = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, nome); // Define o nome do cliente a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado > 0 ? "Cliente(s) deletado(s) com sucesso!" : "Erro ao deletar o cliente.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
}