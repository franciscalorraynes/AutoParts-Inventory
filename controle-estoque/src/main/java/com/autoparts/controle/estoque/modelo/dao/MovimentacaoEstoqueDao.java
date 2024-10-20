
package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.MovimentacaoEstoque;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoEstoqueDao {

    private final Conexao conexao;

    public MovimentacaoEstoqueDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(MovimentacaoEstoque movimentacao) {
        // Verifica se o ID da movimentação é nulo ou zero
        return (movimentacao.getId() == null || movimentacao.getId() == 0L) ? adicionar(movimentacao) : editar(movimentacao);
    }

    private String adicionar(MovimentacaoEstoque movimentacao) {
    String sql = "insert into movimentacaoEstoque (id_peca, quantidade, tipo_movimentacao, data_movimentacao, id_venda, id_fornecedor) values (?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
        preparedStatement.setLong(1, movimentacao.getPecas().getId());
        preparedStatement.setInt(2, movimentacao.getQuantidade());
        preparedStatement.setString(3, movimentacao.getTipoMovimentacao());
        preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(movimentacao.getDataMovimentacao())); // Corrigido
        preparedStatement.setObject(5, movimentacao.getVenda() != null ? movimentacao.getVenda().getId() : null, java.sql.Types.BIGINT);
        preparedStatement.setObject(6, movimentacao.getFornecedor() != null ? movimentacao.getFornecedor().getId() : null, java.sql.Types.BIGINT);

        int resultado = preparedStatement.executeUpdate();
        return resultado == 1 ? "Movimentação adicionada com sucesso!" : "Não foi possível adicionar a movimentação.";
    } catch (SQLException e) {
        return String.format("Erro: %s", e.getMessage());
    }
}


    private String editar(MovimentacaoEstoque movimentacao) {
    String sql = "update movimentacaoEstoque set id_peca=?, tipo_movimentacao=?, quantidade=?, data_movimentacao=?, id_venda=?, id_fornecedor=? where id=?";
    try {
        PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
        preparedStatement.setLong(1, movimentacao.getPecas().getId());
        preparedStatement.setString(2, movimentacao.getTipoMovimentacao());
        preparedStatement.setInt(3, movimentacao.getQuantidade());
        preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(movimentacao.getDataMovimentacao())); // Corrigido
        preparedStatement.setObject(5, movimentacao.getVenda() != null ? movimentacao.getVenda().getId() : null, java.sql.Types.BIGINT);
        preparedStatement.setObject(6, movimentacao.getFornecedor() != null ? movimentacao.getFornecedor().getId() : null, java.sql.Types.BIGINT);
        preparedStatement.setLong(7, movimentacao.getId());

        int resultado = preparedStatement.executeUpdate();
        return resultado == 1 ? "Movimentação editada com sucesso!" : "Não foi possível editar a movimentação.";
    } catch (SQLException e) {
        return String.format("Erro: %s", e.getMessage());
    }
}
     public List<MovimentacaoEstoque> buscarTodas() {
        String sql = "SELECT * FROM movimentacaoEstoque";
        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            while (result.next()) {
                movimentacoes.add(getMovimentacaoEstoque(result));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return movimentacoes;
    }
    
      private MovimentacaoEstoque getMovimentacaoEstoque(ResultSet result) throws SQLException {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setId(result.getLong("id"));

        Pecas pecas = new Pecas(); 
        pecas.setId(result.getLong("id_peca"));
        pecas.setNome(buscarNomePecaPorId(pecas.getId())); // Preenche o nome da peça
        movimentacao.setPecas(pecas);

        movimentacao.setTipoMovimentacao(result.getString("tipo_movimentacao"));
        movimentacao.setQuantidade(result.getInt("quantidade"));
        movimentacao.setDataMovimentacao(result.getTimestamp("data_movimentacao").toLocalDateTime());

        Long idVenda = result.getObject("id_venda", Long.class);
        if (idVenda != null) {
            Venda venda = new Venda(); 
            venda.setId(idVenda);
            movimentacao.setVenda(venda);
        }

        Long idFornecedor = result.getObject("id_fornecedor", Long.class);
        if (idFornecedor != null) {
            Fornecedor fornecedor = new Fornecedor(); 
            fornecedor.setId(idFornecedor);
            fornecedor.setNome(buscarNomeFornecedorPorId(fornecedor.getId())); // Preenche o nome do fornecedor
            movimentacao.setFornecedor(fornecedor);
        }

        return movimentacao;
    }
      
      
      private String buscarNomePecaPorId(Long idPeca) {
        String sql = "SELECT nome FROM pecas WHERE id = ?";
        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
            stmt.setLong(1, idPeca);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getString("nome");
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro ao buscar nome da peça: %s", e.getMessage()));
        }
        return null; // Ou uma string padrão, como "Desconhecido"
    }
      
      private String buscarNomeFornecedorPorId(Long idFornecedor) {
        String sql = "SELECT nome FROM fornecedor WHERE id = ?";
        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
            stmt.setLong(1, idFornecedor);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getString("nome");
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro ao buscar nome do fornecedor: %s", e.getMessage()));
        }
        return null; // Ou uma string padrão, como "Desconhecido"
    }
      
      

    public MovimentacaoEstoque buscarPorId(Long id) {
        String sql = "select * from movimentacaoEstoque where id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getMovimentacaoEstoque(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public String deletarPorId(Long id) {
        String sql = "delete from movimentacaoEstoque where id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Movimentação deletada com sucesso!" : "Erro ao deletar a movimentação.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
    
    public String adicionarPecasAoEstoque(Long idPeca, int quantidade, Long idFornecedor) {
        PecasDao pecasDao = new PecasDao();
        Pecas peca = pecasDao.buscarPecasPeloId(idPeca);

        if (peca == null) {
            return "Erro: Peca não encontrada no banco de dados.";
        }

        String sql = "UPDATE pecas SET quantidade = quantidade + ? WHERE id = ?";
        try (Connection conn = conexao.obterConexao(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantidade);
            preparedStatement.setLong(2, idPeca);
            int resultado = preparedStatement.executeUpdate();

            // Registrar movimentação
            return registrarMovimentacaoEstoque(idPeca, quantidade, "ENTRADA", null, idFornecedor);
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
    
    
    public String subtrairPecasDoEstoque(Long idPeca, int quantidade, Long idVenda) throws SQLException {
    PecasDao pecasDao = new PecasDao();
    Pecas peca = pecasDao.buscarPecasPeloId(idPeca);

    if (peca == null) {
        return "Erro: Peca não encontrada no banco de dados.";
    }

    // Verifica se há estoque suficiente
    if (peca.getQuantidade().longValue() < quantidade) {
        return "Erro: Quantidade insuficiente em estoque.";
    }

    // Atualiza o estoque utilizando o método do PecasDao
    pecasDao.atualizarEstoque(idPeca, quantidade); // Corrigido aqui

    // Registrar movimentação
    return registrarMovimentacaoEstoque(idPeca, quantidade, "SAIDA", idVenda, null);
}


    public int verificarQuantidadeEstoque(Long idPeca) {
        PecasDao pecasDao = new PecasDao();
        Pecas peca = pecasDao.buscarPecasPeloId(idPeca);

        if (peca == null) {
            System.out.println("Erro: Peca não encontrada no banco de dados.");
            return -1; // Retorna -1 em caso de erro
        }

        return peca.getQuantidade().intValue();
    }

    public String registrarMovimentacaoEstoque(Long idPeca, int quantidade, String tipoMovimentacao, Long idVenda, Long idFornecedor) {
        String sql = "INSERT INTO movimentacaoEstoque (id_peca, quantidade, tipo_movimentacao, id_venda, id_fornecedor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexao.obterConexao(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, idPeca);
            preparedStatement.setInt(2, quantidade);
            preparedStatement.setString(3, tipoMovimentacao);

            // Definindo os IDs opcionalmente
            preparedStatement.setObject(4, idVenda, java.sql.Types.BIGINT); // Usado para saídas
            preparedStatement.setObject(5, idFornecedor, java.sql.Types.BIGINT); // Usado para entradas

            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Movimentação registrada com sucesso!" : "Erro ao registrar movimentação.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
    public int getQuantidadeEmEstoque(Long pecaId) throws SQLException {
    String sql = "SELECT quantidade FROM pecas WHERE id = ?";
    try (Connection conn = conexao.obterConexao();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setLong(1, pecaId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("quantidade");
        } else {
            return 0; // 
        }
    }
}

    public void registrarSaidaEstoque(Long idPeca, int quantidade) throws SQLException {
    String sqlMovimentacao = "INSERT INTO movimentacaoEstoque (id_peca, quantidade, tipo_movimentacao, data_movimentacao) VALUES (?, ?, 'SAIDA', NOW())";
    String sqlAtualizarPecas = "UPDATE pecas SET quantidade = quantidade - ? WHERE id = ?";
    
    try (Connection conn = conexao.obterConexao()) {
        // Inicia a transação
        conn.setAutoCommit(false);
        try (PreparedStatement stmtMovimentacao = conn.prepareStatement(sqlMovimentacao);
             PreparedStatement stmtPecas = conn.prepareStatement(sqlAtualizarPecas)) {
            
            // Registrar movimentação
            stmtMovimentacao.setLong(1, idPeca);
            stmtMovimentacao.setInt(2, quantidade);
            stmtMovimentacao.executeUpdate();

            // Atualizar tabela pecas
            stmtPecas.setInt(1, quantidade);
            stmtPecas.setLong(2, idPeca);
            stmtPecas.executeUpdate();

            // Commit da transação
            conn.commit();
        } catch (SQLException e) {
            // Se ocorrer um erro, rollback
            conn.rollback();
            throw new SQLException("Erro ao registrar saída de estoque: " + e.getMessage());
        } finally {
            // Restaura o estado de auto-commit
            conn.setAutoCommit(true);
        }
    }
}   
}