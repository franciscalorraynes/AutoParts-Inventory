/*package com.autoparts.controle.estoque.view.formulario;

import com.autoparts.controle.estoque.modelo.dao.*;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.dominio.Perfil; // Importando o enum Perfil

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    private static VendaDao vendaDao = new VendaDao();
    private static AutenticacaoDao autenticacaoDao = new AutenticacaoDao();
    private static ClienteDao clienteDao = new ClienteDao();
    private static UsuarioDao usuarioDao = new UsuarioDao();
    private static Usuario usuarioAutenticado;

    private JPanel panelPrincipal; // Painel que vai alternar entre as telas
    private CardLayout cardLayout; // Layout para trocar entre as telas

    public MenuPrincipal(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Menu Principal");

        // Menu superior
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opções");
        JMenuItem gerenciarVendas = new JMenuItem("Gerenciar Vendas");
        JMenuItem listarVendas = new JMenuItem("Listar Vendas");
        JMenuItem buscarRelatorio = new JMenuItem("Buscar Relatório de Vendas por ID");
        JMenuItem listarRelatorios = new JMenuItem("Listar Relatórios de Vendas");
        JMenuItem gerenciarClientes = new JMenuItem("Gerenciar Clientes");
        JMenuItem gerenciarPecas = new JMenuItem("Gerenciar Peças");
        JMenuItem sair = new JMenuItem("Sair");

        menu.add(gerenciarVendas);
        menu.add(listarVendas);
        menu.add(buscarRelatorio);
        menu.add(listarRelatorios);
        menu.add(gerenciarClientes);
        menu.add(gerenciarPecas);
        menu.addSeparator();
        menu.add(sair);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Painel principal com CardLayout para alternar entre telas
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.add(new JPanel(), "HOME"); // Tela inicial vazia

        add(panelPrincipal);

        // Ações dos menus
        gerenciarVendas.addActionListener(e -> trocarTela("GERENCIAR_VENDAS"));
        listarVendas.addActionListener(e -> trocarTela("LISTAR_VENDAS"));
        buscarRelatorio.addActionListener(e -> trocarTela("BUSCAR_RELATORIO"));
        listarRelatorios.addActionListener(e -> trocarTela("LISTAR_RELATORIOS"));

        // Gerenciar Clientes - Acesso restrito para Administradores
        gerenciarClientes.addActionListener(e -> {
            if (usuarioAutenticado.getPerfil() == Perfil.ADM) { // Comparando com o enum
                trocarTela("GERENCIAR_CLIENTES");
            } else {
                JOptionPane.showMessageDialog(this, "Acesso negado! Apenas administradores podem gerenciar clientes.");
            }
        });

        // Gerenciar Peças - Acesso permitido para todos
        gerenciarPecas.addActionListener(e -> trocarTela("GERENCIAR_PECAS"));

        // Sair da aplicação
        sair.addActionListener(e -> System.exit(0));
    }

    // Método para trocar as telas no CardLayout
    private void trocarTela(String nomeTela) {
        // Verifica se a tela já foi adicionada
        for (Component component : panelPrincipal.getComponents()) {
            if (component.getName() != null && component.getName().equals(nomeTela)) {
                cardLayout.show(panelPrincipal, nomeTela);
                return;
            }
        }

        // Adiciona a tela conforme o nome
        switch (nomeTela) {
            case "GERENCIAR_VENDAS":
                panelPrincipal.add(new GerenciarVendasForm(), "GERENCIAR_VENDAS");
                break;
            case "BUSCAR_RELATORIO":
                panelPrincipal.add(new BuscarRelatorioForm(), "BUSCAR_RELATORIO");
                break;
            case "LISTAR_RELATORIOS":
                panelPrincipal.add(new ListarRelatoriosForm(), "LISTAR_RELATORIOS");
                break;
            case "GERENCIAR_CLIENTES":
                panelPrincipal.add(new GerenciarClientesForm(), "GERENCIAR_CLIENTES");
                break;
            case "GERENCIAR_PECAS":
                panelPrincipal.add(new GerenciarPecasForm(), "GERENCIAR_PECAS");
                break;
            default:
                break;
        }

        // Exibe a tela desejada
        cardLayout.show(panelPrincipal, nomeTela);
    }
}
*/

/*
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
        setTitle("Menu Principal");

        JPanel panelIcones = new JPanel();
        panelIcones.setLayout(new BoxLayout(panelIcones, BoxLayout.Y_AXIS));

        JButton gerenciarVendas = createIconButton("Gerenciar Vendas", "carrinho-de-compras.png");
        JButton listarVendas = createIconButton("Listar Vendas", "relatorio-de-venda.png");
        JButton buscarRelatorio = createIconButton("Buscar Relatório de Vendas", "relatorio-de-venda.png");
        JButton listarRelatorios = createIconButton("Listar Relatórios", "relatorio-de-venda.png");
        JButton gerenciarClientes = createIconButton("Gerenciar Clientes", "cliente.png");
        JButton gerenciarPecas = createIconButton("Gerenciar Peças", "ferramentas.png");
        JButton sair = createIconButton("Sair", "sair.png");

        panelIcones.add(gerenciarVendas);
        panelIcones.add(listarVendas);
        panelIcones.add(buscarRelatorio);
        panelIcones.add(listarRelatorios);
        panelIcones.add(gerenciarClientes);
        panelIcones.add(gerenciarPecas);
        panelIcones.add(sair);

        add(panelIcones, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.add(new JPanel(), "HOME");

        add(panelPrincipal, BorderLayout.CENTER);

        gerenciarVendas.addActionListener(e -> trocarTela("GERENCIAR_VENDAS"));
        listarVendas.addActionListener(e -> trocarTela("LISTAR_VENDAS"));
        buscarRelatorio.addActionListener(e -> trocarTela("BUSCAR_RELATORIO"));
        listarRelatorios.addActionListener(e -> trocarTela("LISTAR_RELATORIOS"));

        gerenciarClientes.addActionListener(e -> {
            if (usuarioAutenticado.getPerfil() == Perfil.ADM) {
                trocarTela("GERENCIAR_CLIENTES");
            } else {
                JOptionPane.showMessageDialog(this, "Acesso negado! Apenas administradores podem gerenciar clientes.");
            }
        });

        gerenciarPecas.addActionListener(e -> trocarTela("GERENCIAR_PECAS"));
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
        for (Component component : panelPrincipal.getComponents()) {
            if (component.getName() != null && component.getName().equals(nomeTela)) {
                cardLayout.show(panelPrincipal, nomeTela);
                return;
            }
        }

        switch (nomeTela) {
            case "GERENCIAR_VENDAS":
                panelPrincipal.add(new GerenciarVendasForm(), "GERENCIAR_VENDAS");
                break;
            case "BUSCAR_RELATORIO":
                panelPrincipal.add(new BuscarRelatorioForm(), "BUSCAR_RELATORIO");
                break;
            case "LISTAR_RELATORIOS":
                panelPrincipal.add(new ListarRelatoriosForm(), "LISTAR_RELATORIOS");
                break;
            case "GERENCIAR_CLIENTES":
                panelPrincipal.add(new GerenciarClientesForm(), "GERENCIAR_CLIENTES");
                break;
            case "GERENCIAR_PECAS":
                panelPrincipal.add(new GerenciarPecasForm(), "GERENCIAR_PECAS");
                break;
            default:
                break;
        }

        cardLayout.show(panelPrincipal, nomeTela);
    }
}
*/

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
        setTitle("Menu Principal");

        JPanel panelIcones = new JPanel();
        panelIcones.setLayout(new BoxLayout(panelIcones, BoxLayout.Y_AXIS));

        JButton gerenciarVendas = createIconButton("Gerenciar Vendas", "carrinho-de-compras.png");
        JButton listarVendas = createIconButton("Listar Vendas", "relatorio-de-venda.png");
        JButton buscarRelatorio = createIconButton("Buscar Relatório de Vendas", "relatorio-de-venda.png");
        JButton listarRelatorios = createIconButton("Listar Relatórios", "relatorio-de-venda.png");
        JButton gerenciarClientes = createIconButton("Gerenciar Clientes", "cliente.png");
        JButton gerenciarPecas = createIconButton("Gerenciar Peças", "ferramentas.png");
        JButton sair = createIconButton("Sair", "sair.png");

        panelIcones.add(gerenciarVendas);
        panelIcones.add(listarVendas);
        panelIcones.add(buscarRelatorio);
        panelIcones.add(listarRelatorios);
        panelIcones.add(gerenciarClientes);
        panelIcones.add(gerenciarPecas);
        panelIcones.add(sair);

        add(panelIcones, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.add(new JPanel(), "HOME");

        add(panelPrincipal, BorderLayout.CENTER);

        gerenciarVendas.addActionListener(e -> trocarTela("GERENCIAR_VENDAS"));
        listarVendas.addActionListener(e -> trocarTela("LISTAR_VENDAS"));
        buscarRelatorio.addActionListener(e -> trocarTela("BUSCAR_RELATORIO"));
        listarRelatorios.addActionListener(e -> trocarTela("LISTAR_RELATORIOS"));

        gerenciarClientes.addActionListener(e -> {
            if (usuarioAutenticado.getPerfil() == Perfil.ADM) {
                trocarTela("GERENCIAR_CLIENTES");
            } else {
                JOptionPane.showMessageDialog(this, "Acesso negado! Apenas administradores podem gerenciar clientes.");
            }
        });

        gerenciarPecas.addActionListener(e -> trocarTela("GERENCIAR_PECAS"));
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
        for (Component component : panelPrincipal.getComponents()) {
            if (component.getName() != null && component.getName().equals(nomeTela)) {
                cardLayout.show(panelPrincipal, nomeTela);
                return;
            }
        }

        switch (nomeTela) {
            case "GERENCIAR_VENDAS":
                panelPrincipal.add(new GerenciarVendasForm(), "GERENCIAR_VENDAS");
                break;
            case "BUSCAR_RELATORIO":
                panelPrincipal.add(new BuscarRelatorioForm(), "BUSCAR_RELATORIO");
                break;
            case "LISTAR_RELATORIOS":
                panelPrincipal.add(new ListarRelatoriosForm(), "LISTAR_RELATORIOS");
                break;
            case "GERENCIAR_CLIENTES":
                panelPrincipal.add(new GerenciarClientesForm(), "GERENCIAR_CLIENTES");
                break;
            case "GERENCIAR_PECAS":
                panelPrincipal.add(new GerenciarPecasForm(), "GERENCIAR_PECAS");
                break;
            default:
                break;
        }

        cardLayout.show(panelPrincipal, nomeTela);
    }
} 
