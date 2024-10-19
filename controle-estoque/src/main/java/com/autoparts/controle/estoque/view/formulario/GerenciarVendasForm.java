package com.autoparts.controle.estoque.view.formulario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
import com.autoparts.controle.estoque.modelo.dao.VendaDao;
import com.autoparts.controle.estoque.modelo.dao.ItemVendaDao;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dao.PecasDao;  // DAO para lidar com peças e estoque
import java.awt.BorderLayout;
import java.awt.Frame;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class GerenciarVendasForm extends JPanel {

    private VendaDao vendaDao;
    private ItemVendaDao itemVendaDao;  // DAO para manipular os itens de venda
    private PecasDao pecasDao;  // Novo DAO para manipular peças e estoque

    private JButton botaoAdicionar;
    private JButton botaoSalvarAlteracoes;
    private JButton botaoRemover;
    private JButton botaoEditarItensVenda;
    private boolean editandoVendas = false;

    public GerenciarVendasForm() {
        setLayout(null);

        // Componentes para gerenciar vendas
        JLabel label = new JLabel("Gerenciar Vendas");
        botaoAdicionar = new JButton("Adicionar Venda");
        botaoSalvarAlteracoes = new JButton("Salvar Alterações");
        botaoRemover = new JButton("Remover Venda");
        botaoEditarItensVenda = new JButton("Editar Itens da Venda");
        JButton botaoBuscarId = new JButton("Buscar por ID");
        JButton botaoBuscarNome = new JButton("Buscar por Nome Cliente");
        JButton botaoListarVendas = new JButton("Listar Vendas");

        // Posições dos botões
        label.setBounds(30, 30, 200, 25);
        botaoAdicionar.setBounds(30, 70, 150, 25);
        botaoRemover.setBounds(30, 110, 150, 25);
        botaoBuscarId.setBounds(30, 150, 150, 25);
        botaoBuscarNome.setBounds(30, 190, 200, 25);
        botaoListarVendas.setBounds(30, 230, 150, 25);
        botaoEditarItensVenda.setBounds(30, 270, 200, 25);

        // Adicionando os botões ao painel
        add(label);
        add(botaoAdicionar);
        add(botaoRemover);
        add(botaoBuscarId);
        add(botaoBuscarNome);
        add(botaoListarVendas);
        add(botaoEditarItensVenda);

        // Ação para adicionar venda
        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exemplo simples de lógica para adicionar uma nova venda
                JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Tela de adicionar venda aberta!");
            }
        });
/*
        // Ação para remover venda
        botaoRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idVenda = JOptionPane.showInputDialog("Digite o ID da venda a remover:");
                if (idVenda != null && !idVenda.isEmpty()) {
                    Long id = Long.parseLong(idVenda);
                    Venda venda = vendaDao.buscarVendaPeloId(id);
                    
                    if (venda != null) {
                        List<ItemVenda> itensVenda = itemVendaDao.buscarItensPorVendaId(id);
                        
                        // Devolver as quantidades para o estoque antes de remover a venda
                        for (ItemVenda item : itensVenda) {
                            Pecas peca = item.getPecas();
                            peca.setQuantidade(peca.getQuantidade() + item.getQuantidade());  // Atualizar o estoque
                            pecasDao.atualizar(peca);  // Atualizar no banco de dados
                        }
                        
                        vendaDao.deletarPeloId(id);
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Venda removida com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Venda não encontrada!");
                    }
                }
            }
        });
*/
        // Ação para buscar venda por ID
        botaoBuscarId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idVenda = JOptionPane.showInputDialog("Digite o ID da venda:");
                if (idVenda != null && !idVenda.isEmpty()) {
                    Long id = Long.parseLong(idVenda);
                    Venda venda = vendaDao.buscarVendaPeloId(id);
                    if (venda != null) {
                        List<ItemVenda> itensVenda = itemVendaDao.buscarItensPorVendaId(id);
                        StringBuilder detalhesVenda = new StringBuilder();
                        detalhesVenda.append("Venda encontrada: ").append(venda).append("\n");
                        detalhesVenda.append("Itens da venda:\n");
                        for (ItemVenda item : itensVenda) {
                            detalhesVenda.append(item).append("\n");
                        }
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, detalhesVenda.toString());
                    } else {
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Venda não encontrada!");
                    }
                }
            }
        });

        // Ação para editar os itens de venda
        botaoEditarItensVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idVenda = JOptionPane.showInputDialog("Digite o ID da venda para editar os itens:");
                if (idVenda != null && !idVenda.isEmpty()) {
                    Long id = Long.parseLong(idVenda);
                    Venda venda = vendaDao.buscarVendaPeloId(id);
                    if (venda != null) {
                        List<ItemVenda> itensVenda = itemVendaDao.buscarItensPorVendaId(id);

                        if (!itensVenda.isEmpty()) {
                            JDialog editarItensDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(GerenciarVendasForm.this), "Editar Itens da Venda", true);
                            editarItensDialog.setSize(400, 300);
                            editarItensDialog.setLayout(new BorderLayout());

                            String[] colunas = {"ID", "Nome do Item", "Quantidade", "Preço"};
                            DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
                            JTable tabelaItens = new JTable(modeloTabela);

                            for (ItemVenda item : itensVenda) {
                                Object[] linha = {item.getId(), item.getPecas().getNome(), item.getQuantidade(), item.getPrecoUnitario()};
                                modeloTabela.addRow(linha);
                            }

                            JScrollPane scrollPane = new JScrollPane(tabelaItens);
                            editarItensDialog.add(scrollPane, BorderLayout.CENTER);

                            JPanel panelBotoes = new JPanel();
                            JButton botaoSalvar = new JButton("Salvar Alterações");
                            JButton botaoFechar = new JButton("Fechar");
                            panelBotoes.add(botaoSalvar);
                            panelBotoes.add(botaoFechar);
                            editarItensDialog.add(panelBotoes, BorderLayout.SOUTH);

                            // Ação para salvar as alterações nos itens da venda
                           /* botaoSalvar.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                                        Long idItem = (Long) modeloTabela.getValueAt(i, 0);
                                        int novaQuantidade = Integer.parseInt(modeloTabela.getValueAt(i, 2).toString());
                                        BigDecimal novoPreco = new BigDecimal(modeloTabela.getValueAt(i, 3).toString());

                                        ItemVenda itemAtualizado = itemVendaDao.buscarItemVendaPeloId(idItem);
                                        Pecas peca = itemAtualizado.getPecas();

                                        // Atualizar o estoque
                                        int diferencaQuantidade = novaQuantidade - itemAtualizado.getQuantidade();
                                        if (peca.getQuantidade() < diferencaQuantidade) {
                                            JOptionPane.showMessageDialog(editarItensDialog, "Estoque insuficiente para o item: " + peca.getNome());
                                            return;
                                        }

                                        peca.setQuantidade(peca.getQuantidade() - diferencaQuantidade);
                                        pecasDao.buscarPecas(peca);

                                        itemAtualizado.setQuantidade(novaQuantidade);
                                        itemAtualizado.setPrecoUnitario(novoPreco);
                                        itemVendaDao.buscarItemVendas(itemAtualizado);
                                    }

                                    JOptionPane.showMessageDialog(editarItensDialog, "Itens da venda atualizados com sucesso!");
                                    editarItensDialog.dispose();
                                }
                            });
*/
                            botaoFechar.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    editarItensDialog.dispose();
                                }
                            });

                            editarItensDialog.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Não há itens para editar nesta venda!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Venda não encontrada!");
                    }
                }
            }
        });
    }
}
