/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Classes.Comanda;
import Classes.Livro;
import static JDBC.ConnectionFactory.getConnection;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;

/**
 *
 * @author PedroMazarini
 */
public class ComandaDAO  extends DAO <Comanda> {
    
       public ComandaDAO() throws SQLException {
    }

    @Override
    public void salvar(Comanda obj) throws SQLException {
        
        PreparedStatement stmt = getConnection().prepareStatement(
                "INSERT INTO "
                + "comandas(nome, chegada, total, bar_code) "
                + "VALUES(?, ?, ?, ?)" );

        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date data = Calendar.getInstance().getTime();
        String dia = sdf.format(data);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, dia);  
        stmt.setFloat(3, obj.getTotal());
        stmt.setInt(4, obj.getBar_code());
        
        

        stmt.executeUpdate();
        stmt.close();

    }

    

    @Override
       public List<Comanda> listarHoje() throws SQLException {
  
       
          
        List<Comanda> lista = new ArrayList<Comanda>();

        PreparedStatement stmt = getConnection().prepareStatement(
                " SELECT *" 
                + " FROM "
                + " comandas "
                + " where date(chegada) = CURDATE()"
                  );       
               

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {

            Comanda c = new Comanda();
             
            c.setId(rs.getInt("idcomanda" ));
            c.setNome(rs.getString( "nome" ));
            c.setChegada(rs.getTimestamp("chegada"));
            c.setTotal(rs.getFloat("total"));
            c.setBar_code(rs.getInt("bar_code"));
            boolean fechada;
            if (rs.getInt("fechada")==0) {
                fechada = false; 
                }else{
                fechada = true;
            }            
            c.setFechada(fechada);
           

            lista.add( c );

        }

        rs.close();
        stmt.close();

        return (List<Comanda>) lista;

    }
       
    public void fechar_comanda(List<Comanda> comandas_aFechar,String pagamento,float desconto) throws SQLException{
        PreparedStatement stmt = null;
        for(Comanda c : comandas_aFechar){
            stmt = getConnection().prepareStatement(
                " UPDATE comandas "
              + " SET "
              + " fechada = 1,"
              + " saida = ?, "
              + "pagamento = ?,"
              + "desconto=?"
              + " WHERE idcomanda=?" );     


            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date data = Calendar.getInstance().getTime();
            String dia = sdf.format(data);

            stmt.setString(1,dia);
            stmt.setString(2, pagamento);
            stmt.setFloat(3, desconto);
            stmt.setInt(4, c.getId());  
           
            stmt.executeUpdate();
        }
        stmt.close();
    }   
    
    @Override   
    public void alterar_total (float total, int id)  throws SQLException{
        
        PreparedStatement stmt = getConnection().prepareStatement(
            " UPDATE comandas "
          + " SET "
          + " total = ? "
          + " WHERE idcomanda=?"
            );
        
        stmt.setFloat(1,total);
        stmt.setInt(2,id);

        stmt.executeUpdate();
        stmt.close();

    }   

    public Comanda buscarID(int busca) throws SQLException {
 
        PreparedStatement stmt = getConnection().prepareStatement(
                    " SELECT *"
                + " FROM "
                + " comandas "
                + " where (idcomanda = ? )"
                  );
        
            stmt.setInt(1,busca);
            
            ResultSet rs = stmt.executeQuery();

            Comanda c = new Comanda();            
            if(rs.next()){
            c.setId(rs.getInt("idcomanda"));
            c.setNome(rs.getString( "nome" ));
            c.setChegada(rs.getTimestamp("chegada"));
            c.setSaida(rs.getTimestamp("saida"));
            c.setTotal(rs.getFloat("total"));
            c.setPagamento(rs.getString("pagamento"));
            c.setBar_code(rs.getInt("bar_code"));
            c.setDesconto(rs.getFloat("desconto"));
            
            }
           

       

        rs.close();
        stmt.close();

        return c;

        
    }
    
    @Override
    public Comanda buscar(int busca) throws SQLException {
 
        PreparedStatement stmt = getConnection().prepareStatement(
                    " SELECT *"
                + " FROM "
                + " comandas "
                + " where (bar_code = ? AND fechada = 0)"
                  );
        
            stmt.setInt(1,busca);
            
            ResultSet rs = stmt.executeQuery();

            Comanda c = new Comanda();            
            if(rs.next()){
            c.setId(rs.getInt("idcomanda"));
            c.setNome(rs.getString( "nome" ));
            c.setChegada(rs.getTimestamp("chegada"));
            c.setTotal(rs.getFloat("total"));
            c.setBar_code(rs.getInt("bar_code"));
            
            }
           

       

        rs.close();
        stmt.close();

        return c;

        
    }

    @Override
    public List<Comanda> listarTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Comanda> listarConsumo(int c) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

  
    
}
