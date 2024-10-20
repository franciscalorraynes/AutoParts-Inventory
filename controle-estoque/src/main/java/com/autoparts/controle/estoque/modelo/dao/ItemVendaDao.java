package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDao {

    private final Conexao conexao;

    public ItemVendaDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(ItemVenda itemVenda) {
        return (itemVenda.getId() == null || itemVenda.getId() == 0L) ? adicionar(itemVenda) : editar(itemVenda);
    }

   private String adicionar(ItemVenda itemVenda) {
    String sql = "INSERT INTO item_venda(id_venda, id_peca, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
    
    try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql)) {
        preencherValoresDePreparedStatement(preparedStatement, itemVenda);
        int resultado = preparedStatement.executeUpdate();
        
        if (resultado == 1) {
            // Atualizar o estoque após adicionar o item de venda
            MovimentacaoEstoqueDao movimentacaoEstoqueDao = new MovimentacaoEstoqueDao();
            String respostaMovimentacao = movimentacaoEstoqueDao.subtrairPecasDoEstoque(itemVenda.getPecas().getId(), itemVenda.getQuantidade(), itemVenda.getVenda().getId());
            
            if (respostaMovimentacao.startsWith("Erro")) {
                return respostaMovimentacao; // Retorna o erro se houver problema na movimentação
            }
            
            return "Item adicionado e estoque atualizado com sucesso!";
        }
        
        return "Não foi possível adicionar o item";
    } catch (SQLException e) {
        return String.format("Erro: %s", e.getMessage());
    }
}



    private String editar(ItemVenda itemVenda) {
        String sql = "update item_venda set id_venda=?, id_peca=?, quantidade=?, preco_unitario=? where id = ?";

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, itemVenda); 
            preparedStatement.setLong(5, itemVenda.getId()); // ID do item a ser editado

            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Item editado com sucesso!" : "Não foi possível editar o Item";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, ItemVenda itemVenda) throws SQLException {
    preparedStatement.setLong(1, itemVenda.getVenda().getId()); // ID da venda
    preparedStatement.setLong(2, itemVenda.getPecas().getId()); // ID da peça
    preparedStatement.setInt(3, itemVenda.getQuantidade());
    preparedStatement.setBigDecimal(4, itemVenda.getPrecoUnitario());
}


    public List<ItemVenda> buscarItemVendas() {
        String sql = "select * from item_venda";
        List<ItemVenda> itemVendas = new ArrayList<>();

        try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql); ResultSet result = preparedStatement.executeQuery()) {

            while (result.next()) {
                itemVendas.add(getItemVenda(result));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro: %s", e.getMessage()));
        }

        return itemVendas;
    }
    
    public List<ItemVenda> buscarItemVendasPorPecaOuVenda(ItemVenda itemVenda) {
    String sql = "select * from item_venda where id_venda = ? or id_peca = ?";
    List<ItemVenda> itemVendas = new ArrayList<>();

    try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql)) {
        preparedStatement.setLong(1, itemVenda.getVenda().getId());
        preparedStatement.setLong(2, itemVenda.getPecas().getId());
        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            itemVendas.add(getItemVenda(result));
        }
    } catch (SQLException e) {
        System.out.println(String.format("Erro: %s", e.getMessage()));
    }

    return itemVendas;
}


    private ItemVenda getItemVenda(ResultSet result) throws SQLException {
    ItemVenda itemVenda = new ItemVenda();
    itemVenda.setId(result.getLong("id"));

    // Aqui, você busca a peça e a venda relacionadas ao item de venda
    Pecas pecas = new PecasDao().buscarPecasPeloId(result.getLong("id_peca"));
    Venda venda = new VendaDao().buscarVendaPeloId(result.getLong("id_venda"));
    
    itemVenda.setPecas(pecas);
    itemVenda.setVenda(venda);

    itemVenda.setQuantidade(result.getInt("quantidade"));
    itemVenda.setPrecoUnitario(result.getBigDecimal("preco_unitario"));

    return itemVenda;
}


    public ItemVenda buscarItemVendaPeloId(Long id) {
        String sql = "select *from item_venda where id = ?";

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getItemVenda(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro: %s", e.getMessage()));
        }

        return null;
    }

    public List<ItemVenda> buscarItensPorVendaId(Long idVenda) {
    String sql = "select * from item_venda where id_venda = ?"; // Altere id para id_venda
    List<ItemVenda> itensVenda = new ArrayList<>();

    try {
        PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
        preparedStatement.setLong(1, idVenda);
        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            itensVenda.add(getItemVenda(result)); // Reutiliza o método que já transforma o ResultSet em ItemVenda
        }
    } catch (SQLException e) {
        System.out.println(String.format("Erro: %s", e.getMessage()));
    }

    return itensVenda;
}
}