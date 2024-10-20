/*

package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
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
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preencherValoresDePreparedStatement(preparedStatement, venda);
            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1) {
                // Recuperar o ID da venda recém-criada
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    venda.setId(generatedKeys.getLong(1));
                }

                // Adicionar itens de venda e subtrair do estoque
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
    String sql = "SELECT * FROM venda";  // Certifique-se de fechar a string SQL
    List<Venda> vendas = new ArrayList<>();

    try {
        ResultSet resultSet = conexao.obterConexao().prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            vendas.add(getVenda(resultSet)); // Chame o método para mapear o ResultSet para um objeto Venda
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
    cliente.setId(resultSet.getLong("id")); // Isso está errado, deve ser o ID do cliente
    cliente.setNome(resultSet.getString("nome")); // Erro: coluna 'nome' não encontrada
    cliente.setTelefone(resultSet.getString("telefone")); // Erro: coluna 'telefone' não encontrada
    cliente.setEndereco(resultSet.getString("endereco")); // Erro: coluna 'endereco' não encontrada
    venda.setCliente(cliente);

    // Popula o usuário
    Usuario usuario = new Usuario();
    usuario.setId(resultSet.getLong("id")); // Isso também está errado, deve ser o ID do usuário
    usuario.setNome(resultSet.getString("nome")); // Erro: coluna 'nome' não encontrada
    venda.setUsuario(usuario);

    return venda;
}


    public Venda buscarVendaPeloId(Long id) {
        String sql = "SELECT v.id, v.data_venda, v.valor_total, v.desconto, v.troco, v.observacoes, " +
                 "c.id AS id, c.nome AS nome, c.telefone AS telefone, c.endereco AS endereco, " +
                 "u.id AS id, u.nome AS nome " +
                 "FROM venda v " +
                 "JOIN cliente c ON v.id = c.id " +
                 "JOIN usuario u ON v.id = u.id " +
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

    public List<Venda> buscarVendasPorNomeCliente(String nomeCliente) throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.id, v.data_venda, v.valor_total, v.desconto, v.troco, v.observacoes, " +
                 "c.id AS id, c.nome AS nome, c.telefone AS telefone, c.endereco AS endereco, " +
                 "u.id AS id, u.nome AS nome " +
                 "FROM venda v " +
                 "JOIN cliente c ON v.id = c.id " +
                 "JOIN usuario u ON v.id = u.id " +
                 "WHERE c.nome LIKE ?";

        try (PreparedStatement resultado = conexao.obterConexao().prepareStatement(sql)) {
            resultado.setString(1, "%" + nomeCliente + "%"); // Adiciona o % para buscar por nome
            ResultSet rs = resultado.executeQuery();

            while (rs.next()) {
                vendas.add(getVenda(rs)); // Adiciona a venda encontrada à lista
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro ao buscar vendas por nome do cliente: %s", e.getMessage()));
        }
        return vendas;
    }
}
*//*
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
*//*
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


    /*private String editar(Venda venda) throws SQLException {
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
  *//*
     public String editar(Venda venda) throws SQLException {
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
    preparedStatement.setLong(1, venda.getCliente().getId()); // ID do cliente
    preparedStatement.setLong(2, venda.getUsuario().getId()); // ID do usuário

    if (venda.getDataVenda() != null) {
        preparedStatement.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda())); // Conversão para Timestamp
    } else {
        preparedStatement.setTimestamp(3, null); // Valor nulo se não houver data
    }

    preparedStatement.setBigDecimal(4, venda.getTotalDaVenda()); // Valor total
    preparedStatement.setBigDecimal(5, venda.getDesconto()); // Desconto
    preparedStatement.setBigDecimal(6, venda.getTroco()); // Troco
    preparedStatement.setString(7, venda.getObservacao()); // Observações
}
/*
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
    */
    /*
    public List<Venda> buscarVendas() {
    String sql = "SELECT * FROM venda";
    List<Venda> vendas = new ArrayList<>();

    try {
        ResultSet resultSet = conexao.obterConexao().prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            Venda venda = getVenda(resultSet);
            System.out.println("Venda encontrada: ID = " + venda.getId()); // Log para verificar se vendas estão sendo encontradas
            vendas.add(venda);
        }
    } catch (SQLException e) {
        System.out.println(String.format("Erro: %s", e.getLocalizedMessage()));
    }

    System.out.println("Total de vendas retornadas: " + vendas.size()); // Verifica o número total de vendas
    return vendas;
}
*//*
 public List<Venda> buscarVendas() {
    String sql = "SELECT v.*, c.nome AS cliente_nome, c.telefone AS telefone_cliente, "
                 + "c.endereco AS endereco_cliente, u.nome AS usuario_nome "
                 + "FROM venda v "
                 + "JOIN cliente c ON v.id_cliente = c.id "  // Use id_cliente
                 + "JOIN usuario u ON v.id_usuario = u.id"; // Use id_usuario
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
    venda.setId(resultSet.getLong("id")); // ID da venda
    venda.setDataVenda(resultSet.getTimestamp("data_venda").toLocalDateTime());
    venda.setTotalDaVenda(resultSet.getBigDecimal("valor_total"));
    venda.setDesconto(resultSet.getBigDecimal("desconto"));
    venda.setTroco(resultSet.getBigDecimal("troco"));
    venda.setObservacao(resultSet.getString("observacoes"));

    // Popula o cliente
    Cliente cliente = new Cliente();
    cliente.setId(resultSet.getLong("id_cliente")); // ID do cliente (use id_cliente)
    cliente.setNome(resultSet.getString("cliente_nome"));
    cliente.setTelefone(resultSet.getString("telefone_cliente"));
    cliente.setEndereco(resultSet.getString("endereco_cliente"));
    venda.setCliente(cliente);

    // Popula o usuário
    Usuario usuario = new Usuario();
    usuario.setId(resultSet.getLong("id_usuario")); // ID do usuário (use id_usuario)
    usuario.setNome(resultSet.getString("usuario_nome"));
    venda.setUsuario(usuario);

    return venda;
}

 public Venda buscarVendaPeloId(Long id) {
        String sql = "SELECT v.id, v.data_venda, v.valor_total, v.desconto, v.troco, v.observacoes, " +
                     "c.id AS id_cliente, c.nome AS nome_cliente, c.telefone AS telefone_cliente, c.endereco AS endereco_cliente, " +
                     "u.id AS id_usuario, u.nome AS nome_usuario " +
                     "FROM venda v " +
                     "JOIN cliente c ON v.id_cliente = c.id " +
                     "JOIN usuario u ON v.id_usuario = u.id " +
                     "WHERE v.id = ?";

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

    public String deletarPeloId(Long id) {
        String sql = "DELETE FROM venda WHERE id = ?";
        try {
            Venda venda = buscarVendaPeloId(id);
            if (venda == null) {
                return "Erro: Venda não encontrada no banco de dados.";
            }

            try (PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                int resultado = preparedStatement.executeUpdate();
                return resultado == 1 ? "Venda deletada com sucesso!" : "Erro ao deletar a venda.";
            }
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public List<Venda> buscarVendasPorNomeCliente(String nomeCliente) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda WHERE id_cliente IN (SELECT id FROM cliente WHERE nome LIKE ?)";
        
        try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeCliente + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                vendas.add(getVenda(rs));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro ao buscar vendas por nome do cliente: %s", e.getMessage()));
        }
        
        return vendas;
    }  

}
*/

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
   /*
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
*/
   public Long editar(Venda venda) throws SQLException {
    String sql = "UPDATE venda SET id_cliente=?, id_usuario=?, data_venda=?, valor_total=?, desconto=?, troco=?, observacoes=? WHERE id = ?";

    try (PreparedStatement stmt = conexao.obterConexao().prepareStatement(sql)) {
        // Preencher os valores do PreparedStatement usando os dados da venda
        
        // Define o ID do cliente
        stmt.setLong(1, venda.getCliente().getId()); 

        // Define o ID do usuário responsável pela venda
        stmt.setLong(2, venda.getUsuario().getId()); 

        // Define a data da venda
        stmt.setTimestamp(3, java.sql.Timestamp.valueOf(venda.getDataVenda())); 


        // Define o valor total da venda
        stmt.setBigDecimal(4, venda.getTotalDaVenda()); 

        // Define o desconto aplicado, se houver
        stmt.setBigDecimal(5, venda.getDesconto()); 

        // Define o troco, se aplicável
        stmt.setBigDecimal(6, venda.getTroco()); 

        // Define observações sobre a venda, se houver
        stmt.setString(7, venda.getObservacao()); 

        // Define o ID da venda a ser editada (no WHERE)
        stmt.setLong(8, venda.getId()); 

        // Executa a atualização
        stmt.executeUpdate();

        // Retorna o ID da venda que foi editada
        return venda.getId();
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
