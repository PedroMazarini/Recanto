/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Classes.Caixa;
import Classes.Comanda;
import Classes.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PedroMazarini
 */
public class CaixaDAO extends DAO <Caixa> {
    
       public CaixaDAO() throws SQLException {
    }

    @Override
    public void salvar(Caixa obj) throws SQLException {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void alterar_total (float total)  throws SQLException{
        
        PreparedStatement stmt = getConnection().prepareStatement(
            " UPDATE caixa "
          + " SET "
          + " total = ? "
          + " WHERE idcaixa=1"
            );
        
        stmt.setFloat(1,total);

        stmt.executeUpdate();
        stmt.close();

    }   
    public Object buscar() throws SQLException {
       PreparedStatement stmt = getConnection().prepareStatement(
                  " SELECT *"
                + " FROM "
                + " caixa "
                + " where (idcaixa=1)"
                  );
       
            ResultSet rs = stmt.executeQuery();

            Caixa c = new Caixa();            
            if(rs.next()){
                c.setTotal(rs.getFloat("total"));  
                
            }
           

       

        rs.close();
        stmt.close();

        return c;
    }
    
    @Override
    public List<Caixa> listarTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public List<Caixa> listarHoje() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public List<Caixa> listarConsumo(int c) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar_total(float total, int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object buscar(int b) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
