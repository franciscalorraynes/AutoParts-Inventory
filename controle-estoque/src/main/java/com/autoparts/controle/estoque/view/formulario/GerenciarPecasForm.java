package com.autoparts.controle.estoque.view.formulario;

import javax.swing.*;

public class GerenciarPecasForm extends JPanel {
    public GerenciarPecasForm() {
        setLayout(null);

        // Componentes para gerenciar peças
        JLabel label = new JLabel("Gerenciar Peças");
        JButton botaoAdicionar = new JButton("Adicionar Peça");
        JButton botaoRemover = new JButton("Remover Peça");

        label.setBounds(30, 30, 200, 25);
        botaoAdicionar.setBounds(30, 70, 150, 25);
        botaoRemover.setBounds(30, 110, 150, 25);

        add(label);
        add(botaoAdicionar);
        add(botaoRemover);

        //  gerenciar peças (opcional)
        botaoAdicionar.addActionListener(e -> {
            //  adicionar peça
            JOptionPane.showMessageDialog(this, "Peça adicionada!");
        });
        
        botaoRemover.addActionListener(e -> {
            //  remover peça
            JOptionPane.showMessageDialog(this, "Peça removida!");
        });
    }
}
