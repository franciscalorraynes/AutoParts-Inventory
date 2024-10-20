
package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dao.OrdemServicoDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.OrdemServico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GerenciarOrdemServicoForm extends JPanel {

    private OrdemServico ordemServicoEditando = null;
    private DefaultTableModel tabelaModelo;
    private JButton botaoAdicionar;
    private JButton botaoSalvarAlteracoes;
    private boolean editandoOrdemServico = false;

    public GerenciarOrdemServicoForm() {
        setLayout(null);

        JLabel label = new JLabel("Gerenciar Ordens de Serviço");
        botaoAdicionar = new JButton("Adicionar Ordem");
        botaoSalvarAlteracoes = new JButton("Salvar Alterações");
        JButton botaoRemover = new JButton("Remover Ordem");
        JButton botaoEditar = new JButton("Editar Ordem");
        JButton botaoListar = new JButton("Listar Ordens");

        JTextField campoEquipamento = new JTextField(20);
        JTextField campoDefeito = new JTextField(20);
        JTextField campoServicoPrestado = new JTextField(20);
        JTextField campoFuncionarioResponsavel = new JTextField(20);
        JTextField campoValor = new JTextField(20);
        JTextField campoData = new JTextField(20);

        // Adicionando JComboBox para selecionar o cliente
        JComboBox<Cliente> comboClientes = new JComboBox<>();
        carregarClientes(comboClientes); // Método para carregar os clientes

        JLabel labelEquipamento = new JLabel("Equipamento: ");
        JLabel labelDefeito = new JLabel("Defeito: ");
        JLabel labelServicoPrestado = new JLabel("Serviço Prestado: ");
        JLabel labelFuncionarioResponsavel = new JLabel("Funcionário Responsável: ");
        JLabel labelValor = new JLabel("Valor: ");
        JLabel labelData = new JLabel("Data da OS: ");
        JLabel labelCliente = new JLabel("Cliente: ");

        label.setBounds(30, 30, 200, 25);
        campoEquipamento.setBounds(180, 60, 150, 25);
        labelEquipamento.setBounds(30, 60, 150, 25);
        campoDefeito.setBounds(180, 100, 150, 25);
        labelDefeito.setBounds(30, 100, 150, 25);
        campoServicoPrestado.setBounds(180, 140, 150, 25);
        labelServicoPrestado.setBounds(30, 140, 150, 25);
        campoFuncionarioResponsavel.setBounds(180, 180, 150, 25);
        labelFuncionarioResponsavel.setBounds(30, 180, 150, 25);
        campoValor.setBounds(180, 220, 150, 25);
        labelValor.setBounds(30, 220, 150, 25);
        campoData.setBounds(180, 260, 150, 25);
        labelData.setBounds(30, 260, 150, 25);
        comboClientes.setBounds(180, 300, 150, 25);
        labelCliente.setBounds(30, 300, 150, 25);

        botaoAdicionar.setBounds(30, 340, 150, 25);
        botaoSalvarAlteracoes.setBounds(30, 380, 150, 25);
        botaoRemover.setBounds(30, 420, 150, 25);
        botaoEditar.setBounds(30, 460, 150, 25);
        botaoListar.setBounds(30, 500, 150, 25);

        String[] colunas = {"ID", "Equipamento", "Defeito", "Serviço Prestado", "Funcionário", "Valor", "Data", "Cliente"};
        tabelaModelo = new DefaultTableModel(null, colunas);
        JTable tabelaOrdens = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaOrdens);
        add(scrollPane);
        scrollPane.setBounds(350, 60, 600, 250);

        add(label);
        add(campoEquipamento);
        add(labelEquipamento);
        add(campoDefeito);
        add(labelDefeito);
        add(campoServicoPrestado);
        add(labelServicoPrestado);
        add(campoFuncionarioResponsavel);
        add(labelFuncionarioResponsavel);
        add(campoValor);
        add(labelValor);
        add(campoData);
        add(labelData);
        add(comboClientes);
        add(labelCliente);
        add(botaoAdicionar);
        add(botaoSalvarAlteracoes);
        add(botaoRemover);
        add(botaoEditar);
        add(botaoListar);

        // Botões e suas ações
        botaoAdicionar.addActionListener(e -> {
            // Verificar se os campos e comboClientes não são nulos antes de chamar o método
            if (campoEquipamento != null && campoDefeito != null && campoServicoPrestado != null && campoFuncionarioResponsavel != null && campoValor != null && comboClientes != null) {
                adicionarOrdem(campoEquipamento, campoDefeito, campoServicoPrestado, campoFuncionarioResponsavel, campoValor, comboClientes);
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Campos não inicializados corretamente.");
            }
        });        
        botaoEditar.addActionListener(e -> editarOrdem(campoEquipamento, campoDefeito, campoServicoPrestado, campoFuncionarioResponsavel, campoValor, campoData, comboClientes));
        
        botaoSalvarAlteracoes.addActionListener(e -> salvarAlteracoes(campoEquipamento, campoDefeito, campoServicoPrestado, campoFuncionarioResponsavel, campoValor, campoData, comboClientes));

        botaoListar.addActionListener(e -> listarOrdens(tabelaOrdens));
        botaoRemover.addActionListener(e -> {
    String input = JOptionPane.showInputDialog(this, "Digite o ID da ordem de serviço a ser removida:");
    if (input != null && !input.isEmpty()) {
        try {
            long id = Long.parseLong(input);
            removerOrdem(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID deve ser um número válido.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "ID não pode ser vazio.");
    }
});
    }

    private void carregarClientes(JComboBox<Cliente> comboClientes) {
    comboClientes.removeAllItems(); // Limpa os itens existentes
    ClienteDao clienteDao = new ClienteDao();
    List<Cliente> listaClientes = clienteDao.buscarClientes();
    if (listaClientes != null && !listaClientes.isEmpty()) {
        for (Cliente cliente : listaClientes) {
            comboClientes.addItem(cliente);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Não há clientes cadastrados.");
    }
}

    
      private void adicionarOrdem(JTextField campoEquipamento, JTextField campoDefeito, JTextField campoServicoPrestado, JTextField campoFuncionarioResponsavel, JTextField campoValor, JComboBox<Cliente> comboClientes) {
        String equipamento = campoEquipamento.getText().trim();
        String defeito = campoDefeito.getText().trim();
        String servicoPrestado = campoServicoPrestado.getText().trim();
        String funcionarioResponsavel = campoFuncionarioResponsavel.getText().trim();
        String valorStr = campoValor.getText().trim();
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();

        if (equipamento.isEmpty() || defeito.isEmpty() || servicoPrestado.isEmpty() || funcionarioResponsavel.isEmpty() || valorStr.isEmpty() || cliente == null) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        BigDecimal valor;
        try {
            valor = new BigDecimal(valorStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor deve ser um número válido.");
            return;
        }

        OrdemServico novaOrdem = new OrdemServico();
        novaOrdem.setEquipamento(equipamento);
        novaOrdem.setDefeito(defeito);
        novaOrdem.setServicoPrestado(servicoPrestado);
        novaOrdem.setFuncionarioResponsavel(funcionarioResponsavel);
        novaOrdem.setValor(valor);
        novaOrdem.setCliente(cliente);
        novaOrdem.setDataOs(LocalDateTime.now());

        OrdemServicoDao ordemServicoDao = new OrdemServicoDao();
        String mensagem = ordemServicoDao.adicionar(novaOrdem);
        JOptionPane.showMessageDialog(this, mensagem);

        campoEquipamento.setText("");
        campoDefeito.setText("");
        campoServicoPrestado.setText("");
        campoFuncionarioResponsavel.setText("");
        campoValor.setText("");
        comboClientes.setSelectedItem(null);
    }

    private void editarOrdem(JTextField campoEquipamento, JTextField campoDefeito, JTextField campoServicoPrestado, JTextField campoFuncionarioResponsavel, JTextField campoValor, JTextField campoData, JComboBox<Cliente> comboClientes) {
        // Solicita ao usuário o ID ou nome do cliente da ordem de serviço
        String input = JOptionPane.showInputDialog(this, "Digite o ID ou nome do cliente da ordem de serviço a ser editada:");
        OrdemServico ordemServico = null;
        if (input != null && !input.isEmpty()) {
            try {
                long id = Long.parseLong(input);
                System.out.println("Buscando ordem de serviço pelo ID: " + id);
                ordemServico = buscarOrdemServicoPorId(id);  // Função para buscar por ID
            } catch (NumberFormatException ex) {
                System.out.println("Buscando ordem de serviço pelo nome do cliente: " + input);
                ordemServico = buscarOrdemServicoPorNomeCliente(input);  // Função para buscar por nome do cliente
            }

            if (ordemServico != null) {
                // Preencher campos com os dados da ordem de serviço
                campoEquipamento.setText(ordemServico.getEquipamento());
                campoDefeito.setText(ordemServico.getDefeito());
                campoServicoPrestado.setText(ordemServico.getServicoPrestado());
                campoFuncionarioResponsavel.setText(ordemServico.getFuncionarioResponsavel());
                campoValor.setText(ordemServico.getValor().toString());
                campoData.setText(ordemServico.getDataOs().toString());
                comboClientes.setSelectedItem(ordemServico.getCliente());

                // Guardar referência da ordem de serviço que está sendo editada
                ordemServicoEditando = ordemServico;

                editandoOrdemServico = true;
                botaoSalvarAlteracoes.setEnabled(true);
                botaoAdicionar.setEnabled(false);
            } else {
                System.out.println("Ordem de serviço não encontrada.");
                JOptionPane.showMessageDialog(this, "Ordem de serviço não encontrada.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID ou nome não pode ser vazio.");
        }
    }
   

    // Salvar alterações da ordem de serviço
    private void salvarAlteracoes(JTextField campoEquipamento, JTextField campoDefeito, JTextField campoServicoPrestado, JTextField campoFuncionarioResponsavel, JTextField campoValor, JTextField campoData, JComboBox<Cliente> comboClientes) {
        if (editandoOrdemServico && ordemServicoEditando != null) {
            // Obter os dados dos campos de entrada
            String equipamento = campoEquipamento.getText().trim();
            String defeito = campoDefeito.getText().trim();
            String servicoPrestado = campoServicoPrestado.getText().trim();
            String funcionarioResponsavel = campoFuncionarioResponsavel.getText().trim();
            String valorStr = campoValor.getText().trim();
            String dataStr = campoData.getText().trim();

            if (equipamento.isEmpty() || defeito.isEmpty() || servicoPrestado.isEmpty() || funcionarioResponsavel.isEmpty() || valorStr.isEmpty() || dataStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.");
                return;
            }

            // Converter os campos para os tipos apropriados
            BigDecimal valor = new BigDecimal(valorStr);
            LocalDateTime data = LocalDateTime.parse(dataStr);

            Cliente cliente = (Cliente) comboClientes.getSelectedItem();

            // Atualizar os atributos da ordem de serviço
            ordemServicoEditando.setEquipamento(equipamento);
            ordemServicoEditando.setDefeito(defeito);
            ordemServicoEditando.setServicoPrestado(servicoPrestado);
            ordemServicoEditando.setFuncionarioResponsavel(funcionarioResponsavel);
            ordemServicoEditando.setValor(valor);
            ordemServicoEditando.setDataOs(data);
            ordemServicoEditando.setCliente(cliente);

            // Salvar a ordem de serviço no banco de dados
            OrdemServicoDao dao = new OrdemServicoDao();
            dao.salvar(ordemServicoEditando);

            JOptionPane.showMessageDialog(this, "Ordem de serviço atualizada com sucesso.");
            limparCampos(campoEquipamento, campoDefeito, campoServicoPrestado, campoFuncionarioResponsavel, campoValor, campoData, comboClientes);

            editandoOrdemServico = false;
            botaoSalvarAlteracoes.setEnabled(false);
            botaoAdicionar.setEnabled(true);
        }
    }

    private void limparCampos(JTextField campoEquipamento, JTextField campoDefeito, JTextField campoServicoPrestado, JTextField campoFuncionarioResponsavel, JTextField campoValor, JTextField campoData, JComboBox<Cliente> comboClientes) {
        campoEquipamento.setText("");
        campoDefeito.setText("");
        campoServicoPrestado.setText("");
        campoFuncionarioResponsavel.setText("");
        campoValor.setText("");
        campoData.setText("");
        comboClientes.setSelectedIndex(0);
    }

    private void listarOrdens(JTable tabelaOrdens) {
        OrdemServicoDao ordemServicoDao = new OrdemServicoDao();
        List<OrdemServico> ordens = ordemServicoDao.buscarOrdemServico();
        tabelaModelo.setRowCount(0); // Limpa a tabela antes de adicionar os dados
        for (OrdemServico ordem : ordens) {
            tabelaModelo.addRow(new Object[]{
                ordem.getId(),
                ordem.getEquipamento(),
                ordem.getDefeito(),
                ordem.getServicoPrestado(),
                ordem.getFuncionarioResponsavel(),
                ordem.getValor(),
                ordem.getDataOs(),
                ordem.getCliente().getNome()
            });
        }
    }
    // Método para remover a ordem de serviço
private void removerOrdem(long id) {
    OrdemServicoDao dao = new OrdemServicoDao();
    String mensagem = dao.deletarPeloId(id); // Usa seu método deletarPeloId
    JOptionPane.showMessageDialog(this, mensagem);
    listarOrdens(); // Atualiza a lista de ordens após remoção
}

// Método para listar ordens sem precisar de parâmetros
private void listarOrdens() {
    OrdemServicoDao ordemServicoDao = new OrdemServicoDao();
    List<OrdemServico> ordens = ordemServicoDao.buscarOrdemServico();
    tabelaModelo.setRowCount(0); // Limpa a tabela antes de adicionar os dados
    for (OrdemServico ordem : ordens) {
        tabelaModelo.addRow(new Object[]{
            ordem.getId(),
            ordem.getEquipamento(),
            ordem.getDefeito(),
            ordem.getServicoPrestado(),
            ordem.getFuncionarioResponsavel(),
            ordem.getValor(),
            ordem.getDataOs(),
            ordem.getCliente().getNome()
        });
    }
}


    // Funções para buscar ordens de serviço por ID ou nome de cliente
    private OrdemServico buscarOrdemServicoPorId(long id) {
        OrdemServicoDao dao = new OrdemServicoDao();
        return dao.buscarOrdemServicoPeloId(id);
    }

    private OrdemServico buscarOrdemServicoPorNomeCliente(String nomeCliente) {
        OrdemServicoDao dao = new OrdemServicoDao();
        return dao.buscarOrdemServicoPorNomeCliente(nomeCliente);
    }
}
