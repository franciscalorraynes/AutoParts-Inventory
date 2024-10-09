/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoparts.controle.estoque.modelo.dao;

import com.autoparts.controle.estoque.modelo.conexao.Conexao;
import com.autoparts.controle.estoque.modelo.conexao.ConexaoMySql;
import com.autoparts.controle.estoque.modelo.dominio.Venda;

/**
 *
 * @author Lorrayne
 */
public class VendaDao {
    private Conexao conexao;

    public VendaDao(Conexao conexao) {
        this.conexao = new ConexaoMySql();
    }
    
  /*  public String salvar(Venda venda) {
        //quando for 0 novo cadastro, !0 editar
            return venda.getId() == null || venda.getId() == 0L ? adicionar(venda) : editar(venda);
    }
*/
   /* private String adicionar(Venda venda) {
      
        String sql = "insert into venda (nome, id_cliente, data_venda, valor_total, desconto, troco, id_usuario, observacoes) values (?,?,?,?,?,?,?,?)";
        
        Venda vendaTemp = buscarVendaPeloId(venda.getId());
        
      
    }
*/
    private String editar(Venda venda) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private Venda buscarVendaPeloId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
     
}
