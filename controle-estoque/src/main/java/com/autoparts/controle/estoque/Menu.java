package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.AutenticacaoDao;
import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dao.VendaDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
//import com.autoparts.controle.estoque.modelo.dominio.Venda;
import com.autoparts.controle.estoque.view.modelo.LoginDTO;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static  Scanner scanner = new Scanner(System.in);
    private static VendaDao vendaDao = new VendaDao(); // Inicialize sua conexão corretamente
    private static AutenticacaoDao autenticacaoDao = new AutenticacaoDao(); // Instância do DAO de autenticação
    private static ClienteDao clienteDao = new ClienteDao(); // Instância do DAO de clientes
    private static Usuario usuarioAutenticado; // Armazenar o usuário autenticado

    public static void main(String[] args) {
        // Tenta realizar o login antes de mostrar o menu
        usuarioAutenticado = realizarLogin();
        if (usuarioAutenticado == null) {
            System.out.println("Login falhou. Encerrando o programa.");
            return; // Se o login falhar, encerra o programa
        }

        // Exibe o menu principal após o login bem-sucedido
        exibirMenuPrincipal();
    }

    private static Usuario realizarLogin() {
        System.out.println("===== Tela de Login =====");
        System.out.print("Nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        // Cria uma instância de LoginDTO com as informações fornecidas
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario(usuario);
        loginDTO.setSenha(senha);
        
        // Usa a instância do AutenticacaoDao para validar o login
        Usuario usuarioAutenticado = autenticacaoDao.login(loginDTO);
        
        if (usuarioAutenticado != null) {
            System.out.println("Login realizado com sucesso!");
            
            // Verifica se o usuário é ADM ou PADRÃO
            if (Perfil.ADM.equals(usuarioAutenticado.getPerfil())) {
                System.out.println("Bem-vindo, administrador!");
            } else if (Perfil.PADRAO.equals(usuarioAutenticado.getPerfil())) {
                System.out.println("Bem-vindo, usuário padrão!");
            }
            
            return usuarioAutenticado; // Retorna o usuário autenticado
        } else {
            System.out.println("Falha no login! Usuário ou senha inválidos.");
            return null; // Retorna null se a autenticação falhar
        }
    }

    private static void exibirMenuPrincipal() {
        int opcao;
        do {
            System.out.println("===== Menu Principal =====");
            System.out.println("1. Criar Venda");
            System.out.println("2. Listar Vendas");
            System.out.println("3. Buscar Relatório de Vendas por ID");
            System.out.println("4. Listar Relatórios de Vendas");
            System.out.println("5. Gerenciar Clientes");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                   // criarVenda();
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
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 6);
    }
/*
    private static void criarVenda() {
        System.out.print("Digite o ID do Cliente: ");
        Long clienteId = scanner.nextLong();
        // Adicione a lógica para buscar o cliente usando o ID

        System.out.print("Digite o valor total da venda: ");
        BigDecimal totalDaVenda = scanner.nextBigDecimal();

        System.out.print("Digite o desconto: ");
        BigDecimal desconto = scanner.nextBigDecimal();

        System.out.print("Digite o troco: ");
        BigDecimal troco = scanner.nextBigDecimal();

        scanner.nextLine(); // Consumir a quebra de linha
        System.out.print("Digite uma observação (opcional): ");
        String observacao = scanner.nextLine();

        Venda venda = new Venda(null, null, null, totalDaVenda, desconto, troco, observacao, LocalDateTime.now());
        vendaDao.criarVenda(venda);

        System.out.println("Venda criada com sucesso!");
    }
*/
    private static void listarVendas() {
        System.out.println("===== Listagem de Vendas =====");
        // Aqui você pode implementar a listagem de vendas usando vendaDao.listarVendas()
    }

    private static void buscarRelatorioPorId() {
        System.out.print("Digite o ID do Relatório de Vendas: ");
        Long relatorioId = scanner.nextLong();
        // Lógica para buscar o relatório de vendas pelo ID
    }

    private static void listarRelatorios() {
        System.out.println("===== Listagem de Relatórios de Vendas =====");
        // Aqui você pode implementar a listagem de relatórios usando relatorioVendasDao.listarRelatoriosVendas()
    }

    private static void gerenciarClientes() {
        if (Perfil.ADM.equals(usuarioAutenticado.getPerfil())) { // Verifica se o usuário é ADM
            int opcaoCliente;
            do {
                System.out.println("===== Gerenciamento de Clientes =====");
                System.out.println("1. Adicionar Cliente");
                System.out.println("2. Editar Cliente");
                System.out.println("3. Listar Clientes");
                System.out.println("4. Deletar Cliente");
                System.out.println("5. Voltar");
                System.out.print("Escolha uma opção: ");
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
                        System.out.println("Opção inválida! Tente novamente.");
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

        System.out.print("Digite o endereço do cliente: ");
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

        // Cliente clienteExistente = clienteDao.buscarClientePeloId(id);
        // Adicione a lógica de edição aqui
    }

    private static void listarClientes() {
        System.out.println("===== Listagem de Clientes =====");
        List<Cliente> clientes = clienteDao.buscarClientes(); // Busca a lista de clientes

    if (clientes.isEmpty()) {
        System.out.println("Nenhum cliente encontrado.");
    } else {
        System.out.printf("%-10s %-30s %-15s %-30s%n", "ID", "Nome", "Telefone", "Endereço"); // Cabeçalho da tabela
        System.out.println("---------------------------------------------------------------------------------");
        for (Cliente cliente : clientes) {
            // Imprime as informações de cada cliente
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
