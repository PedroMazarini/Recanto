/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Timestamp;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;

/**
 *
 * @author PedroMazarini
 */
public class MovimentoCaixa {
    
    int idmovimento_caixa;
    String descricao;
    String cod_atendente;
    float valor;
    Timestamp data;
    int id_comanda;

    public int getIdmovimento_caixa() {
        return idmovimento_caixa;
    }

    public void setIdmovimento_caixa(int idmovimento_caixa) {
        this.idmovimento_caixa = idmovimento_caixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCod_atendente() {
        return cod_atendente;
    }

    public void setCod_atendente(String cod_atendente) {
        this.cod_atendente = cod_atendente;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public int getId_comanda() {
        return id_comanda;
    }

    public void setId_comanda(int id_comanda) {
        this.id_comanda = id_comanda;
    }
    
    
    
}
