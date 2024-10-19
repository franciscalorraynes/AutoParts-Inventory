
package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.view.modelo.LoginDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton botaoLogin;
    private JButton botaoCadastrar;
    private JLabel labelMensagem;
    private UsuarioDao usuarioDao;

    public LoginForm() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Inicialização de componentes
        txtUsuario = new JTextField(20);
        txtSenha = new JPasswordField(20);
        botaoLogin = new JButton("Login");
        botaoCadastrar = new JButton("Cadastrar");
        labelMensagem = new JLabel("", SwingConstants.CENTER);
        usuarioDao = new UsuarioDao();

        // Adicionando componentes ao layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Rótulo "Usuário"
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Usuário:"), gbc);

        // Campo de texto para o nome de usuário
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtUsuario, gbc);

        // Rótulo "Senha"
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Senha:"), gbc);

        // Campo de senha
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtSenha, gbc);

        // Painel para os botões "Login" e "Cadastrar"
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));  // Definindo os botões lado a lado
        painelBotoes.add(botaoLogin);
        painelBotoes.add(botaoCadastrar);

        // Adicionando o painel com os botões ao layout
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);

        // Mensagem de erro ou sucesso
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(labelMensagem, gbc);

        // Ação do botão de login
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        // Ação do botão de cadastro
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioCadastroUsuario();
            }
        });

        setVisible(true); // Tornar o JFrame visível
    }

    private void realizarLogin() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario(txtUsuario.getText().trim());
        loginDTO.setSenha(new String(txtSenha.getPassword()).trim());

        if (loginDTO.getUsuario().isEmpty() || loginDTO.getSenha().isEmpty()) {
            labelMensagem.setText("Preencha todos os campos!");
            return;
        }

        new SwingWorker<Usuario, Void>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                return usuarioDao.buscarUsuarioPeloNome(loginDTO.getUsuario());
            }

            @Override
            protected void done() {
                try {
                    Usuario usuario = get();
                    if (usuario != null && usuarioDao.verificarSenha(usuario.getSenha(), loginDTO.getSenha())) {
                        labelMensagem.setText("Login realizado!");
                        dispose();
                        new MenuPrincipal(usuario);
                    } else {
                        labelMensagem.setText("Usuário ou senha inválidos!");
                        JOptionPane.showMessageDialog(LoginForm.this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    labelMensagem.setText("Erro ao acessar o banco de dados!");
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(LoginForm.this, "Erro ao tentar acessar o banco de dados.\nPor favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void abrirFormularioCadastroUsuario() {
        new FormularioCadastroUsuario();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}



