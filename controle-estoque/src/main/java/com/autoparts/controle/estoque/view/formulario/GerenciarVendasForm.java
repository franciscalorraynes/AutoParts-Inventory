package com.autoparts.controle.estoque.view.formulario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import com.autoparts.controle.estoque.modelo.dao.VendaDao;
import java.util.List;

public class GerenciarVendasForm extends JPanel {
    private VendaDao vendaDao = new VendaDao(); // Inicializa o DAO de vendas

    public GerenciarVendasForm() {
        setLayout(null);

        // Componentes para gerenciar vendas
        JLabel label = new JLabel("Gerenciar Vendas");
        JButton botaoAdicionar = new JButton("Adicionar Venda");
        JButton botaoRemover = new JButton("Remover Venda");
        JButton botaoBuscarId = new JButton("Buscar por ID");
        JButton botaoBuscarNome = new JButton("Buscar por Nome Cliente");
        JButton botaoListarVendas = new JButton("Listar Vendas");

        // Posições dos botao
        label.setBounds(30, 30, 200, 25);
        botaoAdicionar.setBounds(30, 70, 150, 25);
        botaoRemover.setBounds(30, 110, 150, 25);
        botaoBuscarId.setBounds(30, 150, 150, 25);
        botaoBuscarNome.setBounds(30, 190, 200, 25);
        botaoListarVendas.setBounds(30, 230, 150, 25);

        // Adicionando os botao ao painel
        add(label);
        add(botaoAdicionar);
        add(botaoRemover);
        add(botaoBuscarId);
        add(botaoBuscarNome);
        add(botaoListarVendas);

        //  Adicionar Venda
        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // adicionar venda 
                JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Tela de adicionar venda aberta!");
            }
        });

        //  Remover Venda
        botaoRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  remover venda, poderia exibir um campo de ID
                String idVenda = JOptionPane.showInputDialog("Digite o ID da venda a remover:");
                if (idVenda != null && !idVenda.isEmpty()) {
                    Long id = Long.parseLong(idVenda);
                    String resultado = vendaDao.deletarPeloId(id);
                    JOptionPane.showMessageDialog(GerenciarVendasForm.this, resultado);
                }
            }
        });

        //  Buscar Venda por ID
        botaoBuscarId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  busca por ID
                String idVenda = JOptionPane.showInputDialog("Digite o ID da venda:");
                if (idVenda != null && !idVenda.isEmpty()) {
                    Long id = Long.parseLong(idVenda);
                    Venda venda = vendaDao.buscarVendaPeloId(id);
                    if (venda != null) {
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Venda encontrada: " + venda);
                    } else {
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Venda não encontrada!");
                    }
                }
            }
        });

        //  Buscar Venda por Nome do Cliente
        botaoBuscarNome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // busca por nome de cliente
                String nomeCliente = JOptionPane.showInputDialog("Digite o nome do cliente:");
                if (nomeCliente != null && !nomeCliente.isEmpty()) {
                    List<Venda> vendas = vendaDao.buscarVendasPorNomeCliente(nomeCliente);
                    if (vendas.isEmpty()) {
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Nenhuma venda encontrada para este cliente!");
                    } else {
                        StringBuilder listaVendas = new StringBuilder();
                        for (Venda venda : vendas) {
                            listaVendas.append(venda).append("\n");
                        }
                        JOptionPane.showMessageDialog(GerenciarVendasForm.this, listaVendas.toString());
                    }
                }
            }
        });

        //  Listar todas as Vendas
        botaoListarVendas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  listagem de todas as vendas
                List<Venda> vendas = vendaDao.buscarVendas();
                if (vendas.isEmpty()) {
                    JOptionPane.showMessageDialog(GerenciarVendasForm.this, "Nenhuma venda encontrada.");
                } else {
                    StringBuilder listaVendas = new StringBuilder();
                    for (Venda venda : vendas) {
                        listaVendas.append(venda).append("\n");
                    }
                    JOptionPane.showMessageDialog(GerenciarVendasForm.this, listaVendas.toString());
                }
            }
        });
    }
}
