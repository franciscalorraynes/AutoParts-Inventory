package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GerenciarClientesForm extends JPanel {

    private Cliente clienteEditando = null; 

    private DefaultTableModel tabelaModelo;
    private JButton botaoAdicionar;
    private JButton botaoSalvarAlteracoes; // Novo botão para salvar alterações
    private boolean editandoCliente = false; // Flag para controle de edição

    public GerenciarClientesForm() {
        setLayout(null);

        JLabel label = new JLabel("Gerenciar Clientes");
        botaoAdicionar = new JButton("Adicionar Cliente");
        botaoSalvarAlteracoes = new JButton("Salvar Alterações"); // Novo botão
        JButton botaoRemover = new JButton("Remover Cliente");
        JButton botaoEditar = new JButton("Editar Cliente");
        JButton botaoListar = new JButton("Listar Clientes");

        JTextField campoNome = new JTextField(20);
        JTextField campoEndereco = new JTextField(20);
        JTextField campoTelefone = new JTextField(20);

        JLabel labelNome = new JLabel("Nome: ");
        JLabel labelEndereco = new JLabel("Endereço: ");
        JLabel labelTelefone = new JLabel("Telefone: ");

        label.setBounds(30, 30, 200, 25);
        campoNome.setBounds(100, 60, 150, 25);
        labelNome.setBounds(30, 60, 70, 25);
        campoEndereco.setBounds(100, 100, 150, 25);
        labelEndereco.setBounds(30, 100, 70, 25);
        campoTelefone.setBounds(100, 140, 150, 25);
        labelTelefone.setBounds(30, 140, 70, 25);

        botaoAdicionar.setBounds(30, 180, 150, 25);
        botaoSalvarAlteracoes.setBounds(30, 220, 150, 25); // Novo botão
        botaoRemover.setBounds(30, 260, 150, 25);
        botaoEditar.setBounds(30, 300, 150, 25);
        botaoListar.setBounds(30, 340, 150, 25);

        String[] colunas = {"ID", "Nome", "Endereço", "Telefone"};
        tabelaModelo = new DefaultTableModel(null, colunas);
        JTable tabelaClientes = new JTable(tabelaModelo);
        tabelaClientes.setBounds(300, 60, 500, 250);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        add(scrollPane);
        scrollPane.setBounds(300, 60, 500, 250);

        add(label);
        add(campoNome);
        add(labelNome);
        add(campoEndereco);
        add(labelEndereco);
        add(campoTelefone);
        add(labelTelefone);
        add(botaoAdicionar);
        add(botaoSalvarAlteracoes);
        add(botaoRemover);
        add(botaoEditar);
        add(botaoListar);

        // Lógica para adicionar cliente
        botaoAdicionar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String endereco = campoEndereco.getText().trim();
            String telefone = campoTelefone.getText().trim();

            if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setEndereco(endereco);
            cliente.setTelefone(telefone);

            ClienteDao clienteDao = new ClienteDao();
            String mensagem = clienteDao.salvar(cliente);
            JOptionPane.showMessageDialog(this, mensagem);

            // Limpar campos após salvar
            campoNome.setText("");
            campoEndereco.setText("");
            campoTelefone.setText("");
        });

        //  alterações do cliente
        botaoSalvarAlteracoes.addActionListener(e -> {
            if (editandoCliente) {
                String nome = campoNome.getText().trim();
                String endereco = campoEndereco.getText().trim();
                String telefone = campoTelefone.getText().trim();

                if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                    return;
                }

                Cliente cliente = new Cliente();
                cliente.setId(cliente.getId()); // Mantém o ID original
                cliente.setNome(nome);
                cliente.setEndereco(endereco);
                cliente.setTelefone(telefone);

                ClienteDao clienteDao = new ClienteDao();
                String mensagem = clienteDao.salvar(cliente); // Salva as alterações
                JOptionPane.showMessageDialog(this, mensagem);

                // Resetar estado após salvar
                editandoCliente = false;
                botaoSalvarAlteracoes.setEnabled(false); // Desabilita o botão de salvar após salvar
                botaoAdicionar.setEnabled(true); // Reabilita o botão de adicionar

                // Limpar campos após salvar
                campoNome.setText("");
                campoEndereco.setText("");
                campoTelefone.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum cliente está sendo editado.");
            }
        });

        //remover cliente
        botaoRemover.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Digite o nome do cliente a ser removido:");
            if (nome != null && !nome.isEmpty()) {
                ClienteDao clienteDao = new ClienteDao();
                String mensagem = clienteDao.deletarPorNome(nome);
                JOptionPane.showMessageDialog(this, mensagem);
            } else {
                JOptionPane.showMessageDialog(this, "Nome do cliente não pode ser vazio.");
            }
        });

      
       
        // editar cliente
        botaoEditar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Digite o nome ou ID do cliente a ser editado:");
            Cliente cliente = null;  
            if (input != null && !input.isEmpty()) {
                try {
                    long id = Long.parseLong(input);  // Tenta buscar por ID
                    cliente = buscarClientePorId(id);
                } catch (NumberFormatException ex) {
                    cliente = buscarClientePorNome(input); // Caso não seja número, busca por nome
                }

                if (cliente != null) {
                    // Preencher campos com os dados do cliente
                    campoNome.setText(cliente.getNome());
                    campoEndereco.setText(cliente.getEndereco());
                    campoTelefone.setText(cliente.getTelefone());

                    // Guardar referência ao cliente que está sendo editado
                    clienteEditando = cliente;

                    editandoCliente = true;
                    botaoSalvarAlteracoes.setEnabled(true);
                    botaoAdicionar.setEnabled(false); // Desabilita adicionar enquanto edita
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nome ou ID não pode ser vazio.");
            }
        });

        // salvar alterações do cliente
        botaoSalvarAlteracoes.addActionListener(e -> {
            if (editandoCliente && clienteEditando != null) {
                String nome = campoNome.getText().trim();
                String endereco = campoEndereco.getText().trim();
                String telefone = campoTelefone.getText().trim();

                if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                    return;
                }

                // Atualizar o cliente em edição
                clienteEditando.setNome(nome);
                clienteEditando.setEndereco(endereco);
                clienteEditando.setTelefone(telefone);

                ClienteDao clienteDao = new ClienteDao();
                String mensagem = clienteDao.salvar(clienteEditando); // Salva as alterações
                JOptionPane.showMessageDialog(this, mensagem);

                // Resetar estado após salvar
                editandoCliente = false;
                clienteEditando = null; // Limpar cliente em edição
                botaoSalvarAlteracoes.setEnabled(false); // Desabilita o botão de salvar após salvar
                botaoAdicionar.setEnabled(true); // Reabilita o botão de adicionar

                // Limpar campos após salvar
                campoNome.setText("");
                campoEndereco.setText("");
                campoTelefone.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum cliente está sendo editado.");
            }
        });

        botaoListar.addActionListener(e -> {
            ClienteDao clienteDao = new ClienteDao();
            var listaClientes = clienteDao.buscarClientes();

            if (listaClientes != null && !listaClientes.isEmpty()) {
                tabelaModelo.setRowCount(0);

                for (Cliente clienteDaLista : listaClientes) {  
                    Object[] linha = {clienteDaLista.getId(), clienteDaLista.getNome(), clienteDaLista.getEndereco(), clienteDaLista.getTelefone()};
                    tabelaModelo.addRow(linha);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado.");
            }
        });

        // Validação do campo telefone para aceitar apenas números
        campoTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Impede a digitação de caracteres não numéricos
                }
            }
        });
    }

    //  buscar cliente por ID
    private Cliente buscarClientePorId(Long id) {
        ClienteDao clienteDao = new ClienteDao();
        return clienteDao.buscarClientePeloId(id);
    }

    //  buscar cliente por Nome
    private Cliente buscarClientePorNome(String nome) {
        ClienteDao clienteDao = new ClienteDao();
        return clienteDao.buscarClientePeloNome(nome);
    }
}
