package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.AutenticacaoDao;
import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dao.UsuarioDao; // Adicione esta importacao
import com.autoparts.controle.estoque.modelo.dao.VendaDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import com.autoparts.controle.estoque.view.modelo.LoginDTO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);
    private static VendaDao vendaDao = new VendaDao(); // Inicialize sua conexao corretamente
    private static AutenticacaoDao autenticacaoDao = new AutenticacaoDao(); // Instancia do DAO de autenticacao
    private static ClienteDao clienteDao = new ClienteDao(); // Instancia do DAO de clientes
    private static UsuarioDao usuarioDao = new UsuarioDao(); // Inicialize o UsuarioDao aqui
    private static Usuario usuarioAutenticado; // Armazenar o usuario autenticado

    public static void main(String[] args) throws SQLException {
        // Tenta realizar o login antes de mostrar o menu
        usuarioAutenticado = realizarLogin();
        if (usuarioAutenticado == null) {
            System.out.println("Login falhou. Encerrando o programa.");
            return; // Se o login falhar, encerra o programa
        }

        // Exibe o menu principal apos o login bem-sucedido
        exibirMenuPrincipal();
    }

    private static Usuario realizarLogin() {
        System.out.println("===== Tela de Login =====");
        System.out.print("Nome de usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        // Cria uma instancia de LoginDTO com as informacoes fornecidas
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario(usuario);
        loginDTO.setSenha(senha);

        // Usa a instancia do AutenticacaoDao para validar o login
        Usuario usuarioAutenticado = autenticacaoDao.login(loginDTO);

        if (usuarioAutenticado != null) {
            System.out.println("Login realizado com sucesso!");

            // Verifica se o usuario e ADM ou PADRAO
            if (Perfil.ADM.equals(usuarioAutenticado.getPerfil())) {
                System.out.println("Bem-vindo, administrador!");
            } else if (Perfil.PADRAO.equals(usuarioAutenticado.getPerfil())) {
                System.out.println("Bem-vindo, usuario padrao!");
            }

            return usuarioAutenticado; // Retorna o usuario autenticado
        } else {
            System.out.println("Falha no login! Usuario ou senha invalidos.");
            return null; // Retorna null se a autenticacao falhar
        }
    }

    private static void exibirMenuPrincipal() throws SQLException {
        int opcao;
        do {
            System.out.println("===== Menu Principal =====");
            System.out.println("1. Criar Venda");
            System.out.println("2. Listar Vendas");
            System.out.println("3. Buscar Relatorio de Vendas por ID");
            System.out.println("4. Listar Relatorios de Vendas");
            System.out.println("5. Gerenciar Clientes");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    criarVenda();
                    break;
                case 2:
                    listarVendas();
                    break;
                case 3:
                    buscarRelatorioPorId();
                    break;
                case 4:
                    listarRelatorios();
                    break;
                case 5:
                    gerenciarClientes();
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opcao invalida! Tente novamente.");
            }
        } while (opcao != 6);
    }

    private static void criarVenda() throws SQLException {
        System.out.print("Digite o ID do Cliente: ");
        Long clienteId = scanner.nextLong();
        Cliente cliente = clienteDao.buscarClientePeloId(clienteId);
        if (cliente == null) {
            System.out.println("Cliente nao encontrado. Tente novamente.");
            return;
        }

        System.out.print("Digite o ID do Usuario: ");
        Long usuarioId = scanner.nextLong();
        Usuario usuario = usuarioDao.buscarUsuarioPeloId(usuarioId); // Certifique-se de que `usuarioDao` foi inicializado
        if (usuario == null) {
            System.out.println("Usuario nao encontrado. Tente novamente.");
            return;
        }

        System.out.print("Digite o valor total da venda: ");
        BigDecimal totalDaVenda = scanner.nextBigDecimal();

        System.out.print("Digite o desconto: ");
        BigDecimal desconto = scanner.nextBigDecimal();

        System.out.print("Digite o troco: ");
        BigDecimal troco = scanner.nextBigDecimal();

        scanner.nextLine(); // Consumir a quebra de linha
        System.out.print("Digite uma observacao (opcional): ");
        String observacao = scanner.nextLine();

        Venda venda = new Venda(null, cliente, usuario, totalDaVenda, desconto, troco, observacao, LocalDateTime.now());
        String resultado = vendaDao.salvar(venda);
        System.out.println(resultado);
    }

    private static void listarVendas() {
        System.out.println("===== Listagem de Vendas =====");
        List<Venda> vendas = vendaDao.buscarVendas();

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada.");
        } else {
            System.out.printf("%-10s %-30s %-15s %-10s %-10s %-10s%n", "ID", "Cliente", "Usuario", "Total", "Desconto", "Troco");
            System.out.println("---------------------------------------------------------------------------------");
            for (Venda venda : vendas) {
                // Aqui, voce assume que Venda tem metodos para obter o cliente e usuario
                String clienteNome = venda.getCliente().getNome(); // Acesse o nome do cliente
                String usuarioNome = venda.getUsuario().getNome(); // Acesse o nome do usuario

                // Imprime as informacoes de cada venda
                System.out.printf("%-10d %-30s %-15s %-10.2f %-10.2f %-10.2f%n",
                        venda.getId(),
                        clienteNome,
                        usuarioNome,
                        venda.getTotalDaVenda(),
                        venda.getDesconto(),
                        venda.getTroco());
            }
        }
    }

    private static void buscarRelatorioPorId() {
        System.out.print("Digite o ID do Relatorio de Vendas: ");
        Long relatorioId = scanner.nextLong();
        // Logica para buscar o relatorio de vendas pelo ID
    }

    private static void listarRelatorios() {
        System.out.println("===== Listagem de Relatorios de Vendas =====");
        // Aqui voce pode implementar a listagem de relatorios usando relatorioVendasDao.listarRelatoriosVendas()
    }

    private static void gerenciarClientes() {
        if (Perfil.ADM.equals(usuarioAutenticado.getPerfil())) { // Verifica se o usuario e ADM
            int opcaoCliente;
            do {
                System.out.println("===== Gerenciamento de Clientes =====");
                System.out.println("1. Adicionar Cliente");
                System.out.println("2. Editar Cliente");
                System.out.println("3. Listar Clientes");
                System.out.println("4. Deletar Cliente");
                System.out.println("5. Voltar");
                System.out.print("Escolha uma opcao: ");
                opcaoCliente = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                switch (opcaoCliente) {
                    case 1:
                        adicionarCliente();
                        break;
                    case 2:
                        editarCliente();
                        break;
                    case 3:
                        listarClientes();
                        break;
                    case 4:
                        deletarCliente();
                        break;
                    case 5:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opcao invalida! Tente novamente.");
                }
            } while (opcaoCliente != 5);
        } else {
            System.out.println("Acesso negado! Apenas administradores podem gerenciar clientes.");
        }
    }

    private static void adicionarCliente() {
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o telefone do cliente: ");
        String telefone = scanner.nextLine();

        System.out.print("Digite o endereco do cliente: ");
        String endereco = scanner.nextLine();

        // objeto Cliente e salve no banco de dados
        Cliente cliente = new Cliente(null, nome, telefone, endereco);
        String resultado = clienteDao.salvar(cliente);
        System.out.println(resultado);
    }

    private static void editarCliente() {
        System.out.print("Digite o ID do cliente a ser editado: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consumir a quebra de linha

        Cliente clienteExistente = clienteDao.buscarClientePeloId(id);
        if (clienteExistente == null) {
            System.out.println("Cliente nao encontrado. Tente novamente.");
            return; // Se o cliente não for encontrado, encerra o método
        }

        // Exibe os detalhes atuais do cliente
        System.out.println("Detalhes do Cliente:");
        System.out.printf("ID: %d%nNome: %s%nTelefone: %s%nEndereco: %s%n",
                clienteExistente.getId(),
                clienteExistente.getNome(),
                clienteExistente.getTelefone(),
                clienteExistente.getEndereco());

        // Solicita novas informações
        System.out.print("Digite o novo nome do cliente (ou pressione Enter para manter o mesmo): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            clienteExistente.setNome(novoNome); // Atualiza o nome se fornecido
        }

        System.out.print("Digite o novo telefone do cliente (ou pressione Enter para manter o mesmo): ");
        String novoTelefone = scanner.nextLine();
        if (!novoTelefone.isEmpty()) {
            clienteExistente.setTelefone(novoTelefone); // Atualiza o telefone se fornecido
        }

        System.out.print("Digite o novo endereco do cliente (ou pressione Enter para manter o mesmo): ");
        String novoEndereco = scanner.nextLine();
        if (!novoEndereco.isEmpty()) {
            clienteExistente.setEndereco(novoEndereco); // Atualiza o endereco se fornecido
        }

        // Atualiza o cliente no banco de dados
        String resultado = clienteDao.editar(clienteExistente); // Método que deve existir no ClienteDao para atualização
        System.out.println(resultado);
    }

    private static void listarClientes() {
        System.out.println("===== Listagem de Clientes =====");
        List<Cliente> clientes = clienteDao.buscarClientes(); // Busca a lista de clientes

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
        } else {
            System.out.printf("%-10s %-30s %-15s %-30s%n", "ID", "Nome", "Telefone", "Endereco"); // Cabecalho da tabela
            System.out.println("---------------------------------------------------------------------------------");
            for (Cliente cliente : clientes) {
                // Imprime as informacoes de cada cliente
                System.out.printf("%-10d %-30s %-15s %-30s%n", cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getEndereco());
            }
        }
    }

    private static void deletarCliente() {
        System.out.print("Digite o ID do cliente a ser deletado: ");
        Long id = scanner.nextLong();
        String resultado = clienteDao.deletarPeloId(id);
        System.out.println(resultado);
    }
}
