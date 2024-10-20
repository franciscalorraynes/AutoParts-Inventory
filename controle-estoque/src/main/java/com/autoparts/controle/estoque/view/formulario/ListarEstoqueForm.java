package com.autoparts.controle.estoque.view.formulario;


import com.autoparts.controle.estoque.modelo.dao.MovimentacaoEstoqueDao;
import com.autoparts.controle.estoque.modelo.dominio.MovimentacaoEstoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ListarEstoqueForm extends JPanel {
    private DefaultTableModel tabelaModelo;

    public ListarEstoqueForm() {
        setLayout(null);

        JLabel label = new JLabel("Listar Movimentações de Estoque");
        label.setBounds(30, 30, 250, 25);
        
        String[] colunas = {"ID", "Peça", "Tipo de Movimentação", "Quantidade", "Data da Movimentação", "Fornecedor"};
        tabelaModelo = new DefaultTableModel(null, colunas);
        JTable tabelaMovimentacoes = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaMovimentacoes);
        add(scrollPane);
        scrollPane.setBounds(30, 60, 600, 250);

        JButton botaoListar = new JButton("Listar Movimentações");
        botaoListar.setBounds(30, 320, 200, 25);
        add(botaoListar);

        botaoListar.addActionListener(e -> listarMovimentacoes());

        add(label);
    }

    private void listarMovimentacoes() {
    tabelaModelo.setRowCount(0); // Limpa a tabela
    MovimentacaoEstoqueDao movimentacaoEstoqueDao = new MovimentacaoEstoqueDao();
    List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueDao.buscarTodas();
    
    for (MovimentacaoEstoque movimentacao : movimentacoes) {
        String fornecedorNome = (movimentacao.getFornecedor() != null) ? 
                                movimentacao.getFornecedor().getNome() : "N/A"; // Verifica se o fornecedor é nulo
        tabelaModelo.addRow(new Object[]{
            movimentacao.getId(),
            movimentacao.getPecas().getNome(),
            movimentacao.getTipoMovimentacao(),
            movimentacao.getQuantidade(),
            movimentacao.getDataMovimentacao(),
            fornecedorNome // Adiciona o nome do fornecedor ou "N/A"
        });
    }
}

}
