package com.autoparts.controle.estoque.view.formulario;

import javax.swing.*;

public class ListarRelatoriosVendasForm extends JPanel {
    public ListarRelatoriosVendasForm() {
        setLayout(null);

        // Componentes para listar relatórios de vendas
        JLabel label = new JLabel("Relatórios de Vendas:");
        JTextArea textArea = new JTextArea();
        JButton botaoAtualizar = new JButton("Atualizar");

        label.setBounds(30, 30, 150, 25);
        textArea.setBounds(30, 60, 300, 200);
        botaoAtualizar.setBounds(30, 280, 100, 25);

        add(label);
        add(textArea);
        add(botaoAtualizar);

        //  atualizar relatórios de vendas (opcional)
        botaoAtualizar.addActionListener(e -> {
            //  atualizar a lista de relatórios de vendas
            JOptionPane.showMessageDialog(this, "Relatórios de vendas atualizados!");
        });
    }
}
