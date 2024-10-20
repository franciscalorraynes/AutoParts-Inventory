
package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.conexao.Conexao;
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

    private String adicionar(OrdemServico Ordemservico) {
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

    private String editar(OrdemServico Ordemservico) {
        String sql = "INSERT INTO ordemServico (data_os, equipamento, defeito, servicoPrestado, funcionarioResponsavel, valor, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preencherValoresDePreparedStatement(preparedStatement, Ordemservico);
            int resultado = preparedStatement.executeUpdate();
            return resultado == 1 ? "Ordem de servico editado com sucesso!"
                    : "Nao foi possivel editar o ordem de servico";
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }

    private void preencherValoresDePreparedStatement(PreparedStatement preparedStatement, OrdemServico Ordemservico)
            throws SQLException {

        // Debug: Verifique os valores que estão sendo passados
        System.out.println("Equipamento: " + Ordemservico.getEquipamento());
        System.out.println("Defeito: " + Ordemservico.getDefeito());
        System.out.println("Serviço Prestado: " + Ordemservico.getServicoPrestado());
        System.out.println("Funcionário Responsável: " + Ordemservico.getFuncionarioResponsavel());
        System.out.println("Valor: " + Ordemservico.getValor());
        System.out.println("Cliente ID: " + Ordemservico.getCliente().getId());
        System.out.println("Data OS: " + Ordemservico.getDataOs()); // Adicione essa linha

        // Setando os valores no PreparedStatement
        preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(Ordemservico.getDataOs())); // Data e Hora
        preparedStatement.setString(2, Ordemservico.getEquipamento()); // Equipamento
        preparedStatement.setString(3, Ordemservico.getDefeito()); // Defeito
        preparedStatement.setString(4, Ordemservico.getServicoPrestado()); // Serviço Prestado
        preparedStatement.setString(5, Ordemservico.getFuncionarioResponsavel()); // Funcionário Responsável
        preparedStatement.setBigDecimal(6, Ordemservico.getValor()); // Valor
        preparedStatement.setLong(7, Ordemservico.getCliente().getId()); // ID do cliente
    }

    public List<OrdemServico> buscarOrdemServico() {
        String sql = "SELECT * FROM ordemServico";
        List<OrdemServico> Ordemservico = new ArrayList<>();

        // Utilizando try-with-resources para fechar automaticamente os recursos
        try (Connection conn = conexao.obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet result = stmt.executeQuery()) {

            // Percorrendo os resultados e populando a lista de clientes
            while (result.next()) {
                Ordemservico.add(getOrdemServico(result));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ordem de servico: " + e.getMessage());
        }

        return Ordemservico;
    }

    private OrdemServico getOrdemServico(ResultSet result) throws SQLException {
        OrdemServico Ordemservico = new OrdemServico();
        Ordemservico.setId(result.getLong("id"));
        Ordemservico.setDataOs(result.getTimestamp("dataOs").toLocalDateTime());
        Ordemservico.setEquipamento(result.getString("equipamento"));
        Ordemservico.setDefeito(result.getString("defeito"));
        Ordemservico.setServicoPrestado(result.getString("servicoPrestado"));
        Ordemservico.setFuncionarioResponsavel(result.getString("funcionarioResponsavel"));
        Ordemservico.setValor(result.getBigDecimal("valor")); // Corrigido aqui
        return Ordemservico;
    }

    public OrdemServico buscarOrdemServicoPeloId(Long id) {
        String sql = "select * from ordemServico where idOs = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return getOrdemServico(result);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Erro:  %s", e.getMessage()));
        }
        return null;
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
