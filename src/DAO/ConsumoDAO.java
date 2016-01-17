/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Classes.Consumo;
import Classes.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author PedroMazarini
 */
public class ConsumoDAO extends DAO <Consumo> {
    
       public ConsumoDAO() throws SQLException {
    }

    @Override
    public void salvar(Consumo obj) throws SQLException {
        
        PreparedStatement stmt = getConnection().prepareStatement(
                "INSERT INTO "
                + "consumo(idcomanda, idproduto,quantidade,hr_consumo) "
                + "VALUES(?, ?, ?, ?);" );

        String hora = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        
        stmt.setInt(1, obj.getIdcomanda());
        stmt.setInt(2, obj.getIdproduto());  
        stmt.setFloat(3, obj.getQuantidade());
        stmt.setTime(4,Time.valueOf(hora));

        stmt.executeUpdate();
        stmt.close();

    }

    public void remover_consumo(int id) throws SQLException{
        PreparedStatement stmt = getConnection().prepareStatement(
                "DELETE FROM "
                + "consumo "
                + "WHERE idconsumo = ?" );

        stmt.setInt(1,id);

        stmt.executeUpdate();
        stmt.close();
    }

    @Override
       public List<Consumo> listarHoje() throws SQLException {          
             throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.         
    }

    @Override
    public Consumo buscar(int busca) throws SQLException {       
        
        throw new UnsupportedOperationException("Not supported yet.");   
    }

    @Override
    public List<Consumo> listarTodos() throws SQLException {
       throw new UnsupportedOperationException("Not supported yet.");   
    }

    @Override
    public List<Consumo> listarConsumo(int c) throws SQLException {
        List<Consumo> lista = new ArrayList<Consumo>();
         PreparedStatement stmt = getConnection().prepareStatement(
                  " SELECT * " 
                + " FROM "
                + " consumo "
                + " where (idcomanda = ?)"
                  );
        
        stmt.setInt(1,c );
               
        ResultSet rs = stmt.executeQuery();
       
        while ( rs.next() ) { 
            Consumo consumo = new Consumo();
            
            consumo.setIdconsumo(rs.getInt("idconsumo"));
            consumo.setIdcomanda(rs.getInt("idcomanda"));
            consumo.setIdproduto(rs.getInt("idproduto"));
            consumo.setQuantidade(rs.getFloat("quantidade"));
            consumo.setHr_consumo(rs.getTime("hr_consumo"));
            lista.add(consumo);
            
        }

        rs.close();
        stmt.close();

        return (List<Consumo>) lista;
        
    }

    @Override
    public void alterar_total(float total, int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
