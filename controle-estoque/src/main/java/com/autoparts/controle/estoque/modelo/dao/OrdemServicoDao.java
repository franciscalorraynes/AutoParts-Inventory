package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.OrdemServico;
import com.autoparts.controle.estoque.modelo.exception.NegocioException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Samira
 */
public class OrdemServicoDao {

    private final Conexao conexao;

    public OrdemServicoDao() {
        this.conexao = (Conexao) new ConexaoMySql();
    }

    public String salvar(OrdemServico Ordemservico) {
        return Ordemservico.getId() == null ? adicionar(Ordemservico) : editar(Ordemservico);
    }

    public String adicionar(OrdemServico Ordemservico) {
        String sql = "INSERT INTO ordemServico(data_os, equipamento, defeito, servicoPrestado, funcionarioResponsavel, valor, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        OrdemServico usuarioTemp = buscarOrdemServicoPeloNome(Ordemservico.getServicoPrestado());
        if (usuarioTemp != null) {
            throw new NegocioException(
                    String.format("Erro: username %s já existe no banco de dados", Ordemservico.getServicoPrestado()));
        }

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preencherValoresDePreparedStatement(preparedStatement, Ordemservico);
            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Ordemservico.setId(generatedKeys.getLong(1));
                }
                return "Ordem de servico adicionada com sucesso!";
            } else {
                return "Não foi possível adicionar a ordem de serviço";
            }
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public String editar(OrdemServico ordemServico) {
        String sql = "UPDATE ordemServico SET data_os = ?, equipamento = ?, defeito = ?, servicoPrestado = ?, funcionarioResponsavel = ?, valor = ?, idCliente = ? WHERE idOs = ?";

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, ordemServico);
            preparedStatement.setLong(8, ordemServico.getId()); // Define o ID da ordem de serviço a ser editada
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Ordem de serviço editada com sucesso!" : "Não foi possível editar a ordem de serviço.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, OrdemServico Ordemservico) throws SQLException {

        preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(Ordemservico.getDataOs())); // Data e Hora
        preparedStatement.setString(2, Ordemservico.getEquipamento()); // Equipamento
        preparedStatement.setString(3, Ordemservico.getDefeito()); // Defeito
        preparedStatement.setString(4, Ordemservico.getServicoPrestado()); // Serviço Prestado
        preparedStatement.setString(5, Ordemservico.getFuncionarioResponsavel()); // Funcionário Responsável
        preparedStatement.setBigDecimal(6, Ordemservico.getValor()); // Valor
        preparedStatement.setLong(7, Ordemservico.getCliente().getId()); // ID do cliente
    }

    public List<OrdemServico> buscarOrdemServico() {
        String sql = "SELECT os.idOs, os.data_os, os.equipamento, os.defeito, os.servicoPrestado, os.funcionarioResponsavel, os.valor,\n"
                + "       c.id AS cliente_id, c.nome AS cliente_nome\n"
                + "FROM ordemServico os\n"
                + "JOIN cliente c ON os.idCliente = c.id;";
        List<OrdemServico> ordemServicos = new ArrayList<>();

        try (Connection conn = conexao.obterConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                OrdemServico ordemServico = getOrdemServico(result);
                ordemServicos.add(ordemServico);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ordem de servico: " + e.getMessage());
        }

        return ordemServicos;
    }

    private OrdemServico getOrdemServico(ResultSet result) throws SQLException {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setId(result.getLong("idOs"));
        ordemServico.setDataOs(result.getTimestamp("data_os").toLocalDateTime());
        ordemServico.setEquipamento(result.getString("equipamento"));
        ordemServico.setDefeito(result.getString("defeito"));
        ordemServico.setServicoPrestado(result.getString("servicoPrestado"));
        ordemServico.setFuncionarioResponsavel(result.getString("funcionarioResponsavel"));
        ordemServico.setValor(result.getBigDecimal("valor"));

        // Agora com os aliases
        Cliente cliente = new Cliente();
        cliente.setId(result.getLong("cliente_id"));  // ID do cliente, que é a chave estrangeira
        cliente.setNome(result.getString("cliente_nome"));  // Nome do cliente recuperado da tabela cliente
        ordemServico.setCliente(cliente);  // Associando o cliente à ordem de serviço

        return ordemServico;
    }

    public OrdemServico buscarOrdemServicoPeloId(Long id) {
        String sql = "SELECT os.*, c.nome AS nomeCliente "
                + "FROM OrdemServico os "
                + "JOIN Cliente c ON os.idCliente = c.id "
                + "WHERE os.idOs = ?";
        OrdemServico ordemServico = null;

        try (Connection conn = conexao.obterConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ordemServico = new OrdemServico();
                // Ajuste para usar o nome correto do campo idOs
                ordemServico.setId(rs.getLong("idOs")); // Aqui ajusta para "idOs"
                ordemServico.setEquipamento(rs.getString("equipamento"));
                ordemServico.setDefeito(rs.getString("defeito"));
                ordemServico.setServicoPrestado(rs.getString("servicoPrestado"));
                ordemServico.setFuncionarioResponsavel(rs.getString("funcionarioResponsavel"));
                ordemServico.setValor(rs.getBigDecimal("valor"));
                ordemServico.setDataOs(rs.getTimestamp("data_os").toLocalDateTime());

                // Criar o objeto Cliente associado
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("idCliente")); // id do cliente na tabela OrdemServico
                cliente.setNome(rs.getString("nomeCliente")); // Usar o alias criado na consulta SQL
                ordemServico.setCliente(cliente);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }

        return ordemServico;
    }

    public OrdemServico buscarOrdemServicoPeloNome(String servicoPrestado) {
        String sql = "SELECT * FROM ordemServico WHERE servicoPrestado = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, servicoPrestado);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getOrdemServico(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
    }

    public OrdemServico buscarOrdemServicoPorNomeCliente(String nomeCliente) {
        String sql = "SELECT os.*, c.nome AS nomeCliente "
                + "FROM OrdemServico os "
                + "JOIN Cliente c ON os.idCliente = c.id "
                + "WHERE c.nome LIKE ?";
        OrdemServico ordemServico = null;

        try (Connection conn = conexao.obterConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeCliente + "%"); // Permitir pesquisa parcial
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ordemServico = new OrdemServico();
                // Ajuste para usar o nome correto do campo idOs
                ordemServico.setId(rs.getLong("idOs")); // Aqui ajusta para "idOs"
                ordemServico.setEquipamento(rs.getString("equipamento"));
                ordemServico.setDefeito(rs.getString("defeito"));
                ordemServico.setServicoPrestado(rs.getString("servicoPrestado"));
                ordemServico.setFuncionarioResponsavel(rs.getString("funcionarioResponsavel"));
                ordemServico.setValor(rs.getBigDecimal("valor"));
                ordemServico.setDataOs(rs.getTimestamp("data_os").toLocalDateTime());

                // Criar o objeto Cliente associado
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("idCliente")); // id do cliente na tabela OrdemServico
                cliente.setNome(rs.getString("nome")); // Usar o alias criado na consulta SQL
                ordemServico.setCliente(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordemServico;
    }

    public String deletarPeloId(Long id) {
        String sql = "delete from ordemServico WHERE idOs = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id); // Define o ID do ordem de servico a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado == 1 ? "Ordem de servico deletado com sucesso!" : "Erro ao deletar o ordem de servico.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    public String deletarPorNome(String servicoPrestado) {
        String sql = "delete from ordemServico WHERE servicoPrestado = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, servicoPrestado); // Define o nome do Servico Prestado a ser deletado
            int resultado = preparedStatement.executeUpdate(); // Executa o comando de deleção
            return resultado > 0 ? "Ordem de servico(s) deletado(s) com sucesso!"
                    : "Erro ao deletar o ordem de servico.";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
}
