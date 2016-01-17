/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

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
public class UsuarioDAO extends DAO <Usuario> {
    
       public UsuarioDAO() throws SQLException {
    }

    @Override
    public void salvar(Usuario obj) throws SQLException {
       PreparedStatement stmt = getConnection().prepareStatement(
                "INSERT INTO "
                + "atendente(nome, codigo,gerente) "
                + "VALUES(?, ?, ?);" );

        
        
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getCodigo());  
        stmt.setInt(3, obj.getGerente());

        stmt.executeUpdate();
        stmt.close();
    }
    public void remover_usuario(String codigo) throws SQLException{
        PreparedStatement stmt = getConnection().prepareStatement(
                "DELETE FROM "
                + "atendente "
                + "WHERE codigo = ?" );

        stmt.setString(1,codigo);

        stmt.executeUpdate();
        stmt.close();
    }
    @Override
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<Usuario>();

        PreparedStatement stmt = getConnection().prepareStatement(
                " SELECT * "
                + " FROM "
                + " atendente; "
                  );       
               

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {   
            Usuario u = new Usuario();
            
            u.setNome(rs.getString( "nome" ));
            u.setCodigo(rs.getString("codigo"));  
            u.setGerente(rs.getInt("gerente"));
           

            lista.add( u );

        }

        rs.close();
        stmt.close();

        return (List<Usuario>) lista;
    }

    @Override
    public List<Usuario> listarHoje() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object buscar(int b) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Usuario> listarConsumo(int c) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterar_total(float total, int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
    
}
