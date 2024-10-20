package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.RelatorioVendas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioVendasDao {
    private final Conexao conexao;

    public RelatorioVendasDao() {
        conexao = new ConexaoMySql(); // Inicializa a conexão
    }

    public List<RelatorioVendas> buscarTodas() {
        String sql = "SELECT * FROM relatorio_vendas";
        List<RelatorioVendas> relatorio = new ArrayList<>();
        
        try (Connection conn = conexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                RelatorioVendas venda = new RelatorioVendas();
                venda.setIdVenda(resultSet.getLong("idVenda"));
                venda.setCliente(resultSet.getString("cliente"));
                venda.setPeca(resultSet.getString("peca"));
                venda.setQuantidade(resultSet.getInt("quantidade"));
                venda.setValorTotal(resultSet.getDouble("valor_total"));
                venda.setDataVenda(resultSet.getTimestamp("data_venda").toLocalDateTime());

                relatorio.add(venda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar relatório de vendas: " + e.getMessage());
        }
        
        return relatorio;
    }
}
