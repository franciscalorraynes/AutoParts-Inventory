/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.conexao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author fllsa
 */
interface Conexao {
        public Connection obterConexao() throws SQLException;

}
