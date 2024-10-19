package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
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

    /*
    public String adicionar(Venda venda) {
    String sql = "INSERT INTO vendas (id_cliente, id_usuario, data_venda, valor_total, desconto, troco, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = conexao.obterConexao(); PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        preencherValoresDePreparedStatement(preparedStatement, venda);
        int resultado = preparedStatement.executeUpdate();
        
        // Recupera o ID gerado da venda
        if (resultado == 1) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venda.setId(generatedKeys.getLong(1)); // Define o ID da venda recém-adicionada
                }
            }

            // Adiciona os itens de venda e subtrai as peças do estoque
            ItemVendaDao itemVendaDao = new ItemVendaDao();
            MovimentacaoEstoqueDao movimentacaoEstoqueDao = new MovimentacaoEstoqueDao();

            for (ItemVenda itemVenda : venda.getItensVenda()) {
                itemVenda.setVenda(venda); // Associa a venda ao item
                String resultadoItem = itemVendaDao.salvar(itemVenda);
                
                if (!resultadoItem.startsWith("Item adicionado")) {
                    return "Erro ao adicionar item na venda: " + resultadoItem;
                }

                // Atualiza o estoque após adicionar o item
                String respostaMovimentacao = movimentacaoEstoqueDao.subtrairPecasDoEstoque(itemVenda.getPecas().getId(), itemVenda.getQuantidade(), venda.getId());
                if (respostaMovimentacao.startsWith("Erro")) {
                    return respostaMovimentacao; // Retorna o erro se houver problema na movimentação
                }
            }

            return "Venda registrada com sucesso!";
        } else {
            return "Erro ao registrar a venda.";
        }
    } catch (SQLException e) {
        return String.format("Erro: %s", e.getMessage());
    }
}
*/
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
/*
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
*/
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
    
    // Popula a venda
    venda.setId(resultSet.getLong("id"));
    venda.setDataVenda(resultSet.getTimestamp("data_venda").toLocalDateTime());
    venda.setTotalDaVenda(resultSet.getBigDecimal("valor_total"));
    venda.setDesconto(resultSet.getBigDecimal("desconto"));
    venda.setTroco(resultSet.getBigDecimal("troco"));
    venda.setObservacao(resultSet.getString("observacoes"));

    // Popula o cliente
    Cliente cliente = new Cliente();
    cliente.setId(resultSet.getLong("cliente_id"));
    cliente.setNome(resultSet.getString("nome"));
    cliente.setTelefone(resultSet.getString("telefone"));
    cliente.setEndereco(resultSet.getString("endereco"));
    venda.setCliente(cliente);

    // Popula o usuário
    Usuario usuario = new Usuario();
    usuario.setId(resultSet.getLong("usuario_id"));
    usuario.setNome(resultSet.getString("usuario_nome"));
    venda.setUsuario(usuario);

    return venda;
    }

    public Venda buscarVendaPeloId(Long id) {
         String sql = "SELECT v.id, v.data_venda, v.valor_total, v.desconto, v.troco, v.observacoes, " +
                 "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf AS cliente_cpf, " +
                 "u.id AS usuario_id, u.nome AS usuario_nome " +
                 "FROM venda v " +
                 "JOIN cliente c ON v.id_cliente = c.id " +
                 "JOIN usuario u ON v.id_usuario = u.id " +
                 "WHERE v.id = ?";

    try {
        PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql);
        stmt.setLong(1, id);
        ResultSet result = stmt.executeQuery();

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
            return "Erro: Venda não encontrado no banco de dados.";
        }

        String sql = "delete from venda WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id); // Define o ID da venda a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado == 1 ? "Venda deletado com sucesso!" : "Erro ao deletar o venda.";
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
