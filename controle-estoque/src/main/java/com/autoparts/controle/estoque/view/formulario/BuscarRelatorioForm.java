package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.VendaDao; // Certifique-se de importar seu DAO corretamente
import com.autoparts.controle.estoque.modelo.dominio.Venda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class BuscarRelatorioForm extends JFrame {
    private JTextField txtRelatorioId;
    private JButton btnBuscar;
    private JTextArea txtAreaResultado;
    private VendaDao vendaDao;

    public BuscarRelatorioForm() {
        setTitle("Buscar Relatório por ID");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicialização de componentes
        txtRelatorioId = new JTextField();
        btnBuscar = new JButton("Buscar");
        txtAreaResultado = new JTextArea();
        txtAreaResultado.setEditable(false);

        // Adiciona os componentes ao painel
        JPanel panel = new JPanel();
        panel.add(new JLabel("Digite o ID do Relatório: "));
        panel.add(txtRelatorioId);
        panel.add(btnBuscar);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(txtAreaResultado), BorderLayout.CENTER);

        // Inicializa o DAO
        vendaDao = new VendaDao();

        // Ação do botão de buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRelatorio();
            }
        });

        setVisible(true);
    }

    private void buscarRelatorio() {
        try {
            Long relatorioId = Long.parseLong(txtRelatorioId.getText().trim());
            Venda relatorio = vendaDao.buscarVendaPeloId(relatorioId); // Certifique-se de que o método existe
            if (relatorio != null) {
                txtAreaResultado.setText("ID: " + relatorio.getId() +
                        "\nCliente: " + relatorio.getCliente().getNome() +
                        "\nTotal: " + relatorio.getTotalDaVenda() +
                        "\nDesconto: " + relatorio.getDesconto() +
                        "\nTroco: " + relatorio.getTroco() +
                        "\nObservação: " + relatorio.getObservacao());
            } else {
                txtAreaResultado.setText("Relatório não encontrado.");
            }
        } catch (NumberFormatException e) {
            txtAreaResultado.setText("ID inválido. Por favor, digite um número.");
        } catch (Exception e) {
            txtAreaResultado.setText("Erro ao buscar relatório: " + e.getMessage());
        }
    }
}
