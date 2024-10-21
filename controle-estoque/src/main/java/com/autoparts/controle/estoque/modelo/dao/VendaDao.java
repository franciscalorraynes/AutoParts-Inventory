
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
/*
    public String salvar(Venda venda) throws SQLException {
        return venda.getId() == null || venda.getId() == 0L ? adicionar(venda) : editar(venda);
    }
    */
    public Long salvar(Venda venda) throws SQLException {
    return venda.getId() == null || venda.getId() == 0L ? adicionar(venda) : editar(venda);
}


   private Long adicionar(Venda venda) throws SQLException {
    // Exemplo de lógica para adicionar a venda no banco de dados
    String sql = "INSERT INTO venda (id_cliente, id_usuario, data_venda, valor_total, desconto, troco, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setLong(1, venda.getCliente().getId()); // ID do cliente
        stmt.setLong(2, venda.getUsuario().getId()); // ID do usuário
        stmt.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda())); // Data da venda
        stmt.setBigDecimal(4, venda.getTotalDaVenda()); // Total da venda
        stmt.setBigDecimal(5, venda.getDesconto()); // Desconto aplicado
        stmt.setBigDecimal(6, venda.getTroco()); // Troco
        stmt.setString(7, venda.getObservacao()); // Observações sobre a venda

        stmt.executeUpdate();

        // Obtém o ID gerado
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);
                venda.setId(generatedId); // Define o ID na venda
                return generatedId; // Retorna o ID gerado
            } else {
                throw new SQLException("Erro ao obter ID gerado para a venda.");
            }
        }
    }
}


    
   
    
    /*
    private String adicionar(Venda venda) {
        String sql = "INSERT INTO venda (id_cliente, id_usuario, data_venda, valor_total, desconto, troco, observacoes) VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherValoresDePreparedStatement(preparedStatement, venda);
            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venda.setId(generatedKeys.getLong(1));
                    }
                }

                ItemVendaDao itemVendaDao = new ItemVendaDao();
                MovimentacaoEstoqueDao movimentacaoEstoqueDao = new MovimentacaoEstoqueDao();

                for (ItemVenda itemVenda : venda.getItensVenda()) {
                    itemVenda.setVenda(venda);
                    String resultadoItem = itemVendaDao.salvar(itemVenda);

                    if (!resultadoItem.startsWith("Item adicionado")) {
                        return "Erro ao adicionar item na venda: " + resultadoItem;
                    }

                    String respostaMovimentacao = movimentacaoEstoqueDao.subtrairPecasDoEstoque(itemVenda.getPecas().getId(), itemVenda.getQuantidade(), venda.getId());
                    if (respostaMovimentacao.startsWith("Erro")) {
                        return respostaMovimentacao;
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
*//*
    public String editar(Venda venda) throws SQLException {
        String sql = "UPDATE venda SET id_cliente = ?, id_usuario = ?, data_venda = ?, valor_total = ?, desconto = ?, troco = ?, observacoes = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql)) {
            preencherValoresDePreparedStatement(preparedStatement, venda);
            preparedStatement.setLong(8, venda.getId());
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Venda editada com sucesso!" : "Não foi possível editar a venda";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
*/
    public Long editar(Venda venda) throws SQLException {
    // Exemplo de lógica para editar a venda existente
    String sql = "UPDATE venda SET id_cliente=?, id_usuario=?, data_venda=?, valor_total=?, desconto=?, troco=?, observacoes=? WHERE id = ?";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        // Preencher os valores do PreparedStatement usando o método auxiliar
        preencherValoresDePreparedStatement(stmt, venda);
        
        // Define o ID da venda a ser editada
        stmt.setLong(8, venda.getId()); // O ID da venda a ser editada

        stmt.executeUpdate();
        return venda.getId(); // Retorna o ID da venda que foi editada
    }
}

   /*
   
public Long editar(Venda venda) throws SQLException {
    String sql = "UPDATE venda SET id_cliente=?, id_usuario=?, data_venda=?, valor_total=?, desconto=?, troco=?, observacoes=? WHERE id = ?";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        // Atualizar os dados básicos da venda
        stmt.setLong(1, venda.getCliente().getId()); // ID do cliente
        stmt.setLong(2, venda.getUsuario().getId()); // ID do usuário
        stmt.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda())); // Data da venda
        stmt.setBigDecimal(4, venda.getTotalDaVenda()); // Valor total
        stmt.setBigDecimal(5, venda.getDesconto()); // Desconto
        stmt.setBigDecimal(6, venda.getTroco()); // Troco
        stmt.setString(7, venda.getObservacao()); // Observações
        stmt.setLong(8, venda.getId()); // ID da venda a ser editada

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Erro ao editar venda: nenhuma linha foi afetada.");
        }

        // Agora atualizamos os itens da venda
        atualizarItensVenda(venda);

        return venda.getId(); // Retorna o ID da venda editada
    }
}

*/
private void atualizarItensVenda(Venda venda) throws SQLException {
    // Buscar os itens de venda atuais no banco de dados
    List<ItemVenda> itensExistentes = buscarItensPorVendaId(venda.getId().intValue());

    // Atualizar ou adicionar novos itens
    for (ItemVenda itemNovo : venda.getItensVenda()) {
        boolean itemExiste = false;

        for (ItemVenda itemExistente : itensExistentes) {
            if (itemNovo.getId() != null && itemNovo.getId().equals(itemExistente.getId())) {
                // Item já existe, então deve ser atualizado
                atualizarItemVenda(itemNovo);
                itemExiste = true;
                break;
            }
        }

        if (!itemExiste) {
            // Se o item não existe, devemos adicioná-lo
            salvarItemVenda(venda.getId(), itemNovo);
        }
    }

    // Remover itens que não estão mais associados à venda
    for (ItemVenda itemExistente : itensExistentes) {
        boolean itemFoiRemovido = true;

        for (ItemVenda itemNovo : venda.getItensVenda()) {
            if (itemExistente.getId().equals(itemNovo.getId())) {
                itemFoiRemovido = false;
                break;
            }
        }

        if (itemFoiRemovido) {
            deletarItemVenda(itemExistente.getId());
        }
    }
}
private void atualizarItemVenda(ItemVenda item) throws SQLException {
    String sql = "UPDATE item_venda SET id_peca=?, quantidade=?, preco_unitario=? WHERE id=?";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        stmt.setLong(1, item.getPecas().getId());
        stmt.setInt(2, item.getQuantidade());
        stmt.setBigDecimal(3, item.getPrecoUnitario());
        stmt.setLong(4, item.getId());
        stmt.executeUpdate();
    }
}

private void deletarItemVenda(Long id) throws SQLException {
    String sql = "DELETE FROM item_venda WHERE id=?";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        stmt.setLong(1, id);
        stmt.executeUpdate();
    }
}



    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, Venda venda) throws SQLException {
    preparedStatement.setLong(1, venda.getCliente().getId());
    preparedStatement.setLong(2, venda.getUsuario().getId());
    
    if (venda.getDataVenda() != null) {
        preparedStatement.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda()));
    } else {
        preparedStatement.setTimestamp(3, null);
    }
    
    preparedStatement.setBigDecimal(4, venda.getTotalDaVenda());
    preparedStatement.setBigDecimal(5, venda.getDesconto());
    preparedStatement.setBigDecimal(6, venda.getTroco());
    preparedStatement.setString(7, venda.getObservacao());
}

/*
    public List<Venda> buscarVendas() {
        String sql = "SELECT v.*, c.nome AS cliente_nome, c.telefone AS telefone_cliente, c.endereco AS endereco_cliente, u.nome AS usuario_nome "
                   + "FROM venda v JOIN cliente c ON v.id_cliente = c.id "
                   + "JOIN usuario u ON v.id_usuario = u.id";
        List<Venda> vendas = new ArrayList<>();

        try (ResultSet resultSet = conexao.obterConexao().prepareStatement(sql).executeQuery()) {
            while (resultSet.next()) {
                vendas.add(getVenda(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro: %s", e.getLocalizedMessage()));
        }

        return vendas;
    }
*/
    public List<Venda> buscarVendas() {
    String sql = "SELECT v.*, c.nome AS cliente_nome, c.telefone AS telefone_cliente, c.endereco AS endereco_cliente, "
               + "u.nome AS usuario_nome, iv.quantidade AS quantidade, iv.id AS item_id "
               + "FROM venda v "
               + "JOIN cliente c ON v.id_cliente = c.id "
               + "JOIN usuario u ON v.id_usuario = u.id "
               + "LEFT JOIN item_venda iv ON v.id = iv.id_venda"; // Aqui é necessário LEFT JOIN para itens
    List<Venda> vendas = new ArrayList<>();

    try (ResultSet resultSet = conexao.obterConexao().prepareStatement(sql).executeQuery()) {
        while (resultSet.next()) {
            // Crie a venda e adicione itens a ela aqui
            Venda venda = getVenda(resultSet);
            // Adicione a lógica para popular itensVenda aqui, se a quantidade não for nula
            int quantidade = resultSet.getInt("quantidade");
            if (quantidade > 0) {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setQuantidade(quantidade);
                // Defina as outras propriedades do itemVenda conforme necessário
                venda.getItensVenda().add(itemVenda); // Certifique-se que esta lista não é nula
            }
            vendas.add(venda);
        }
    } catch (SQLException e) {
        System.out.println(String.format("Erro: %s", e.getLocalizedMessage()));
    }

    return vendas;
}

    
    private Venda getVenda(ResultSet resultSet) throws SQLException {
    Venda venda = new Venda();
    
    // Mapeia os dados básicos da venda
    venda.setId(resultSet.getLong("id"));  // ID da venda
    venda.setDataVenda(resultSet.getTimestamp("data_venda").toLocalDateTime());  // Data da venda
    venda.setTotalDaVenda(resultSet.getBigDecimal("valor_total"));  // Total da venda
    venda.setDesconto(resultSet.getBigDecimal("desconto"));  // Desconto aplicado
    venda.setTroco(resultSet.getBigDecimal("troco"));  // Troco
    venda.setObservacao(resultSet.getString("observacoes"));  // Observações sobre a venda

    // Cria e mapeia o cliente
    Cliente cliente = new Cliente();
    cliente.setId(resultSet.getLong("id_cliente"));  // ID do cliente
    cliente.setNome(resultSet.getString("cliente_nome"));  // Nome do cliente
    cliente.setTelefone(resultSet.getString("telefone_cliente"));  // Telefone do cliente
    cliente.setEndereco(resultSet.getString("endereco_cliente"));  // Endereço do cliente
    venda.setCliente(cliente);  // Define o cliente na venda

    // Cria e mapeia o usuário
    Usuario usuario = new Usuario();
    usuario.setId(resultSet.getLong("id_usuario"));  // ID do usuário
    usuario.setNome(resultSet.getString("usuario_nome"));  // Nome do usuário
    venda.setUsuario(usuario);  // Define o usuário na venda

    return venda;  // Retorna a venda mapeada
}


    public Venda buscarVendaPeloId(Long id) {
        String sql = "SELECT v.*, c.nome AS cliente_nome, c.telefone AS telefone_cliente, c.endereco AS endereco_cliente, u.nome AS usuario_nome "
                   + "FROM venda v JOIN cliente c ON v.id_cliente = c.id "
                   + "JOIN usuario u ON v.id_usuario = u.id "
                   + "WHERE v.id = ?";
        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
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
    // Método para buscar itens de venda por ID
// Método para buscar itens de venda por ID da venda
private List<ItemVenda> buscarItensPorVendaId(int idVenda) {
    String sql = "SELECT iv.id, iv.id_venda, iv.id_peca, iv.quantidade, iv.preco_unitario, p.nome AS nome_peca " 
               + "FROM item_venda iv " 
               + "JOIN pecas p ON iv.id_peca = p.id " 
               + "WHERE iv.id_venda = ?"; // Filtrar pelo ID da venda

    List<ItemVenda> itens = new ArrayList<>(); // Lista para armazenar os itens de venda

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        stmt.setInt(1, idVenda); // Definir o ID da venda no prepared statement
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                // Criar um novo objeto ItemVenda para cada linha encontrada
                ItemVenda item = new ItemVenda();
                item.setId(resultSet.getLong("id")); // Definir ID do item de venda
                item.setQuantidade(resultSet.getInt("quantidade")); // Definir quantidade
                item.setPrecoUnitario(resultSet.getBigDecimal("preco_unitario")); // Definir preço unitário
                
                // Criar e popular a peça associada
                Pecas peca = new Pecas();
                peca.setId(resultSet.getLong("id_peca")); // Definir ID da peça
                peca.setNome(resultSet.getString("nome_peca")); // Definir nome da peça

                item.setPecas(peca); // Associar a peça ao item de venda
                
                // Adicionar o item de venda à lista de itens
                itens.add(item);
            }
        }
    } catch (SQLException e) {
        System.out.println(String.format("Erro ao buscar itens da venda: %s", e.getLocalizedMessage()));
    }

    return itens; // Retornar a lista de itens de venda
}


    public String deletarPeloId(Long id) {
        String sql = "DELETE FROM venda WHERE id = ?";
        Venda venda = buscarVendaPeloId(id);

        if (venda == null) {
            return "Venda não encontrada.";
        }

        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
            stmt.setLong(1, id);
            int resultado = stmt.executeUpdate();
            return resultado == 1 ? "Venda deletada com sucesso!" : "Erro ao deletar a venda.";
        } catch (SQLException e) {
            return String.format("Erro ao deletar a venda: %s", e.getMessage());
        }
    }

    public List<Venda> buscarVendasPorNomeCliente(String nomeCliente) {
        String sql = "SELECT v.*, c.nome AS cliente_nome, c.telefone AS telefone_cliente, c.endereco AS endereco_cliente, u.nome AS usuario_nome "
                   + "FROM venda v JOIN cliente c ON v.id_cliente = c.id "
                   + "JOIN usuario u ON v.id_usuario = u.id "
                   + "WHERE c.nome LIKE ?";
        List<Venda> vendas = new ArrayList<>();

        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeCliente + "%");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                vendas.add(getVenda(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro ao buscar vendas por nome de cliente: %s", e.getMessage()));
        }

        return vendas;
    }
    
    public void salvarItemVenda(Long vendaId, ItemVenda item) throws SQLException {
    String sql = "INSERT INTO item_venda (id_venda, id_peca, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        stmt.setLong(1, vendaId);
        stmt.setLong(2, item.getPecas().getId());
        stmt.setInt(3, item.getQuantidade());
        stmt.setBigDecimal(4, item.getPrecoUnitario());
        stmt.executeUpdate();   
    }
}




}
