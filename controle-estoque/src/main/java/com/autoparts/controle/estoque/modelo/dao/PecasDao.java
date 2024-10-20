package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PecasDao {

    private final Conexao conexao;

    public PecasDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(Pecas pecas) {
        // Verifica se o ID da peça é nulo ou zero
        return (pecas.getId() == null || pecas.getId() == 0L) ? adicionar(pecas) : editar(pecas);
    }

    private String adicionar(Pecas pecas) {
        String sql = "INSERT INTO pecas (nome, descricao, quantidade, preco, id_fornecedor) VALUES (?, ?, ?, ?, ?)";
        Pecas pecasTemp = buscarPecasPeloNome(pecas.getNome());
        if (pecasTemp != null) {
            return String.format("Erro: peca %s ja existe no banco de dados", pecas.getNome());
        }

        try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherValoresDePreparedStatement(preparedStatement, pecas);
            // Adiciona o ID do fornecedor
            if (pecas.getFornecedor() != null) {
                preparedStatement.setLong(5, pecas.getFornecedor().getId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.BIGINT); // Se o fornecedor não existir, define como NULL
            }

            int resultado = preparedStatement.executeUpdate();
            if (resultado == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pecas.setId(generatedKeys.getLong(1)); // Atribui o ID gerado
                    }
                }
                return "Peca adicionada com sucesso!";
            } else {
                return "Nao foi possível adicionar a peca";
            }
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editar(Pecas pecas) {
        String sql = "UPDATE pecas SET nome=?, descricao=?, quantidade=?, preco=?, id_fornecedor=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, pecas);
            // Adiciona o ID da peça que está sendo editada
            preparedStatement.setLong(6, pecas.getId());
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Peca editada com sucesso!" : "Nao foi possível editar a peca";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, Pecas pecas) throws SQLException {
        preparedStatement.setString(1, pecas.getNome());
        preparedStatement.setString(2, pecas.getDescricao());
        preparedStatement.setInt(3, pecas.getQuantidade());
        preparedStatement.setBigDecimal(4, pecas.getPreco());

        // Aqui você deve adicionar o ID do fornecedor se ele estiver presente
        if (pecas.getFornecedor() != null) {
            preparedStatement.setLong(5, pecas.getFornecedor().getId());
        } else {
            preparedStatement.setNull(5, java.sql.Types.BIGINT); // Se o fornecedor não existir, define como NULL
        }
    }

    public List<Pecas> buscarPecas() {
        String sql = "SELECT p.id, p.nome, p.descricao, p.quantidade, p.preco, p.data_criacao, f.id AS id_fornecedor, f.nome AS fornecedor "
                + "FROM pecas p "
                + "JOIN fornecedor f ON p.id_fornecedor = f.id";
        List<Pecas> pecas = new ArrayList<>();
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            while (result.next()) {
                pecas.add(getPecas(result));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return pecas;
    }

    private Pecas getPecas(ResultSet result) throws SQLException {
        Pecas pecas = new Pecas();
        pecas.setId(result.getLong("id"));
        pecas.setNome(result.getString("nome"));
        pecas.setDescricao(result.getString("descricao"));
        pecas.setQuantidade(result.getInt("quantidade"));
        pecas.setPreco(result.getBigDecimal("preco"));
        pecas.setDataCriacao(result.getObject("data_criacao", LocalDateTime.class));

        // Obter o fornecedor
        Long idFornecedor = result.getLong("id_fornecedor");
        if (!result.wasNull()) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(idFornecedor);  // Define o ID do fornecedor
            fornecedor.setNome(result.getString("id_fornecedor")); // Define o nome do fornecedor
            pecas.setFornecedor(fornecedor);
        }
        return pecas;
    }

    public Pecas buscarPecasPeloId(Long id) {
        String sql = "SELECT * FROM pecas WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getPecas(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public Pecas buscarPecasPeloNome(String nome) {
        String sql = "SELECT * FROM pecas WHERE nome = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, nome);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getPecas(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public String deletarPeloId(Long id) {
        // Verifica se a peca com o ID fornecido existe
        Pecas pecas = buscarPecasPeloId(id);
        if (pecas == null) {
            return "Erro: Peca não encontrado no banco de dados.";
        }

        String sql = "delete from pecas WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id); // Define o ID da peca a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado == 1 ? "Peca deletado com sucesso!" : "Erro ao deletar o peca.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public String deletarPorNome(String nome) {
        // ve se o nome do cliente existe
        Pecas pecastemPecas = buscarPecasPeloNome(nome);
        if (pecastemPecas == null) {
            return "Erro: Pecas não encontrado no banco de dados.";
        }

        String sql = "delete from cliente WHERE nome = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, nome); // Define o nome do cliente a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado > 0 ? "Pecas deletada(s) com sucesso!" : "Erro ao deletar pecas.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public void atualizarEstoque(Long idPeca, int quantidade) throws SQLException {
        String sql = "UPDATE pecas SET quantidade = quantidade - ? WHERE id = ?";
        try (Connection conn = conexao.obterConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setLong(2, idPeca);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Erro: Nenhuma peça encontrada com o ID " + idPeca);
            }
        }
    }

}
