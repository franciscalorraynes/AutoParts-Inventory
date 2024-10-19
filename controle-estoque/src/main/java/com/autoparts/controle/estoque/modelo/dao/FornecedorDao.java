package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDao {

    private final Conexao conexao;

    public FornecedorDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(Fornecedor fornecedor) {
        // Quando for 0 novo cadastro, !0 editar
        return fornecedor.getId() == null || fornecedor.getId() == 0L ? adicionarFornecedor(fornecedor) : editarFornecedor(fornecedor);
    }

    private String adicionarFornecedor(Fornecedor fornecedor) {
        String sql = "insert into fornecedor (nome, cpf, telefone, endereco) VALUES (?,?,?,?)";

        // Log do nome do fornecedor que está sendo adicionado
        System.out.println("Tentando adicionar fornecedor: " + fornecedor.getNome());

        // Validação para caso o fornecedor já exista
        Fornecedor fornecedorTemp = buscarFornecedorPeloNome(fornecedor.getNome());
        if (fornecedorTemp != null) {
            return String.format("Erro: fornecedor %s já existe no banco de dados", fornecedor.getNome());
        }

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, fornecedor);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Fornecedor Adicionado com Sucesso!" : "Não foi possível adicionar o fornecedor";

        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editarFornecedor(Fornecedor fornecedor) {
        String sql = "update fornecedor set nome = ?, cpf=?, telefone=?, endereco=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, fornecedor);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Fornecedor editado com sucesso!" : "Não foi possível editar o fornecedor";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, Fornecedor fornecedor) throws SQLException {
        preparedStatement.setString(1, fornecedor.getNome());
        preparedStatement.setString(2, fornecedor.getCpf());
        preparedStatement.setString(3, fornecedor.getTelefone());
        preparedStatement.setString(4, fornecedor.getEndereco());

        if (fornecedor.getId() != null && fornecedor.getId() != 0L) {
            preparedStatement.setLong(5, fornecedor.getId());
        }
    }

    public List<Fornecedor> buscarFornecedores() {
        String sql = "select * from fornecedor";
        List<Fornecedor> fornecedores = new ArrayList<>();
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            while (result.next()) {
                fornecedores.add(getFornecedor(result));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return fornecedores;
    }

    private Fornecedor getFornecedor(ResultSet result) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setId(result.getLong("id"));
        fornecedor.setNome(result.getString("nome"));
        fornecedor.setCpf(result.getString("cpf"));
        fornecedor.setTelefone(result.getString("telefone"));
        fornecedor.setEndereco(result.getString("endereco"));

        return fornecedor;
    }

    public Fornecedor buscarFornecedorPeloId(Long id) {
        String sql = String.format("select * from fornecedor where id = %d", id);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            if (result.next()) {
                return getFornecedor(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public Fornecedor buscarFornecedorPeloNome(String nomeFornecedor) {
        String sql = "select * from fornecedor where nome = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, nomeFornecedor);
            ResultSet result = preparedStatement.executeQuery();

            // Log do nome que está sendo buscado
            System.out.println("Buscando fornecedor pelo nome: " + nomeFornecedor);
            
            if (result.next()) {
                return getFornecedor(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public String deletarPeloId(Long id) {
        // Verifica se o fornecedor com o ID fornecido existe
        Fornecedor fornecedor = buscarFornecedorPeloId(id);
        if (fornecedor == null) {
            return "Erro: Fornecedor não encontrado no banco de dados.";
        }

        String sql = "delete from fornecedor WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id); // Define o ID do fornecedor a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado == 1 ? "Fornecedor deletado com sucesso!" : "Erro ao deletar o fornecedor.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public String deletarPorNome(String nome) {
        // Verifica se o nome do fornecedor existe
        Fornecedor fornecedortemFornecedor = buscarFornecedorPeloNome(nome);
        if (fornecedortemFornecedor == null) {
            return "Erro: fornecedor não encontrado no banco de dados.";
        }

        String sql = "delete from fornecedor WHERE nome = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, nome); // Define o nome do fornecedor a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado > 0 ? "Fornecedor(s) deletado(s) com sucesso!" : "Erro ao deletar o fornecedor.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
}
