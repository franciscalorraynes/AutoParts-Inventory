/*package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.AutenticacaoDao;
import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dao.PecasDao;
import com.autoparts.controle.estoque.modelo.dao.UsuarioDao;
import com.autoparts.controle.estoque.modelo.dao.VendaDao;
import com.autoparts.controle.estoque.modelo.dominio.Cliente;
import com.autoparts.controle.estoque.modelo.dominio.ItemVenda;
import com.autoparts.controle.estoque.modelo.dominio.Perfil;
import com.autoparts.controle.estoque.modelo.dominio.Pecas;
import com.autoparts.controle.estoque.modelo.dominio.Usuario;
import com.autoparts.controle.estoque.modelo.dominio.Venda;
import com.autoparts.controle.estoque.view.modelo.LoginDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);
    private static VendaDao vendaDao = new VendaDao(); // DAO de Venda
    private static PecasDao pecasDao = new PecasDao(); // DAO de Peças
    private static AutenticacaoDao autenticacaoDao = new AutenticacaoDao(); // DAO de autenticação
    private static ClienteDao clienteDao = new ClienteDao(); // DAO de clientes
    private static UsuarioDao usuarioDao = new UsuarioDao(); // DAO de usuários
    private static Usuario usuarioAutenticado; // Armazenar o usuário autenticado

    public static void main(String[] args) {
        try {
            // Realiza o login antes de mostrar o menu
            usuarioAutenticado = realizarLogin();
            if (usuarioAutenticado == null) {
                System.out.println("Login falhou. Encerrando o programa.");
                return;
            }

            // Exibe o menu principal após o login
            exibirMenuPrincipal();
        } catch (SQLException e) {
            System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    private static Usuario realizarLogin() {
        System.out.println("===== Tela de Login =====");
        System.out.print("Nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        // Cria um LoginDTO com as informações fornecidas
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario(usuario);
        loginDTO.setSenha(senha);

        // Usa o DAO de autenticação para validar o login
        Usuario usuarioAutenticado = autenticacaoDao.login(loginDTO);

        if (usuarioAutenticado != null) {
            System.out.println("Login realizado com sucesso!");

            // Verifica o perfil do usuário
            if (Perfil.ADM.equals(usuarioAutenticado.getPerfil())) {
                System.out.println("Bem-vindo, administrador!");
            } else if (Perfil.PADRAO.equals(usuarioAutenticado.getPerfil())) {
                System.out.println("Bem-vindo, usuário padrão!");
            }

            return usuarioAutenticado;
        } else {
            System.out.println("Falha no login! Usuário ou senha inválidos.");
            return null;
        }
    }

    private static void exibirMenuPrincipal() throws SQLException {
        int opcao;
        do {
            System.out.println("===== Menu Principal =====");
            System.out.println("1. Criar Venda");
            System.out.println("2. Listar Vendas");
            System.out.println("3. Buscar Relatório de Vendas por ID");
            System.out.println("4. Gerenciar Clientes");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            try {
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
                        gerenciarClientes();
                        break;
                    case 5:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine(); // Limpar a entrada inválida
                opcao = 0; // Repetir o loop
            }
        } while (opcao != 5);
    }

    private static void criarVenda() throws SQLException {
        // Solicita informações do cliente
        System.out.print("Digite o ID do Cliente: ");
        Long clienteId = scanner.nextLong();
        Cliente cliente = clienteDao.buscarClientePeloId(clienteId);
        if (cliente == null) {
            System.out.println("Cliente não encontrado. Tente novamente.");
            return;
        }

        // Solicita informações do usuário responsável pela venda
        System.out.print("Digite o ID do Usuário: ");
        Long usuarioId = scanner.nextLong();
        Usuario usuario = usuarioDao.buscarUsuarioPeloId(usuarioId);
        if (usuario == null) {
            System.out.println("Usuário não encontrado. Tente novamente.");
            return;
        }

        // Solicita dados financeiros
        BigDecimal totalDaVenda = solicitarValor("Digite o valor total da venda: ");
        BigDecimal desconto = solicitarValor("Digite o desconto: ");
        BigDecimal troco = solicitarValor("Digite o troco: ");

        scanner.nextLine(); // Consumir quebra de linha
        System.out.print("Digite uma observação (opcional): ");
        String observacao = scanner.nextLine();

        // Criação inicial da venda
        Venda venda = new Venda(null, cliente, usuario, totalDaVenda, desconto, troco, observacao, LocalDateTime.now());

        // Adicionar itens à venda
        List<ItemVenda> itensVenda = new ArrayList<>();
        String adicionarMaisItens;
        do {
            // Solicitar informações do item (peça)
            System.out.print("Digite o ID da peça: ");
            Long pecaId = scanner.nextLong();
            Pecas peca = pecasDao.buscarPecasPeloId(pecaId);
            if (peca == null) {
                System.out.println("Peça não encontrada. Tente novamente.");
                return;
            }

            // Solicitar quantidade e preço unitário
            int quantidade = solicitarQuantidade("Digite a quantidade: ");
            BigDecimal precoUnitario = solicitarValor("Digite o preço unitário: ");

            // Criar o ItemVenda e adicionar à lista de itens
            ItemVenda itemVenda = new ItemVenda(null, peca, venda, quantidade, precoUnitario);
            itensVenda.add(itemVenda);

            // Perguntar se deseja adicionar mais itens
            System.out.print("Deseja adicionar mais itens? (s/n): ");
            adicionarMaisItens = scanner.next();
        } while (adicionarMaisItens.equalsIgnoreCase("s"));

        // Associa a lista de itens à venda
        venda.setItensVenda(itensVenda);

        // Salvar a venda e seus itens
        String resultado = vendaDao.salvar(venda);
        System.out.println(resultado);
    }

    private static int solicitarQuantidade(String mensagem) {
        int quantidade = 0;
        boolean entradaValida = false;
        while (!entradaValida) {
            System.out.print(mensagem);
            try {
                quantidade = scanner.nextInt();
                if (quantidade > 0) {
                    entradaValida = true;
                } else {
                    System.out.println("Quantidade deve ser maior que zero.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar a entrada inválida
            }
        }
        return quantidade;
    }

    private static BigDecimal solicitarValor(String mensagem) {
        BigDecimal valor = BigDecimal.ZERO;
        boolean entradaValida = false;
        while (!entradaValida) {
            System.out.print(mensagem);
            try {
                valor = scanner.nextBigDecimal();
                if (valor.compareTo(BigDecimal.ZERO) >= 0) {
                    entradaValida = true;
                } else {
                    System.out.println("Valor deve ser maior ou igual a zero.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um valor numérico.");
                scanner.nextLine(); // Limpar a entrada inválida
            }
        }
        return valor;
    }

    private static void listarVendas() {
        System.out.println("===== Listagem de Vendas =====");
        List<Venda> vendas = vendaDao.buscarVendas();

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada.");
        } else {
            System.out.printf("%-10s %-30s %-15s %-10s %-10s %-10s%n", "ID", "Cliente", "Usuário", "Total", "Desconto", "Troco");
            System.out.println("---------------------------------------------------------------------------------");
            for (Venda venda : vendas) {
                String clienteNome = venda.getCliente().getNome();
                String usuarioNome = venda.getUsuario().getNome();
                System.out.printf("%-10d %-30s %-15s %-10s %-10s %-10s%n",
                        venda.getId(), clienteNome, usuarioNome, venda.getTotalDaVenda(), venda.getDesconto(), venda.getTroco());
            }
        }
    }

    private static void buscarRelatorioPorId() {
        System.out.print("Digite o ID da venda que deseja buscar: ");
        Long idVenda = scanner.nextLong();

        Venda venda = vendaDao.buscarVendaPeloId(idVenda);
        if (venda == null) {
            System.out.println("Venda não encontrada.");
            return;
        }

        // Exibir informações da venda
        System.out.println("===== Relatório de Venda =====");
        System.out.printf("ID: %d%n", venda.getId());
        System.out.printf("Cliente: %s%n", venda.getCliente().getNome());
        System.out.printf("Usuário: %s%n", venda.getUsuario().getNome());
        System.out.printf("Total: %s%n", venda.getTotalDaVenda());
        System.out.printf("Desconto: %s%n", venda.getDesconto());
        System.out.printf("Troco: %s%n", venda.getTroco());
        System.out.printf("Data da Venda: %s%n", venda.getDataVenda());
        System.out.printf("Observação: %s%n", venda.getObservacao());

        // Listar itens da venda
        System.out.println("===== Itens da Venda =====");
        for (ItemVenda item : venda.getItensVenda()) {
            System.out.printf("Peça: %s, Quantidade: %d, Preço Unitário: %s%n",
                    item.getPecas().getNome(), item.getQuantidade(), item.getPrecoUnitario());
        }
    }

    private static void gerenciarClientes() throws SQLException {
        int opcao;
        do {
            System.out.println("===== Gerenciamento de Clientes =====");
            System.out.println("1. Adicionar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Editar Cliente");
            System.out.println("4. Deletar Cliente");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                switch (opcao) {
                    case 1:
                        adicionarCliente();
                        break;
                    case 2:
                        listarClientes();
                        break;
                    case 3:
                        editarCliente();
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
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine(); // Limpar a entrada inválida
                opcao = 0; // Repetir o loop
            }
        } while (opcao != 5);
    }

    private static void adicionarCliente() throws SQLException {
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o telefone do cliente: ");
        String telefone = scanner.nextLine();
        System.out.print("Digite o endereco do cliente: ");
        String endereco = scanner.nextLine();

        Cliente cliente = new Cliente(null, nome, telefone, endereco);
        String resultado = clienteDao.adicionar(cliente);
        System.out.println(resultado);
    }

    private static void listarClientes() {
        System.out.println("===== Listagem de Clientes =====");
        List<Cliente> clientes = clienteDao.buscarClientes();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
        } else {
            System.out.printf("%-10s %-30s %-15s%n", "ID", "Nome", "Telefone", "Endereco");
            System.out.println("--------------------------------------------------");
            for (Cliente cliente : clientes) {
                System.out.printf("%-10d %-30s %-15s%n", cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getEndereco());
            }
        }
    }

    private static void editarCliente() throws SQLException {
        System.out.print("Digite o ID do cliente que deseja editar: ");
        Long idCliente = scanner.nextLong();
        Cliente cliente = clienteDao.buscarClientePeloId(idCliente);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.print("Novo nome (atual: " + cliente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        System.out.print("Novo telefone (atual: " + cliente.getTelefone()+ "): ");
        String novoTelefone = scanner.nextLine();

        cliente.setNome(novoNome);
        cliente.setTelefone(novoTelefone);

        String resultado = clienteDao.salvar(cliente);
        System.out.println(resultado);
    }

    private static void deletarCliente() throws SQLException {
        System.out.print("Digite o ID do cliente que deseja deletar: ");
        Long idCliente = scanner.nextLong();
        String resultado = clienteDao.deletarPeloId(idCliente);
        System.out.println(resultado);
    }
}
*/
/*

package com.autoparts.controle.estoque;

import com.autoparts.controle.estoque.modelo.dao.AutenticacaoDao;
import com.autoparts.controle.estoque.modelo.dao.ClienteDao;
import com.autoparts.controle.estoque.modelo.dao.UsuarioDao; 
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
            return; // Se o login falhar, encerra o programafrafffs
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
        // buscar o relatorio de vendas pelo ID
    }

    private static void listarRelatorios() {
        System.out.println("===== Listagem de Relatorios de Vendas =====");
        //implementar 
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
        String resultado = clienteDao.editar(clienteExistente); 
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

*/