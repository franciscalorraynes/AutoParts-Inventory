
package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dao.ItemVendaDao;
import com.autoparts.controle.estoque.modelo.dao.MovimentacaoEstoqueDao;
import com.autoparts.controle.estoque.modelo.dao.PecasDao;
import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dao.VendaDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;  
import com.autoparts.controle.estoque.modelo.dominio.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GerenciarVendasForm extends JPanel {

    // Variáveis de instância
    private Venda vendaEditando = null;
    private VendaDao vendaDao;
    private Usuario usuario;  // Usuário autenticado (logado)
    private JTextField campoIdVenda, campoDesconto, campoTroco, campoObservacao, campoQuantidade, campoPrecoTotal;
    private JComboBox<Pecas> comboPecas;
    private JComboBox<Cliente> comboClientes;
    private DefaultTableModel tabelaModelo;
    private JComboBox<Usuario> comboUsuarios;  // Novo JComboBox para usuários
    private JButton botaoAdicionarVenda, botaoSalvarAlteracoes;
    private JLabel labelUsuario;
    private JTable tabelaVendas;
    private boolean editandoVenda = false; // Flag para controle de edição


    public GerenciarVendasForm(Usuario usuario) {
        this.usuario = usuario;  // Armazena o usuário autenticado
        setLayout(null);
        inicializarComponentes();
        vendaDao = new VendaDao();
    }

   private void inicializarComponentes() {
    // Inicializa e define os componentes da interface gráfica
    JLabel label = new JLabel("Gerenciar Vendas");
    label.setBounds(30, 20, 200, 25);
    add(label);
    
    // Afastamento do JComboBox de Usuários
    JLabel labelEspaco1 = new JLabel(); // Para adicionar espaço
    labelEspaco1.setBounds(30, 145, 60, 25); // Ajustar o afastamento como preferir
    add(labelEspaco1);

    // Label e JComboBox para Usuários
    JLabel labelUsuario = new JLabel("Usuário:");
    comboUsuarios = new JComboBox<>();
    labelUsuario.setBounds(30, 60, 60, 25); // Alterado para uma posição acima (linha diferente)
comboUsuarios.setBounds(120, 60, 150, 25); // Ajuste a posição se necessário
    carregarUsuarios(comboUsuarios);  // Carregar usuários no comboBox
    add(labelUsuario);
    add(comboUsuarios);

    JLabel labelEspaco = new JLabel(); // Para adicionar espaço
    labelEspaco.setBounds(30, 145, 60, 25); // Ajustar o afastamento como preferir
    add(labelEspaco);

    // Campos de entrada e rótulos
    JLabel labelDesconto = new JLabel("Desconto: ");
    campoDesconto = new JTextField(20);
    labelDesconto.setBounds(30, 100, 90, 25);
    campoDesconto.setBounds(120, 100, 150, 25);
    add(labelDesconto);
    add(campoDesconto);

    JLabel labelTroco = new JLabel("Troco: ");
    campoTroco = new JTextField(20);
    labelTroco.setBounds(30, 140, 90, 25);
    campoTroco.setBounds(120, 140, 150, 25);
    add(labelTroco);
    add(campoTroco);

    JLabel labelObservacao = new JLabel("Observação: ");
    campoObservacao = new JTextField(20);
    labelObservacao.setBounds(30, 180, 90, 25);
    campoObservacao.setBounds(120, 180, 150, 25);
    add(labelObservacao);
    add(campoObservacao);

    JLabel labelPeca = new JLabel("Peça:");
    comboPecas = new JComboBox<>();
    labelPeca.setBounds(30, 220, 60, 25); // Mover para baixo
    comboPecas.setBounds(120, 220, 150, 25); // Mover para baixo
    carregarPecas(comboPecas);
    add(labelPeca);
    add(comboPecas);

    JLabel labelCliente = new JLabel("Cliente:");
    comboClientes = new JComboBox<>();
    labelCliente.setBounds(30, 260, 60, 25); // Mover para baixo
    comboClientes.setBounds(120, 260, 150, 25); // Mover para baixo
    carregarClientes(comboClientes);
    add(labelCliente);
    add(comboClientes);

    JLabel labelQuantidade = new JLabel("Quantidade:");
    campoQuantidade = new JTextField(20);
    labelQuantidade.setBounds(300, 60, 100, 25);
    campoQuantidade.setBounds(400, 60, 100, 25);
    add(labelQuantidade);
    add(campoQuantidade);

    JLabel labelPrecoTotal = new JLabel("Preço Total:");
    campoPrecoTotal = new JTextField(20);
    labelPrecoTotal.setBounds(300, 100, 100, 25);
    campoPrecoTotal.setBounds(400, 100, 100, 25);
    add(labelPrecoTotal);
    add(campoPrecoTotal);

    // Botões
    botaoAdicionarVenda = new JButton("Adicionar Venda");
    botaoSalvarAlteracoes = new JButton("Salvar Alterações");
    JButton botaoRemover = new JButton("Remover Venda");
    JButton botaoListar = new JButton("Listar Vendas");
    JButton botaoEditar = new JButton("Editar Venda");

    botaoAdicionarVenda.setBounds(30, 300, 150, 25);
    botaoSalvarAlteracoes.setBounds(30, 340, 150, 25);
    botaoRemover.setBounds(30, 380, 150, 25);
    botaoListar.setBounds(30, 420, 150, 25); // Mover para baixo
    botaoEditar.setBounds(30, 460, 150, 25); // Adicionar o botão de editar

    add(botaoAdicionarVenda);
    add(botaoSalvarAlteracoes);
    add(botaoRemover);
    add(botaoListar);
    add(botaoEditar); // Adicionar o botão de editar

    // Tabela de Vendas
    String[] colunas = {"ID", "Peça", "Cliente", "Quantidade", "Preço Total", "Data da Venda", "Usuário"};
    tabelaModelo = new DefaultTableModel(null, colunas);
    tabelaVendas = new JTable(tabelaModelo);
    JScrollPane scrollPane = new JScrollPane(tabelaVendas);
    scrollPane.setBounds(300, 140, 600, 250);
    add(scrollPane);

    // Ação dos botões
    botaoAdicionarVenda.addActionListener(e -> {
        adicionarVenda();
    });
    
    /*
   botaoEditar.addActionListener(e -> editarVenda());

    botaoSalvarAlteracoes.addActionListener(e -> {
        // Lógica para salvar alterações
        if (!campoIdVenda.getText().isEmpty()) {
            editarVenda(); // Se o campo ID não estiver vazio, edita
        } else {
            adicionarVenda(); // Se estiver vazio, adiciona uma nova venda
        }
    });
*/
    botaoListar.addActionListener(e -> listarVendas());

}
    // Carregar Peças no comboBox
    private void carregarPecas(JComboBox<Pecas> comboPecas) {
        PecasDao pecasDao = new PecasDao();
        List<Pecas> pecasList = pecasDao.buscarPecas();
        for (Pecas peca : pecasList) {
            comboPecas.addItem(peca);
        }
    }

    // Carregar Clientes no comboBox
    private void carregarClientes(JComboBox<Cliente> comboClientes) {
        ClienteDao clienteDao = new ClienteDao();
        List<Cliente> clientesList = clienteDao.buscarClientes();
        for (Cliente cliente : clientesList) {
            comboClientes.addItem(cliente);
        }
    }

    private void carregarUsuarios(JComboBox<Usuario> comboUsuarios) {
        UsuarioDao usuarioDao = new UsuarioDao();
        List<Usuario> listaUsuarios = usuarioDao.buscarUsuarios(); // Método que deve retornar a lista de usuários
        for (Usuario usuario : listaUsuarios) {
            comboUsuarios.addItem(usuario);
        }
    }

     private void carregarVendasNaTabela() {
        VendaDao vendaDao = new VendaDao();
        List<Venda> vendas = vendaDao.buscarVendas();
        tabelaModelo.setRowCount(0);  // Limpar a tabela antes de adicionar novos dados

        for (Venda venda : vendas) {
            Object[] linha = {
                venda.getId(),
                venda.getItensVenda().get(0).getPecas().getNome(),
                venda.getCliente().getNome(), // Nome do cliente
                venda.getItensVenda().get(0).getQuantidade(),
                venda.getTotalDaVenda(),
                venda.getDataVenda(),
                venda.getUsuario().getNome()  // Nome do usuário que fez a venda
            };
            tabelaModelo.addRow(linha);  // Adicionar linha à tabela
        }
    }
    private void adicionarVenda() {
    PecasDao pecasDao = new PecasDao();

    // Buscar peça selecionada
    Pecas pecaSelecionada = (Pecas) comboPecas.getSelectedItem();
    if (pecaSelecionada == null) {
        JOptionPane.showMessageDialog(this, "Selecione uma peça.");
        return;
    }

    // Buscar cliente selecionado
    Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
    if (clienteSelecionado == null) {
        JOptionPane.showMessageDialog(this, "Selecione um cliente.");
        return;
    }

    // Validar campos de entrada
    String quantidadeStr = campoQuantidade.getText().trim();
    String precoUnitarioStr = campoPrecoTotal.getText().trim();
    
    if (quantidadeStr.isEmpty() || precoUnitarioStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios.");
        return;
    }

    // Converter os valores
    int quantidade;
    BigDecimal precoUnitario;
    try {
        quantidade = Integer.parseInt(quantidadeStr);
        precoUnitario = new BigDecimal(precoUnitarioStr); // Certifique-se de que este é um número válido
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Quantidade e Preço Total devem ser números válidos.");
        return;
    }

    // Verifica se há estoque suficiente
    if (pecaSelecionada.getQuantidade() < quantidade) {
        JOptionPane.showMessageDialog(this, "Estoque insuficiente para a quantidade solicitada.");
        return;
    }

    // Criar novo item de venda
    ItemVenda itemVenda = new ItemVenda();
    itemVenda.setPecas(pecaSelecionada);
    itemVenda.setQuantidade(quantidade);
    itemVenda.setPrecoUnitario(precoUnitario);

    // Criar nova venda
    Venda venda = new Venda();
    venda.setCliente(clienteSelecionado);
    venda.setUsuario(usuario);  // Usuário autenticado
    venda.setDataVenda(LocalDateTime.now());

    // Adicionar item à venda
    List<ItemVenda> itensVenda = new ArrayList<>();
    itensVenda.add(itemVenda);
    venda.setItensVenda(itensVenda);

    // Calcular total da venda
    BigDecimal totalDaVenda = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    venda.setTotalDaVenda(totalDaVenda);

    // Salvar a venda no banco de dados
    VendaDao vendaDao = new VendaDao();
    try {
        Long vendaId = Long.valueOf(vendaDao.salvar(venda)); // Certifique-se de que o método `salvar` retorna o ID da venda como `Long`
        venda.setId(vendaId); // Definir ID da venda para uso posterior

        // Depois salve cada ItemVenda no banco, associando-o à venda
        for (ItemVenda item : itensVenda) {
            item.setVenda(venda); // Vincula a venda ao item
            vendaDao.salvarItemVenda(vendaId, item); // Crie este método para salvar ItemVenda
        }

        JOptionPane.showMessageDialog(this, "Venda salva com sucesso!");
        
        // Atualizar estoque como SAIDA
        MovimentacaoEstoqueDao estoqueDao = new MovimentacaoEstoqueDao();
        
        // Registrar a saída no estoque
        estoqueDao.registrarSaidaEstoque(pecaSelecionada.getId(), quantidade); // Mantenha apenas esta linha
        
        // Limpar os campos após salvar
        comboPecas.setSelectedIndex(0);
        comboClientes.setSelectedIndex(0);
        campoQuantidade.setText("");
        campoPrecoTotal.setText("");
        campoDesconto.setText("");
        campoTroco.setText("");
        campoObservacao.setText("");
        
        // Atualizar tabela de vendas
        carregarVendasNaTabela();  // Atualizar a tabela após adicionar
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar venda: " + ex.getMessage());
    }
}


    
    
private void listarVendas() {
    // Limpar a tabela antes de listar
    tabelaModelo.setRowCount(0); // Limpa todas as linhas da tabela

    // Criar uma instância do VendaDao
    VendaDao vendaDao = new VendaDao();
    ItemVendaDao itemVendaDao = new ItemVendaDao(); // Criar uma instância do ItemVendaDao

    // Obter a lista de vendas do DAO
    List<Venda> vendas = vendaDao.buscarVendas();
    System.out.println("Vendas encontradas: " + vendas.size()); // Log do número de vendas

    // Verificar se a lista de vendas está vazia
    if (vendas.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nenhuma venda encontrada."); // Mensagem caso não haja vendas
    } else {
        // Preencher a tabela com as vendas
        for (Venda venda : vendas) {
            System.out.println("Processando venda ID: " + venda.getId()); // Log da venda em processamento

            // Verificação de nulos para evitar NullPointerException
            String nomeCliente = venda.getCliente() != null ? venda.getCliente().getNome() : "Cliente não encontrado";
            String nomeUsuario = venda.getUsuario() != null ? venda.getUsuario().getNome() : "Usuário não encontrado";
            LocalDateTime dataVenda = venda.getDataVenda(); // Obter a data de venda diretamente

            // Formatar LocalDateTime para dd/MM/yyyy HH:mm
            String dataVendaFormatada = dataVenda != null ? 
                dataVenda.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Data não disponível";

            // Usar ItemVendaDao para buscar os itens da venda no banco de dados
            List<ItemVenda> itensVenda = itemVendaDao.buscarItensPorVendaId(venda.getId()); // Usa o ID da venda como Long
            if (itensVenda != null && !itensVenda.isEmpty()) {
                for (ItemVenda item : itensVenda) {
                    String nomePeca = item.getPecas() != null ? item.getPecas().getNome() : "Peça não encontrada";
                    int quantidade = item.getQuantidade(); // Obter a quantidade do item

                    // Criar a linha com os dados da venda
                    Object[] linha = {
                        venda.getId(),       // ID da venda
                        nomePeca,            // Nome da peça
                        nomeCliente,         // Nome do cliente
                        quantidade,          // Quantidade de peças
                        venda.getTotalDaVenda(), // Preço total
                        dataVendaFormatada,  // Data da venda (formatada)
                        nomeUsuario          // Nome do usuário
                    };

                    tabelaModelo.addRow(linha); // Adiciona a linha na tabela
                    System.out.println("Adicionando linha: " + Arrays.toString(linha)); // Log da linha adicionada
                }
            } else {
                // Caso não haja itens de venda, exiba uma mensagem
                System.out.println("Venda ID " + venda.getId() + " não possui itens.");
            }
        }

        // Atualiza a tabela para refletir as mudanças
        tabelaModelo.fireTableDataChanged();
    }
}

/*
private void editarVenda() {
    // Solicita ao usuário o ID ou nome do cliente da venda
    String input = JOptionPane.showInputDialog(this, "Digite o ID ou nome do cliente da venda a ser editada:");
    Venda venda = null;

    if (input != null && !input.isEmpty()) {
        try {
            // Tenta buscar por ID
            long id = Long.parseLong(input);
            System.out.println("Buscando venda pelo ID: " + id);
            venda = vendaDao.buscarVendaPeloId(id); // Função para buscar por ID
        } catch (NumberFormatException ex) {
            // Se não for um número, busca por nome do cliente
            System.out.println("Buscando venda pelo nome do cliente: " + input);
            venda = vendaDao.(input); // Função para buscar por nome do cliente
        }

        if (venda != null) {
            // Preencher campos com os dados da venda
            campoDesconto.setText(venda.getDesconto() != null ? venda.getDesconto().toString() : "");
            campoTroco.setText(venda.getTroco() != null ? venda.getTroco().toString() : "");
            campoObservacao.setText(venda.getObservacao() != null ? venda.getObservacao() : "");

            // Selecionar cliente e usuário correspondentes
            comboClientes.setSelectedItem(venda.getCliente());
            comboUsuarios.setSelectedItem(venda.getUsuario());

            // Limpar o combo de peças e preencher com os itens da venda
            comboPecas.removeAllItems();
            for (ItemVenda item : venda.getItensVenda()) {
                comboPecas.addItem(item.getPecas()); // Adiciona todas as peças da venda ao combo
            }

            // Seleciona a primeira peça como padrão
            if (!venda.getItensVenda().isEmpty()) {
                comboPecas.setSelectedIndex(0);
                ItemVenda primeiroItem = venda.getItensVenda().get(0);
                campoQuantidade.setText(String.valueOf(primeiroItem.getQuantidade()));
                campoPrecoTotal.setText(primeiroItem.getPrecoUnitario().toString());
            }

            // Guardar referência à venda que está sendo editada
            vendaEditando = venda;
            editandoVenda = true;

            // Habilitar/Desabilitar botões conforme necessário
            botaoSalvarAlteracoes.setEnabled(true);
            botaoAdicionarVenda.setEnabled(false); // Desabilita adicionar enquanto edita
        } else {
            JOptionPane.showMessageDialog(this, "Venda não encontrada.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "ID ou nome não pode ser vazio.");
    }
}

private void salvarAlteracoesVenda() throws SQLException {
    if (editandoVenda && vendaEditando != null) {
        String descontoStr = campoDesconto.getText().trim();
        String trocoStr = campoTroco.getText().trim();
        String observacao = campoObservacao.getText().trim();

        // Verifica se os campos necessários estão preenchidos
        if (descontoStr.isEmpty() || trocoStr.isEmpty() || observacao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        // Converter desconto e troco
        BigDecimal desconto;
        BigDecimal troco;
        try {
            desconto = new BigDecimal(descontoStr);
            troco = new BigDecimal(trocoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Desconto e troco devem ser números válidos.");
            return;
        }

        // Atualizar a venda em edição
        vendaEditando.setDesconto(desconto);
        vendaEditando.setTroco(troco);
        vendaEditando.setObservacao(observacao);
        vendaEditando.setCliente((Cliente) comboClientes.getSelectedItem()); // Atribui o cliente selecionado
        vendaEditando.setUsuario((Usuario) comboUsuarios.getSelectedItem()); // Atribui o usuário selecionado

        VendaDao vendaDao = new VendaDao();
        long idAtualizado = vendaDao.salvar(vendaEditando); // Salva as alterações e obtém o ID da venda atualizada

        // Exibe uma mensagem de sucesso com o ID da venda atualizada
        JOptionPane.showMessageDialog(this, "Venda atualizada com sucesso! ID: " + idAtualizado);

        // Resetar estado após salvar
        editandoVenda = false;
        vendaEditando = null; // Limpar venda em edição
        botaoSalvarAlteracoes.setEnabled(false); // Desabilita o botão de salvar após salvar
        botaoAdicionarVenda.setEnabled(true); // Reabilita o botão de adicionar

        // Limpar campos após salvar
        campoDesconto.setText("");
        campoTroco.setText("");
        campoObservacao.setText("");
        comboClientes.setSelectedIndex(-1); // Limpa a seleção do cliente
        comboUsuarios.setSelectedIndex(-1); // Limpa a seleção do usuário
    } else {
        JOptionPane.showMessageDialog(this, "Nenhuma venda está sendo editada.");
    }
}



/*
    private void removerVenda() {
        try {
            Long idVenda = Long.parseLong(campoIdVenda.getText()); // Pega o ID da venda
            VendaDao vendaDao = new VendaDao();
            vendaDao.(idVenda); // Implementar este método no VendaDao
            JOptionPane.showMessageDialog(this, "Venda removida com sucesso.");
            carregarVendasNaTabela(); // Atualiza a tabela de vendas
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID de venda válido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover a venda: " + e.getMessage());
        }
    }
}
*/

    


}

