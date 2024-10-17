-- Criar o banco de dados
CREATE DATABASE controle_estoque;

-- Usar o banco de dados
USE controle_estoque;

-- Tabela de Usuários
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(75) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE, -- Garantir logins únicos
    senha VARCHAR(100) NOT NULL, 
    telefone VARCHAR(20), 
    perfil ENUM('ADM', 'PADRAO') DEFAULT 'PADRAO',
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_login DATETIME DEFAULT '0001-01-01 00:00:00'
);



-- Tabela de Clientes
CREATE TABLE cliente (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    endereco TEXT
);

-- Tabela de Fornecedores
CREATE TABLE fornecedor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
	cpf VARCHAR(14),
    telefone VARCHAR(20),
    endereco TEXT
    );

-- Tabela de Peças
CREATE TABLE pecas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    quantidade INT NOT NULL, 
    preco DECIMAL(10,2) NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_fornecedor BIGINT,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id)
);

CREATE TABLE venda (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_cliente BIGINT NOT NULL,
    data_venda DATETIME DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL,
    desconto DECIMAL(10,2) DEFAULT 0.00,  -- Coluna para desconto, valor padrão 0.00
    troco DECIMAL(10,2) DEFAULT 0.00,     -- Coluna para troco, valor padrão 0.00
    id_usuario BIGINT NOT NULL,
    observacoes TEXT,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);


-- Tabela de Itens de Venda
CREATE TABLE item_venda (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_venda BIGINT NOT NULL,
    id_peca BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES venda(id),
    FOREIGN KEY (id_peca) REFERENCES pecas(id)
);

-- Tabela de Ordem de Serviço
CREATE TABLE ordemServico (
    idOs BIGINT PRIMARY KEY AUTO_INCREMENT, 
    data_os TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data e hora automática
    equipamento VARCHAR(150) NOT NULL, 
    defeito VARCHAR(150) NOT NULL,
    servicoPrestado VARCHAR(150),
    funcionarioResponsavel VARCHAR(30),
    valor DECIMAL(10,2),
    idCliente BIGINT NOT NULL, -- Chave estrangeira para clientes
    FOREIGN KEY (idCliente) REFERENCES cliente(id)
);
-- Tabela de Relatório de Vendas
CREATE TABLE relatorioVendas (
    idRelatorio BIGINT PRIMARY KEY AUTO_INCREMENT, -- Chave primária para o relatório
    idVenda BIGINT NOT NULL, -- Chave estrangeira para a venda
    idCliente BIGINT NOT NULL, -- Chave estrangeira para o cliente
    idPeca BIGINT NOT NULL, -- Chave estrangeira para a peça vendida
    quantidade INT NOT NULL, -- Quantidade de peças vendidas
    valorTotal DECIMAL(10,2) NOT NULL, -- Valor total da venda
    dataVenda DATETIME DEFAULT CURRENT_TIMESTAMP, -- Data e hora da venda
    FOREIGN KEY (idVenda) REFERENCES venda(id), -- Referência para a tabela de vendas
    FOREIGN KEY (idCliente) REFERENCES cliente(id), -- Referência para o cliente
    FOREIGN KEY (idPeca) REFERENCES pecas(id) -- Referência para a peça
);
