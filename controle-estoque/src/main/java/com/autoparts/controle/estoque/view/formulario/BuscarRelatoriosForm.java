package com.autoparts.controle.estoque.view.formulario;

import javax.swing.*;

public class BuscarRelatoriosForm extends JPanel {
    public BuscarRelatoriosForm() {
        setLayout(null);

        // Componentes para buscar relatório
        JLabel labelId = new JLabel("ID da Venda:");
        JTextField txtId = new JTextField();
        JButton botaoBuscar = new JButton("Buscar");

        labelId.setBounds(30, 30, 80, 25);
        txtId.setBounds(120, 30, 150, 25);
        botaoBuscar.setBounds(120, 70, 150, 25);

        add(labelId);
        add(txtId);
        add(botaoBuscar);

        // buscar relatório (opcional)
        botaoBuscar.addActionListener(e -> {
            // buscar o relatório
            JOptionPane.showMessageDialog(this, "Relatório encontrado!");
        });
    }
}
