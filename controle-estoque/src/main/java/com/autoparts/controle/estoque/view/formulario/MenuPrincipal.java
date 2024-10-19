package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.*;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private static VendaDao vendaDao = new VendaDao();
    private static AutenticacaoDao autenticacaoDao = new AutenticacaoDao();
    private static ClienteDao clienteDao = new ClienteDao();
    private static UsuarioDao usuarioDao = new UsuarioDao();
    private static Usuario usuarioAutenticado;

    private JPanel panelPrincipal; 
    private CardLayout cardLayout; 

    public MenuPrincipal(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setVisible(true);
    }

    private void inicializarComponentes() {
         setTitle("AutoParts");

    JPanel panelIcones = new JPanel();
    panelIcones.setLayout(new BoxLayout(panelIcones, BoxLayout.Y_AXIS));
    panelIcones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem externa

    JButton gerenciarVendas = createIconButton("Gerenciar Vendas", "carrinho-de-compras.png");
    JButton gerenciarClientes = createIconButton("Gerenciar Clientes", "cliente.png");
    JButton gerenciarPecas = createIconButton("Gerenciar Peças", "ferramentas.png");
    JButton gerenciarEstoque = createIconButton("Estoque", "estoque.png");
    JButton listarRelatorios = createIconButton("Relatórios", "caneta.png");
    JButton sair = createIconButton("Sair", "sair.png");

    panelIcones.add(gerenciarVendas);
    panelIcones.add(Box.createVerticalStrut(10)); // Espaçamento entre os botões
    panelIcones.add(gerenciarClientes);
    panelIcones.add(Box.createVerticalStrut(10));
    panelIcones.add(gerenciarPecas);
    panelIcones.add(Box.createVerticalStrut(10));
    panelIcones.add(gerenciarEstoque);
    panelIcones.add(Box.createVerticalStrut(10));
    panelIcones.add(listarRelatorios);
    panelIcones.add(Box.createVerticalStrut(20)); // Espaço extra antes do botão de "Sair"
    panelIcones.add(sair);

    add(panelIcones, BorderLayout.WEST);

    cardLayout = new CardLayout();
    panelPrincipal = new JPanel(cardLayout);
    panelPrincipal.setBackground(Color.WHITE); // Fundo branco
    panelPrincipal.add(new JPanel(), "HOME");

    add(panelPrincipal, BorderLayout.CENTER);

    gerenciarVendas.addActionListener(e -> trocarTela("GERENCIAR_VENDAS"));
    listarRelatorios.addActionListener(e -> trocarTela("LISTAR_RELATORIOS"));

    gerenciarClientes.addActionListener(e -> {
        if (usuarioAutenticado.getPerfil() == Perfil.ADM) {
            trocarTela("GERENCIAR_CLIENTES");
        } else {
            JOptionPane.showMessageDialog(this, "Acesso negado! Apenas administradores podem gerenciar clientes.");
        }
    });

    gerenciarPecas.addActionListener(e -> trocarTela("GERENCIAR_PECAS"));
    gerenciarEstoque.addActionListener(e -> trocarTela("GERENCIAR_ESTOQUE"));
    sair.addActionListener(e -> System.exit(0));
    
    }

    private JButton createIconButton(String text, String iconName) {
        String iconPath = "C:/Users/Lorrayne/Documents/BACKUP-27-09-2024/fllsa/02/OneDrive/Documentos/NetBeansProjects/AutoParts-Inventory/controle-estoque/src/main/java/com/autoparts/controle/estoque/view/modelo/image/" + iconName;

        java.io.File file = new java.io.File(iconPath);
        if (file.exists()) {
            ImageIcon iconOriginal = new ImageIcon(iconPath);
            Image image = iconOriginal.getImage();
            Image imageRedimensionada = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon iconRedimensionado = new ImageIcon(imageRedimensionada);

            JButton button = new JButton(text, iconRedimensionado);
            button.setPreferredSize(new Dimension(150, 50));
            button.setIconTextGap(10);
            return button;
        } else {
            System.err.println("Ícone não encontrado: " + iconPath);
            return new JButton(text);
        }
    }

    private void trocarTela(String nomeTela) {
    Component componenteExistente = null;
    
    for (Component component : panelPrincipal.getComponents()) {
        if (component.getName() != null && component.getName().equals(nomeTela)) {
            componenteExistente = component;
            break;
        }
    }
    
    if (componenteExistente == null) {
        switch (nomeTela) {
            case "GERENCIAR_VENDAS":
                GerenciarVendasForm vendasForm = new GerenciarVendasForm();
                vendasForm.setName("GERENCIAR_VENDAS");
                panelPrincipal.add(vendasForm, "GERENCIAR_VENDAS");
                break;
            case "LISTAR_RELATORIOS":
                ListarRelatoriosForm relatoriosForm = new ListarRelatoriosForm();
                relatoriosForm.setName("LISTAR_RELATORIOS");
                panelPrincipal.add(relatoriosForm, "LISTAR_RELATORIOS");
                break;
            case "GERENCIAR_CLIENTES":
                GerenciarClientesForm clientesForm = new GerenciarClientesForm();
                clientesForm.setName("GERENCIAR_CLIENTES");
                panelPrincipal.add(clientesForm, "GERENCIAR_CLIENTES");
                break;
            case "GERENCIAR_PECAS":
                GerenciarPecasForm pecasForm = new GerenciarPecasForm();
                pecasForm.setName("GERENCIAR_PECAS");
                panelPrincipal.add(pecasForm, "GERENCIAR_PECAS");
                break;
            case "GERENCIAR_ESTOQUE":  // Adicione esta parte
                GerenciarEstoqueForm estoqueForm = new GerenciarEstoqueForm();
                estoqueForm.setName("GERENCIAR_ESTOQUE");
                panelPrincipal.add(new GerenciarEstoqueForm(), "GERENCIAR_ESTOQUE");
            break;

            default:
                break;
        }
    }
    
    cardLayout.show(panelPrincipal, nomeTela);
}

} 
