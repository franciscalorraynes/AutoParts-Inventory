
package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.FornecedorDao;
import com.autoparts.controle.estoque.modelo.dao.MovimentacaoEstoqueDao;
import com.autoparts.controle.estoque.modelo.dao.PecasDao;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dominio.Fornecedor; // Importe a classe Fornecedores

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GerenciarPecasForm extends JPanel {

    private Pecas pecasEditando = null;

    private DefaultTableModel tabelaModelo;
    private JButton botaoAdicionar;
    private JButton botaoSalvarAlteracoes;
    private boolean editandoPecas = false;

    public GerenciarPecasForm() {
        setLayout(null);

        JLabel label = new JLabel("Gerenciar Peças");
        botaoAdicionar = new JButton("Adicionar Peça");
        botaoSalvarAlteracoes = new JButton("Salvar Alterações");
        JButton botaoRemover = new JButton("Remover Peça");
        JButton botaoEditar = new JButton("Editar Peça");
        JButton botaoListar = new JButton("Listar Peças");

        JTextField campoNome = new JTextField(20);
        JTextField campoDescricao = new JTextField(20);
        JTextField campoQuantidade = new JTextField(20);
        JTextField campoPreco = new JTextField(20);

        // Adicione um JComboBox para selecionar fornecedores
        JComboBox<Fornecedor> comboFornecedores = new JComboBox<>();
        carregarFornecedores(comboFornecedores); // Método para carregar fornecedores

        JLabel labelNome = new JLabel("Nome: ");
        JLabel labelDescricao = new JLabel("Descrição: ");
        JLabel labelQuantidade = new JLabel("Quantidade: ");
        JLabel labelPreco = new JLabel("Preço: ");
        JLabel labelFornecedor = new JLabel("Fornecedor: ");

        label.setBounds(30, 30, 200, 25);
        campoNome.setBounds(100, 60, 150, 25);
        labelNome.setBounds(30, 60, 70, 25);
        campoDescricao.setBounds(100, 100, 150, 25);
        labelDescricao.setBounds(30, 100, 70, 25);
        campoQuantidade.setBounds(100, 140, 150, 25);
        labelQuantidade.setBounds(30, 140, 90, 25);
        campoPreco.setBounds(100, 180, 150, 25);
        labelPreco.setBounds(30, 180, 70, 25);
        comboFornecedores.setBounds(100, 220, 150, 25);
        labelFornecedor.setBounds(30, 220, 90, 25);

        botaoAdicionar.setBounds(30, 260, 150, 25);
        botaoSalvarAlteracoes.setBounds(30, 300, 150, 25);
        botaoRemover.setBounds(30, 340, 150, 25);
        botaoEditar.setBounds(30, 380, 150, 25);
        botaoListar.setBounds(30, 420, 150, 25);

        String[] colunas = {"ID", "Nome", "Descrição", "Quantidade", "Preço", "Data Criação", "Fornecedor"};
        tabelaModelo = new DefaultTableModel(null, colunas);
        JTable tabelaPecas = new JTable(tabelaModelo);
        tabelaPecas.setBounds(300, 60, 600, 250);
        JScrollPane scrollPane = new JScrollPane(tabelaPecas);
        add(scrollPane);
        scrollPane.setBounds(300, 60, 600, 250);

        add(label);
        add(campoNome);
        add(labelNome);
        add(campoDescricao);
        add(labelDescricao);
        add(campoQuantidade);
        add(labelQuantidade);
        add(campoPreco);
        add(labelPreco);
        add(comboFornecedores); // Adiciona o JComboBox ao formulário
        add(labelFornecedor);
        add(botaoAdicionar);
        add(botaoSalvarAlteracoes);
        add(botaoRemover);
        add(botaoEditar);
        add(botaoListar);

        /// Lógica para adicionar peça
botaoAdicionar.addActionListener(e -> {
    String nome = campoNome.getText().trim();
    String descricao = campoDescricao.getText().trim();
    String quantidadeStr = campoQuantidade.getText().trim();
    String precoStr = campoPreco.getText().trim();
    Fornecedor fornecedor = (Fornecedor) comboFornecedores.getSelectedItem();

    if (nome.isEmpty() || descricao.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty() || fornecedor == null) {
        JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
        return;
    }

    int quantidade;
    BigDecimal preco;
    try {
        quantidade = Integer.parseInt(quantidadeStr);
        preco = new BigDecimal(precoStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Quantidade e Preço devem ser números válidos.");
        return;
    }

    Pecas pecas = new Pecas();
    pecas.setNome(nome);
    pecas.setDescricao(descricao);
    pecas.setQuantidade(quantidade);
    pecas.setPreco(preco);
    pecas.setDataCriacao(LocalDateTime.now()); // Define a data de criação
    pecas.setFornecedor(fornecedor); // Define o fornecedor

    PecasDao pecasDao = new PecasDao();
    String mensagem = pecasDao.salvar(pecas);
    
    // Verifica se o ID da peça foi gerado corretamente
    if (pecas.getId() == null) {
        JOptionPane.showMessageDialog(this, "Erro: ID da peça não foi gerado corretamente.");
        return;
    }

    JOptionPane.showMessageDialog(this, mensagem);

    // Verifica se o ID do fornecedor foi definido corretamente
    if (fornecedor.getId() == null) {
        JOptionPane.showMessageDialog(this, "Erro: ID do fornecedor não foi gerado corretamente.");
        return;
    }

    // Após salvar a peça, adiciona no estoque
    MovimentacaoEstoqueDao estoqueDao = new MovimentacaoEstoqueDao();
    estoqueDao.adicionarPecasAoEstoque(pecas.getId(), quantidade, fornecedor.getId());
    JOptionPane.showMessageDialog(this, "Estoque atualizado.");

    // Limpar campos após salvar
    campoNome.setText("");
    campoDescricao.setText("");
    campoQuantidade.setText("");
    campoPreco.setText("");
    comboFornecedores.setSelectedIndex(0); // Resetar o JComboBox
});


        // Alterações da peça
        botaoSalvarAlteracoes.addActionListener(e -> {
            if (editandoPecas && pecasEditando != null) {
                String nome = campoNome.getText().trim();
                String descricao = campoDescricao.getText().trim();
                String quantidadeStr = campoQuantidade.getText().trim();
                String precoStr = campoPreco.getText().trim();
                Fornecedor fornecedor = (Fornecedor) comboFornecedores.getSelectedItem();

                if (nome.isEmpty() || descricao.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty() || fornecedor == null) {
                    JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                    return;
                }

                int quantidade;
                BigDecimal preco;
                try {
                    quantidade = Integer.parseInt(quantidadeStr);
                    preco = new BigDecimal(precoStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantidade e Preço devem ser números válidos.");
                    return;
                }

                // Atualizar a peça em edição
                pecasEditando.setNome(nome);
                pecasEditando.setDescricao(descricao);
                pecasEditando.setQuantidade(quantidade);
                pecasEditando.setPreco(preco);
                pecasEditando.setFornecedor(fornecedor); // Atualiza o fornecedor

                PecasDao pecasDao = new PecasDao();
                String mensagem = pecasDao.salvar(pecasEditando); // Salva as alterações
                JOptionPane.showMessageDialog(this, mensagem);

                // Resetar estado após salvar
                editandoPecas = false;
                pecasEditando = null;
                botaoSalvarAlteracoes.setEnabled(false);
                botaoAdicionar.setEnabled(true);

                // Limpar campos após salvar
                campoNome.setText("");
                campoDescricao.setText("");
                campoQuantidade.setText("");
                campoPreco.setText("");
                comboFornecedores.setSelectedIndex(0); // Resetar o JComboBox
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma peça está sendo editada.");
            }
        });

        // Remover peça
botaoRemover.addActionListener(e -> {
    String input = JOptionPane.showInputDialog(this, "Digite o ID ou nome da peça a ser removida:");
    if (input != null && !input.isEmpty()) {
        PecasDao pecasDao = new PecasDao();
        Pecas pecas = null;

        // Tenta buscar a peça pelo ID primeiro
        try {
            long id = Long.parseLong(input);
            pecas = pecasDao.buscarPecasPeloId(id);
        } catch (NumberFormatException ex) {
            // Se falhar, tenta buscar pelo nome
            pecas = pecasDao.buscarPecasPeloNome(input);
        }

        if (pecas != null) {
            String mensagem = pecasDao.deletarPeloId(pecas.getId()); // Use o método correto
            JOptionPane.showMessageDialog(this, mensagem);
            
            // Se a peça foi removida com sucesso, remova a linha da tabela
            if (mensagem.equals("Peca deletado com sucesso!")) {
                for (int i = 0; i < tabelaModelo.getRowCount(); i++) {
                    if (tabelaModelo.getValueAt(i, 1).equals(pecas.getNome())) { // Verifica o nome na coluna apropriada
                        tabelaModelo.removeRow(i); // Remove a linha correspondente
                        break; // Sai do loop após encontrar e remover
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Peça não encontrada.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Nome ou ID da peça não pode ser vazio.");
    }
});
 
        
      botaoEditar.addActionListener(e -> {
    String input = JOptionPane.showInputDialog(this, "Digite o ID ou nome da peça a ser editada:");
    Pecas pecas = null;
    if (input != null && !input.isEmpty()) {
        try {
            long id = Long.parseLong(input);
            System.out.println("Buscando peça pelo ID: " + id);
            pecas = buscarPecasPorId(id);
        } catch (NumberFormatException ex) {
            System.out.println("Buscando peça pelo nome: " + input);
            pecas = buscarPecasPorNome(input);
        }

        if (pecas != null) {
            // Preencher campos com os dados da peça
            campoNome.setText(pecas.getNome());
            campoDescricao.setText(pecas.getDescricao());
            campoQuantidade.setText(String.valueOf(pecas.getQuantidade()));
            campoPreco.setText(pecas.getPreco().toString());
            comboFornecedores.setSelectedItem(pecas.getFornecedor());

            // Guardar referência à peça que está sendo editada
            pecasEditando = pecas;

            editandoPecas = true;
            botaoSalvarAlteracoes.setEnabled(true);
            botaoAdicionar.setEnabled(false);
        } else {
            System.out.println("Peça não encontrada.");
            JOptionPane.showMessageDialog(this, "Peça não encontrada.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Nome ou ID não pode ser vazio.");
    }
});
// // Salvar alterações da peça
botaoSalvarAlteracoes.addActionListener(e -> {
    if (editandoPecas && pecasEditando != null) {
        String nome = campoNome.getText().trim();
        String descricao = campoDescricao.getText().trim();
        String quantidadeStr = campoQuantidade.getText().trim();
        String precoStr = campoPreco.getText().trim();

        if (nome.isEmpty() || descricao.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        // Converter a quantidade e preço
        int quantidade;
        BigDecimal preco;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            preco = new BigDecimal(precoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números válidos.");
            return;
        }

        // Atualizar a peça em edição
        pecasEditando.setNome(nome);
        pecasEditando.setDescricao(descricao);
        pecasEditando.setQuantidade(quantidade);
        pecasEditando.setPreco(preco);
        pecasEditando.setFornecedor((Fornecedor) comboFornecedores.getSelectedItem()); // Atribui o fornecedor selecionado

        PecasDao pecasDao = new PecasDao();
        String mensagem = pecasDao.salvar(pecasEditando); // Salva as alterações
        JOptionPane.showMessageDialog(this, mensagem);

        // Resetar estado após salvar
        editandoPecas = false;
        pecasEditando = null; // Limpar peça em edição
        botaoSalvarAlteracoes.setEnabled(false); // Desabilita o botão de salvar após salvar
        botaoAdicionar.setEnabled(true); // Reabilita o botão de adicionar

        // Limpar campos após salvar
        campoNome.setText("");
        campoDescricao.setText("");
        campoQuantidade.setText("");
        campoPreco.setText("");
        comboFornecedores.setSelectedIndex(-1); // Limpa a seleção do fornecedor
    } else {
        JOptionPane.showMessageDialog(this, "Nenhuma peça está sendo editada.");
    }
});


        // Listar peças
        botaoListar.addActionListener(e -> {
            PecasDao pecasDao = new PecasDao();
            var listaPecas = pecasDao.buscarPecas();

            if (listaPecas != null && !listaPecas.isEmpty()) {
                tabelaModelo.setRowCount(0);

                for (Pecas pecasDaLista : listaPecas) {
                    Object[] linha = {pecasDaLista.getId(), pecasDaLista.getNome(), pecasDaLista.getDescricao(),
                        pecasDaLista.getQuantidade(), pecasDaLista.getPreco(),
                        pecasDaLista.getDataCriacao(), pecasDaLista.getFornecedor()}; // Adicione a data e o fornecedor
                    tabelaModelo.addRow(linha);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma peça encontrada.");
            }
        });

        // Validação do campo quantidade para aceitar apenas números
        campoQuantidade.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Impede a digitação de caracteres não numéricos
                }
            }
        });

        // Validação do campo preço para aceitar apenas números e ponto
        campoPreco.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume(); // Impede a digitação de caracteres não numéricos
                }
            }
        });
    }

    // Método para carregar fornecedores no JComboBox
    private void carregarFornecedores(JComboBox<Fornecedor> comboFornecedores) { // Alterado para Fornecedor
        FornecedorDao fornecedoresDao = new FornecedorDao();
        List<Fornecedor> listaFornecedores = fornecedoresDao.buscarFornecedores(); // Alterado para Fornecedor
        for (Fornecedor fornecedor : listaFornecedores) {
            comboFornecedores.addItem(fornecedor); // Adiciona o fornecedor ao JComboBox
        }
    }

    public Pecas buscarPecasPorId(Long id) {
    PecasDao pecasDao = new PecasDao();
    return pecasDao.buscarPecasPeloId(id);
}

public Pecas buscarPecasPorNome(String nome) {
    PecasDao pecasDao = new PecasDao();
    return pecasDao.buscarPecasPeloNome(nome);
}

}
