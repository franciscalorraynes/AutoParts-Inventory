/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Lorrayne
 */
public class ItemVendaDao {

    private final Conexao conexao;

    public ItemVendaDao() {
        this.conexao = new ConexaoMySql();
    }

    public String salvar(ItemVenda itemVenda) {
        return itemVenda.getId() == null || itemVenda.getId() == 0L ? adicionar(itemVenda) : editar(itemVenda);
    }

    private String adicionar(ItemVenda itemVenda) {
        String sql = "insert into itemvenda(id_venda, id_peca, quantidade, preco_unitario) values (?,?,?,?,)";

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Item adicionado com sucesso!" : "Nao foi possivel adicionar o item";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private String editar(ItemVenda itemVenda) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
