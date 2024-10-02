package com.autoparts.modelo.conexao;

import java.sql.Connection;
import java.sql.SQLException;

interface Conexao {
public Connection obterConexao() throws SQLException;
}
