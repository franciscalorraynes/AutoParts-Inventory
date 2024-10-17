package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    private final Conexao conexao;

    public ClienteDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(Cliente cliente) {
        // Quando for 0 o novo cadastro, se não, editar
        return (cliente.getId() == null || cliente.getId() == 0L) ? adicionar(cliente) : editar(cliente);
    }

    public String adicionar(Cliente cliente) {
        String sql = "insert into cliente(nome, telefone, endereco) VALUES (?,?, ?)";
        
        // Verifica se o cliente já existe
        Cliente clienteTemp = buscarClientePeloNome(cliente.getNome());
        if (clienteTemp != null) {
            return String.format("Erro: cliente já existe no banco de dados", cliente);
        }
        
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, cliente);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Cliente adicionado com sucesso!" : "Não foi possível adicionar o cliente.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public String editar(Cliente cliente) {
        String sql = "update cliente set nome=?, telefone=?, endereco=?  WHERE id=?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, cliente);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Cliente editado com sucesso!" : "Não foi possível editar o cliente.";
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
    String sql = "SELECT * FROM cliente";
    List<Cliente> clientes = new ArrayList<>();
    
    // Utilizando try-with-resources para fechar automaticamente os recursos
    try (Connection conn = conexao.obterConexao();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet result = stmt.executeQuery()) {

        // Percorrendo os resultados e populando a lista de clientes
        while (result.next()) {
            clientes.add(getCliente(result));
        }

    } catch (SQLException e) {
        System.out.println("Erro ao buscar clientes: " + e.getMessage());
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
        String sql = "select * from cliente where id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

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
