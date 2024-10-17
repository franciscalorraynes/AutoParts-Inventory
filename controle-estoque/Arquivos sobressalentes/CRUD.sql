-- criando um CRUD
-- create 
insert into usuarios(usuario, telefone, login, senha)
values('Administrador', '98132-7173', 'Adm', 'admin');
insert into usuarios(usuario, telefone, login, senha)
values('Funcionario', '9999-9999', 'funcionarios', '980423');
insert into usuarios(usuario, telefone, login, senha)
values('Erro', '9999-9999', 'teste',  '980423');
-- read
select * from usuarios; 
-- update 
update usuarios set telefone = 84981738276 where idUsuario = 2;
-- delete
delete from usuario where id = 1;
use controle_estoque;
insert into cliente(nome, telefone, endereco)
values('José Antonio', '98175-9392', 'Rua Vicente Fontes, Nº96, Centro, Riacho de Santana');
insert into cliente(nome, telefone, endereco)
VALUES('Maria Silva',  '98123-4567', 'Rua Exemplo, Nº123');

select * from cliente;
update cliente set endereco = 'Rua Vicente Fontes, Nº 96, Centro, Riacho de Santana' where id = 1;

ALTER TABLE ordemServico
CHANGE automovel equipamento VARCHAR(150) NOT NULL;

insert into ordemServico ( equipamento, defeito, servicoPrestado, funcionarioResponsavel, valor, idCliente)
values('Carro', 'Motor quebrou', 'Troca de peça defeituosa', 'Wesley', '150.99', 1);
select * from ordemServico;
 
 -- Inserindo dados na tabela fornecedor
INSERT INTO fornecedor (nome, cpf, telefone, endereco) VALUES 
('Anto Miguel', '123.456.789-00', '99999-0000', 'Rua dos Fornecedores, 1'),
('Luzia Silva', '987.654.321-00', '88888-1111', 'Avenida do Fornecedor, 2');

-- Inserindo dados na tabela pecas
INSERT INTO pecas (nome, descricao, quantidade, preco, id_fornecedor) VALUES 
('Parafuso', 'Kit 400 parafusos', 2, 35.99, 1), 
('Motor', 'Descrição do Motor', 2, 200.35, 2);
select * from pecas;

-- Inserindo dados na tabela venda
INSERT INTO venda (id_cliente, valor_total, id_usuario) VALUES 
(1, 100.00, 1), 
(2, 71.99, 1);
update venda set valor_total = 200 where id = 2;

select * from usuario;
DESCRIBE pecas;


-- Inserindo dados na tabela relatorio_vendas
INSERT INTO relatorioVendas (idVenda, idCliente, idPeca, quantidade, valorTotal) VALUES 
(1, 1, 1, 2, 79.99), 
(2, 2, 2, 4, 350.00);



