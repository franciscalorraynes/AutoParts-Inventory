package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.OrdemServicoDao;
import com.autoparts.controle.estoque.modelo.dao.RelatorioVendasDao;
import com.autoparts.controle.estoque.modelo.dominio.OrdemServico;
import com.autoparts.controle.estoque.modelo.dominio.RelatorioVendas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarRelatoriosForm extends JPanel {
    private JTabbedPane tabbedPane;

    public ListarRelatoriosForm() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        // Adiciona a aba para Relatórios de Vendas
        tabbedPane.addTab("Relatórios de Vendas", criarPainelVendas());

        // Adiciona a aba para Ordens de Serviço
        tabbedPane.addTab("Ordens de Serviço", criarPainelOrdensServico());
    }

    private JPanel criarPainelVendas() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Relatórios de Vendas:");
        DefaultTableModel tabelaModelo = new DefaultTableModel(new String[]{"ID Venda", "Cliente", "Peça", "Quantidade", "Valor Total", "Data Venda"}, 0);
        JTable tabela = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        JButton botaoAtualizar = new JButton("Atualizar");

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(botaoAtualizar, BorderLayout.SOUTH);

        botaoAtualizar.addActionListener(e -> {
            listarVendas(tabelaModelo);
            JOptionPane.showMessageDialog(this, "Relatórios de vendas atualizados!");
        });

        listarVendas(tabelaModelo); // Listar ao inicializar
        return panel;
    }

    private JPanel criarPainelOrdensServico() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Ordens de Serviço:");
        DefaultTableModel tabelaModelo = new DefaultTableModel(new String[]{"ID Ordem", "Data OS", "Equipamento", "Defeito", "Serviço Prestado", "Funcionário", "Valor", "Cliente"}, 0);
        JTable tabela = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        JButton botaoAtualizar = new JButton("Atualizar");

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(botaoAtualizar, BorderLayout.SOUTH);

        botaoAtualizar.addActionListener(e -> {
            listarOrdensServico(tabelaModelo);
            JOptionPane.showMessageDialog(this, "Ordens de serviço atualizadas!");
        });

        listarOrdensServico(tabelaModelo); // Listar ao inicializar
        return panel;
    }

    private void listarVendas(DefaultTableModel tabelaModelo) {
        tabelaModelo.setRowCount(0); // Limpa a tabela
        RelatorioVendasDao relatorioVendasDao = new RelatorioVendasDao();
        List<RelatorioVendas> vendas = relatorioVendasDao.buscarTodas();

        for (RelatorioVendas venda : vendas) {
            tabelaModelo.addRow(new Object[]{
                venda.getIdVenda(),
                venda.getCliente(),
                venda.getPeca(),
                venda.getQuantidade(),
                venda.getValorTotal(),
                venda.getDataVenda()
            });
        }
    }

    private void listarOrdensServico(DefaultTableModel tabelaModelo) {
    tabelaModelo.setRowCount(0); // Limpa a tabela
    OrdemServicoDao ordemServicoDao = new OrdemServicoDao();
    List<OrdemServico> ordensServico = ordemServicoDao.buscarOrdemServico();

    for (OrdemServico ordemServico : ordensServico) {
        tabelaModelo.addRow(new Object[]{
            ordemServico.getId(),
            ordemServico.getDataOs(),
            ordemServico.getEquipamento(),
            ordemServico.getDefeito(),
            ordemServico.getServicoPrestado(),
            ordemServico.getFuncionarioResponsavel(),
            ordemServico.getValor(),
            ordemServico.getCliente()// Se você quiser exibir o ID do cliente
        });
    }
}
}
