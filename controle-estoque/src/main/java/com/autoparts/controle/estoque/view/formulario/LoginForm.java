/*package com.autoparts.controle.estoque.view.formulario;

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
    private JLabel labelMensagem;
    private UsuarioDao usuarioDao;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());  // Usando GridBagLayout para maior flexibilidade

        // Inicialização de componentes
        txtUsuario = new JTextField(20);
        txtSenha = new JPasswordField(20);
        botaoLogin = new JButton("Login");
        labelMensagem = new JLabel("", SwingConstants.CENTER);

        // Adicionando componentes ao layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Espaçamento entre os componentes

        // Primeira linha (rótulo "Usuário")
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Alinhando à esquerda
        add(new JLabel("Usuário:"), gbc);

        // Segunda linha (campo de texto para o nome de usuário)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o campo de texto ocupar a largura disponível
        add(txtUsuario, gbc);

        // Terceira linha (rótulo "Senha")
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Senha:"), gbc);

        // Quarta linha (campo de senha)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtSenha, gbc);

        // Quinta linha (botão de login)
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE; // Não preencher a célula
        gbc.anchor = GridBagConstraints.CENTER; // Centralizar o botão
        add(botaoLogin, gbc);

        // Sexta linha (mensagem de erro ou sucesso)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // Ocupa as duas colunas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Preenche a largura da célula
        add(labelMensagem, gbc);

        // Inicializa o DAO
        usuarioDao = new UsuarioDao();

        // Ação do botão de login
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        setVisible(true);
    }

    private void realizarLogin() {
        LoginDTO loginDTO = new LoginDTO();

        // Capturando os dados do formulário
        loginDTO.setUsuario(txtUsuario.getText().trim());
        loginDTO.setSenha(new String(txtSenha.getPassword()).trim());

        if (loginDTO.getUsuario().isEmpty() || loginDTO.getSenha().isEmpty()) {
            labelMensagem.setText("Preencha todos os campos!");
            return;
        }

        // Usando SwingWorker para evitar bloquear a UI
        new SwingWorker<Usuario, Void>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                // Simula um pequeno atraso (opcional)
                Thread.sleep(1000); // Remova se não precisar de simulação
                return usuarioDao.buscarUsuarioPeloNome(loginDTO.getUsuario());
            }

            @Override
            protected void done() {
                try {
                    Usuario usuario = get();
                    if (usuario != null && usuarioDao.verificarSenha(usuario.getSenha(), loginDTO.getSenha())) {
                        labelMensagem.setText("Login realizado!");
                        dispose(); // Fecha a janela de login
                        new MenuPrincipal(usuario); // Passa o usuário autenticado
                    } else {
                        labelMensagem.setText("Usuário ou senha inválidos!");
                        // Mostrar uma mensagem de erro mais destacada
                        JOptionPane.showMessageDialog(LoginForm.this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    labelMensagem.setText("Erro ao acessar o banco de dados!");
                    e.printStackTrace(); // Log de erro no console
                    // Exibe uma mensagem de erro detalhada
                    JOptionPane.showMessageDialog(LoginForm.this, "Erro ao tentar acessar o banco de dados.\nPor favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}

*/
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

        // Botão de login
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botaoLogin, gbc);

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
                        dispose(); // Fecha a janela de login
                        new MenuPrincipal(usuario); // Passa o usuário autenticado
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}