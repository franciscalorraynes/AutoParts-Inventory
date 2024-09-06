package com.autoparts.modelo.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySql implements Conexao {
    private final String USUARIO = "root";
    private final String SENHA = "lorralola243";
    private final String URL = "jdbc:mysql://localhost:3306/controle_estoque";
    private Connection conectar;

    @Override
    public Connection obterConexao() throws SQLException {
        if (conectar == null) {
            conectar = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return conectar;

    }

}
