package com.autoparts.controle.estoque.view.formulario;

import javax.swing.*;

public class ListarRelatoriosForm extends JPanel {
    public ListarRelatoriosForm() {
        setLayout(null);

        // Componentes para listar relatórios
        JLabel label = new JLabel("Relatórios:");
        JTextArea textArea = new JTextArea();
        JButton botaoAtualizar = new JButton("Atualizar");

        label.setBounds(30, 30, 80, 25);
        textArea.setBounds(30, 60, 300, 200);
        botaoAtualizar.setBounds(30, 280, 100, 25);

        add(label);
        add(textArea);
        add(botaoAtualizar);

        // Lógica para atualizar relatórios (opcional)
        botaoAtualizar.addActionListener(e -> {
            // Lógica para atualizar a lista de relatórios
            JOptionPane.showMessageDialog(this, "Relatórios atualizados!");
        });
    }
}
