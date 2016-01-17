/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Classes.Comanda;
import Classes.Livro;
import Classes.Produto;
import Classes.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author PedroMazarini
 */
public class ProdutoDAO extends DAO <Produto> {
    
       public ProdutoDAO() throws SQLException {
    }

    @Override
    public void salvar(Produto obj) throws SQLException {
        
        PreparedStatement stmt = getConnection().prepareStatement(
                "INSERT INTO "
                + "produtos(prod_nome, preco,bar_code,porkilo,quantidade) "
                + "VALUES(?, ?, ?,?,?);" );

        
        
        stmt.setString(1, obj.getNome());
        stmt.setFloat(2, obj.getPreco());  
        stmt.setString(3, obj.getBar_code());
        stmt.setInt(4, obj.getPorkilo());
        stmt.setFloat(5, obj.getQuantidade());  

        stmt.executeUpdate();
        stmt.close();

    }

    public void alterarProduto(Produto prod) throws SQLException{
        PreparedStatement stmt = getConnection().prepareStatement(
                " UPDATE produtos "
              + " SET "
              + "prod_nome = ?,"
              + "preco = ?,"
              + "quantidade = ?"
              + " WHERE idprodutos=?" ); 
        
        stmt.setString(1, prod.getNome());
        stmt.setFloat(2, prod.getPreco());
        stmt.setFloat(3, prod.getQuantidade());
        stmt.setInt(4, prod.getId_produto());
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void remover_produto(String codigo) throws SQLException{
        PreparedStatement stmt = getConnection().prepareStatement(
                "DELETE FROM "
                + "produtos "
                + "WHERE bar_code = ?" );

        stmt.setString(1,codigo);

        stmt.executeUpdate();
        stmt.close();
    }
    

    @Override
       public List<Produto> listarHoje() throws SQLException {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Produto buscar(int busca) throws SQLException {         
 
        PreparedStatement stmt = getConnection().prepareStatement(
                  " SELECT * " 
                + " FROM "
                + " produtos "
                + " where (bar_code = ?)"
                  );
        
        stmt.setInt(1,busca );
               
        ResultSet rs = stmt.executeQuery();
       
            Produto p = new Produto();
             
            p.setId_produto(rs.getInt("idprodutos"));
            p.setNome(rs.getString( "prod_nome" ));
            p.setBar_code(rs.getString("bar_code"));
            p.setPreco(rs.getFloat("preco"));
            p.setPorkilo(rs.getInt("porkilo"));
            p.setQuantidade(rs.getFloat("quantidade"));
           

       

        rs.close();
        stmt.close();

        return p;

        
    }

    @Override
    public List<Produto> listarTodos() throws SQLException {
         List<Produto> lista = new ArrayList<Produto>();

        PreparedStatement stmt = getConnection().prepareStatement(
                " SELECT * "
                + " FROM "
                + " produtos; "
                  );       
               

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {   
            Produto p = new Produto();
            
            p.setId_produto((rs.getInt("idprodutos")));
            p.setNome(rs.getString( "prod_nome" ));
            p.setBar_code(rs.getString("bar_code"));
            p.setPreco(rs.getFloat("preco"));
            p.setPorkilo(rs.getInt("porkilo"));
            p.setQuantidade(rs.getFloat("quantidade"));
                     
            lista.add( p );

        }

        rs.close();
        stmt.close();

        return (List<Produto>) lista;
    }

    @Override
    public List<Produto> listarConsumo(int c) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar_total(float total, int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
