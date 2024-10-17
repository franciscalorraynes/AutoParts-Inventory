package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDao {

    private final Conexao conexao;

    public VendaDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(Venda venda) throws SQLException {
        // Quando for 0, significa novo cadastro, senão edição
        return venda.getId() == null || venda.getId() == 0L ? adicionar(venda) : editar(venda);
    }

    private String adicionar(Venda venda) {
        String sql = "INSERT INTO venda (id_cliente, id_usuario, data_venda, valor_total, desconto, troco, observacoes) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, venda);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Venda adicionada com sucesso!" : "Não foi possível adicionar a venda";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editar(Venda venda) throws SQLException {
        String sql = "UPDATE venda SET id_cliente = ?, id_usuario = ?, data_venda = ?, valor_total = ?, desconto = ?, troco = ?, observacoes = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, venda);
            preparedStatement.setLong(8, venda.getId()); // Adiciona o ID da venda para a cláusula WHERE
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Venda editada com sucesso!" : "Não foi possível editar a venda";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, Venda venda) throws SQLException {
        preparedStatement.setLong(1, venda.getCliente().getId());
        preparedStatement.setLong(2, venda.getUsuario().getId());
        if (venda.getDataVenda() != null) {
            preparedStatement.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda())); // Convertendo LocalDateTime para Timestamp
        } else {
            preparedStatement.setTimestamp(3, null); // Passa null se a data for null
        }
        preparedStatement.setBigDecimal(4, venda.getTotalDaVenda());
        preparedStatement.setBigDecimal(5, venda.getDesconto());
        preparedStatement.setBigDecimal(6, venda.getTroco());
        preparedStatement.setString(7, venda.getObservacao());
    }

    public List<Venda> buscarVendas() {
        String sql = "SELECT * FROM venda";
        List<Venda> vendas = new ArrayList<>();

        try {
            ResultSet resultSet = conexao.obterConexao().prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                vendas.add(getVenda(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro: %s", e.getLocalizedMessage()));
        }

        return vendas;
    }

    private Venda getVenda(ResultSet resultSet) throws SQLException {
        Venda venda = new Venda();

        venda.setId(resultSet.getLong("id"));
        Cliente cliente = new ClienteDao().buscarClientePeloId(resultSet.getLong("id_cliente"));
        Usuario usuario = new UsuarioDao().buscarUsuarioPeloId(resultSet.getLong("id_usuario"));

        venda.setCliente(cliente);
        venda.setUsuario(usuario);
        venda.setDataVenda(resultSet.getTimestamp("data_venda").toLocalDateTime());
        venda.setTotalDaVenda(resultSet.getBigDecimal("valor_total"));
        venda.setDesconto(resultSet.getBigDecimal("desconto"));
        venda.setTroco(resultSet.getBigDecimal("troco"));
        venda.setObservacao(resultSet.getString("observacoes"));

        return venda;
    }

    public Venda buscarVendaPeloId(Long id) {
        String sql = "SELECT * FROM venda WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getVenda(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro ao buscar venda por ID: %s", e.getMessage()));
        }
        return null;
    }

    public String deletarPeloId(Long id) {
        // Verifica se a venda com o ID fornecido existe
        Venda venda = buscarVendaPeloId(id);
        if (venda == null) {
            return "Erro: Venda não encontrada no banco de dados.";
        }

        String sql = "DELETE FROM venda WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id); // Define o ID da venda a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado == 1 ? "Venda deletada com sucesso!" : "Erro ao deletar a venda.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public List<Venda> buscarVendasPorNomeCliente(String nomeCliente) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda WHERE id_cliente IN (SELECT id FROM cliente WHERE nome LIKE ?)";
        
        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeCliente + "%"); // Adiciona o % para buscar por nome
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                vendas.add(getVenda(rs)); // Adiciona a venda encontrada à lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vendas;
    }
}
