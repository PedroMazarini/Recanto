/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import view.Tela_login;

/**
 *
 * @author PedroMazarini
 */
public class Data_transfer {
    
    JFrame frame;
    Comanda comanda;
    String chegada;
    Usuario usuario;
    List<Produto> lista_produtos;
    List<Usuario> lista_usuarios;
    Tela_login tela_login;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    

    
    
    public List<Usuario> getLista_usuarios() {
        return lista_usuarios;
    }

    public void setLista_usuarios(List<Usuario> lista_usuarios) {
        this.lista_usuarios = lista_usuarios;
    }

    
    
    public Tela_login getTela_login() {
        return tela_login;
    }

    public void setTela_login(Tela_login tela_login) {
        this.tela_login = tela_login;
    }

    public List<Produto> getLista_produtos() {
        return lista_produtos;
    }

    public void setLista_produtos(List<Produto> lista_produtos) {
        this.lista_produtos = lista_produtos;
    }

    public String getChegada() {
        return chegada;
    }

    public void setChegada(String chegada) {
        this.chegada = chegada;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    
}
