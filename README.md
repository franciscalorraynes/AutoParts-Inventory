# AutoParts Inventory

---

#### Sistema de Gerenciamento de Controle de Estoque de Peças Automotivas

###### Desenvolvido por: [Francisca Lorrayne](https://github.com/franciscalorraynes) e [Samira França](https://github.com/samirafq)

<div>
  <img src="https://github.com/franciscalorraynes/AutoParts-Inventory/blob/main/controle-estoque/src/main/java/com/autoparts/controle/estoque/view/modelo/image/Captura%20de%20tela%202024-10-09%20090206.png?raw=true" width="700" height="250">
</div>
---

**imagem provisória** 

AutoParts é um sistema de gerenciamento de estoque de peças automotivas, desenvolvido em Java utilizando conceitos de Programação Orientada a Objetos (POO) e Swing para a interface gráfica, seguindo a arquitetura MVC (Model-View-Controller).

## Funcionalidades Principais

- **Gerenciamento de peças**: Permite o cadastro, consulta, atualização e remoção de peças no sistema.
  - ![Gerenciamento de Peças](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\pecas.png)
- **Gerenciamento de vendas**: Controla a venda de peças, registrando as transações realizadas.
  - ![Gerenciamento de Vendas](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\Gerenciar_vendas.png)
- **Ordem de serviço**: Gerencia ordens de serviço associadas às vendas e manutenções.
  - ![Ordem de Serviço](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\OS)
- **Gerenciamento de clientes**: Permite o cadastro e consulta de informações dos clientes.
  - ![Gerenciamento de Clientes](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\clientes.png)
- **Cadastro de usuários**: Facilita o registro e controle de usuários do sistema.
  - ![Cadastro de Usuários](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\usuarios.png)
- **Listar estoque**: Mostra as peças disponíveis no estoque.
  - ![Listar Estoque](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\estoque.png)
- **Relatórios de ordens de serviço e vendas**: Gera relatórios detalhados sobre as atividades realizadas.
  - ![Relatórios](C:\Users\Lorrayne\Documents\BACKUP-27-09-2024\fllsa\02\OneDrive\Documentos\NetBeansProjects\AutoParts-Inventory\Arquivos sobressalentes\fotos de perfil\relatorios.png)

## Requisitos de Sistema

- Java JDK 11 ou superior
- MySQL
- IDE recomendada: NetBeans ou IntelliJ IDEA

## Instalação

1. Clone o repositório do GitHub:

    ```bash
    git clone https://github.com/franciscalorraynes/AutoParts-Inventory.git
    ```

2. Navegue até o diretório do projeto:

    ```bash
    cd AutoParts-Inventory
    ```

3. Importe o projeto na sua IDE de preferência (NetBeans ou IntelliJ IDEA).

4. Certifique-se de que o MySQL esteja instalado e configurado em sua máquina.

5. Crie o banco de dados e as tabelas necessárias conforme o script SQL fornecido no repositório.

6. Configure as credenciais de conexão com o banco de dados MySQL no arquivo de configuração do projeto.

7. Compile e execute o projeto diretamente pela IDE.

## Estrutura do Projeto

- **Interface Gráfica:** Desenvolvida em Swing para fornecer uma experiência interativa ao usuário.
- **Lógica de Negócios:** Implementada em Java utilizando conceitos de POO e seguindo o padrão MVC para gerenciar as operações do sistema.
- **Armazenamento de Dados:** Utiliza MySQL para armazenar informações sobre as peças automotivas e suas movimentações de estoque.

## Contribuição

Contribuições são bem-vindas! Se você quiser contribuir para este projeto, por favor, abra uma issue para discutir as mudanças propostas ou envie um pull request.

## Equipe

<table align="center">
  <tr>    
    <td align="center">
      <a href="https://github.com/franciscalorraynes">
        <img src="https://avatars.githubusercontent.com/u/104534319?v=4" 
        width="120px;" alt="Francisca Lorrayne"/><br>
        <sub>
          <b>Francisca Lorrayne</b>
         </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/samirafq">
        <img src="https://avatars.githubusercontent.com/u/111064435?v=4" 
        width="120px;" alt="Samira França"/><br>
        <sub>
          <b>Samira França</b>
         </sub>
      </a>
    </td>
  </tr>
</table>

<p align="center">
Cada contribuidor desempenhou um papel essencial no desenvolvimento e aprimoramento deste projeto.
</p>

## Licença

Este projeto está licenciado sob a [Licença MIT](https://github.com/franciscalorraynes/AutoParts_Inventory/blob/main/LICENSE). Consulte o arquivo LICENSE para obter mais detalhes.

## Agradecimentos

Agradecemos aos desenvolvedores e à comunidade de código aberto que contribuíram com ferramentas como Java, MySQL e outras bibliotecas que tornaram possível o desenvolvimento deste sistema de controle de estoque de peças automotivas.
