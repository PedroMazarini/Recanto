/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Classes.Comanda;
import Classes.MovimentoCaixa;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author PedroMazarini
 */
public class MovimentoDAO extends DAO <MovimentoCaixa> {
    
       public MovimentoDAO() throws SQLException {
    }

    @Override
    public void salvar(MovimentoCaixa obj) throws SQLException {
        
        PreparedStatement stmt = getConnection().prepareStatement(
                "INSERT INTO "
                + "movimento_caixa(descricao, cod_atendente, valor, data, id_comanda) "
                + "VALUES(?, ?, ?, ?,?)" );

        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date data = Calendar.getInstance().getTime();
        String dia = sdf.format(data);
        stmt.setString(1, obj.getDescricao());
        stmt.setString(2, obj.getCod_atendente());  
        stmt.setFloat(3, obj.getValor());
        stmt.setString(4, dia);
        stmt.setInt(5,obj.getId_comanda());
        
        

        stmt.executeUpdate();
        stmt.close();

    }

       public List<MovimentoCaixa> listar(Date data) throws SQLException {
           
        List<MovimentoCaixa> lista = new ArrayList<MovimentoCaixa>();

        PreparedStatement stmt = getConnection().prepareStatement(
                " SELECT *" 
                + " FROM "
                + " movimento_caixa "
                + " where date(data) = ?"
                  );       
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String dia = sdf.format(data);
        stmt.setString(1, dia);
               

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {

            MovimentoCaixa mov = new MovimentoCaixa();
            
             
            mov.setIdmovimento_caixa(rs.getInt("idmovimento_caixa" ));
            mov.setDescricao(rs.getString("descricao"));
            mov.setCod_atendente(rs.getString("cod_atendente"));
            mov.setId_comanda(rs.getInt("id_comanda"));
            mov.setData(rs.getTimestamp("data"));
            mov.setValor(rs.getFloat("valor"));
            
            lista.add( mov );

        }

        rs.close();
        stmt.close();

        return (List<MovimentoCaixa>) lista;

    }
       
    
    @Override   
    public void alterar_total (float total, int id)  throws SQLException{
        

    }   

    
    @Override
    public Comanda buscar(int busca) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MovimentoCaixa> listarTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MovimentoCaixa> listarConsumo(int c) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MovimentoCaixa> listarHoje() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

  
    
}
