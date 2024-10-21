package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Classe para o formulário de cadastro de usuário.
 */
public class FormularioCadastroUsuario extends JFrame {

    public FormularioCadastroUsuario() {
        abrirFormularioCadastroUsuarioComOpcaoDePerfil();  // Método que configura o formulário
    }

    private void abrirFormularioCadastroUsuarioComOpcaoDePerfil() {
        setTitle("Cadastro de Usuário");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        JLabel labelNome = new JLabel("Nome completo:");
        JLabel labelUsuario = new JLabel("Usuário:");
        JLabel labelSenha = new JLabel("Senha:");
        
        JTextField campoNome = new JTextField(20);  // Campo para o nome completo
        JTextField campoUsuario = new JTextField(20);
        JPasswordField campoSenha = new JPasswordField(20);
        JComboBox<Perfil> comboPerfil = new JComboBox<>(Perfil.values());  // Escolha de perfil
        JButton botaoSalvar = new JButton("Salvar");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos do formulário
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(labelNome, gbc);

        gbc.gridx = 1;
        add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(labelUsuario, gbc);

        gbc.gridx = 1;
        add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelSenha, gbc);

        gbc.gridx = 1;
        add(campoSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Perfil:"), gbc);

        gbc.gridx = 1;
        add(comboPerfil, gbc);  // ComboBox para o perfil

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botaoSalvar, gbc);

        // Exibe o formulário após todos os componentes serem adicionados
        setVisible(true);

        // Ação do botão "Salvar"
        botaoSalvar.addActionListener(e -> {
            String nomeCompleto = campoNome.getText().trim();  // Nome completo do usuário
            String usuarioLogin = campoUsuario.getText().trim();  // Nome de login (username)
            String senha = new String(campoSenha.getPassword()).trim();
            Perfil perfilSelecionado = (Perfil) comboPerfil.getSelectedItem();

            // Verifica se os campos obrigatórios estão preenchidos
            if (nomeCompleto.isEmpty() || usuarioLogin.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            } else {
                // Cria um novo usuário com os dados preenchidos
                Usuario novoUsuario = new Usuario();
                novoUsuario.setNome(nomeCompleto);  // Nome completo do usuário
                novoUsuario.setNomeUsuario(usuarioLogin);  // Nome de login
                novoUsuario.setSenha(senha);  // Senha (será criptografada no DAO)
                novoUsuario.setPerfil(perfilSelecionado);  // Perfil selecionado (ADM ou PADRAO)

                // Salva o usuário no banco de dados
                UsuarioDao usuarioDao = new UsuarioDao();
                String mensagem = usuarioDao.salvar(novoUsuario);
                
                // Exibe mensagem de sucesso ou erro
                JOptionPane.showMessageDialog(this, mensagem);
                
                // Fecha a janela após salvar
                dispose();
            }
        });
    }

    
}
