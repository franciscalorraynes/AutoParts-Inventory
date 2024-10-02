/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

/**
 *
 * @author Lorrayne
 */
public class Main {
 public static void main(String[] args) throws SQLException {
              // Criar uma instância de Pecas
         // Criar uma instância de Fornecedor (novo fornecedor que ainda não existe no banco de dados)
        Fornecedor fornecedor = new Fornecedor(null, "Claudio", "001.002.321-00", "98132-5432", "Avenida Exemplo, 30");

        // Criar uma instância de Pecas
        Pecas pecas = new Pecas(null, "Pneu", "Pneu de moto", new BigDecimal(2), new BigDecimal(250.90), LocalDateTime.now(), fornecedor);

        // Definir as SQLs de inserção
        String sqlFornecedor = "INSERT INTO fornecedor (nome, cpf, telefone, endereco) VALUES (?, ?, ?, ?)";
        String sqlPecas = "INSERT INTO pecas (nome, descricao, quantidade, preco, data_criacao, id_fornecedor) VALUES (?, ?, ?, ?, ?, ?)";



        // Estabelecer a conexão
        Conexao conexao = new ConexaoMySql();
        try (Connection conn = conexao.obterConexao()) {
            // Desabilitar o auto-commit para que possamos realizar as inserções em uma única transação
            conn.setAutoCommit(false);

            try {
                // Inserir o fornecedor
                try (PreparedStatement stmtFornecedor = conn.prepareStatement(sqlFornecedor, Statement.RETURN_GENERATED_KEYS)) {
                    stmtFornecedor.setString(1, fornecedor.getNome());
                    stmtFornecedor.setString(2, fornecedor.getCpf());
                    stmtFornecedor.setString(3, fornecedor.getTelefone());
                    stmtFornecedor.setString(4, fornecedor.getEndereco());

                    // Executar a inserção do fornecedor
                    int rowsInsertedFornecedor = stmtFornecedor.executeUpdate();

                    if (rowsInsertedFornecedor > 0) {
                        // Obter o ID gerado automaticamente para o fornecedor
                        ResultSet generatedKeys = stmtFornecedor.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            Long id = generatedKeys.getLong(1);
                            fornecedor.setId(id); // Definir o ID do fornecedor
                            System.out.println("Fornecedor inserido com sucesso! ID: " + id);
                        }
                    }
                }

                // Inserir a peça utilizando o ID do fornecedor recém-criado
                try (PreparedStatement stmtPecas = conn.prepareStatement(sqlPecas)) {
                    stmtPecas.setString(1, pecas.getNome());
                    stmtPecas.setString(2, pecas.getDescricao());
                    stmtPecas.setBigDecimal(3, pecas.getQuantidade());
                    stmtPecas.setBigDecimal(4, pecas.getPreco());
                    stmtPecas.setObject(5, pecas.getDataCriacao());  // Inserir LocalDateTime
                    stmtPecas.setLong(6, fornecedor.getId());  // Utilizar o ID do fornecedor

                    // Executar a inserção da peça
                    int rowsInsertedPecas = stmtPecas.executeUpdate();

                    if (rowsInsertedPecas > 0) {
                        System.out.println("Peça inserida com sucesso!");
                    }
                }

                // Se ambas inserções foram bem-sucedidas, confirmar a transação
                conn.commit();

            } catch (SQLException e) {
                // Em caso de erro, desfazer a transação
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
