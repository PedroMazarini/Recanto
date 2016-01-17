/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author PedroMazarini
 */
public class Comanda {
    
    int id;
    String nome;
    Timestamp chegada;
    Timestamp saida;
    boolean fechada;
    float total;
    String pagamento;
    int bar_code;
    float desconto;

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

    public int getBar_code() {
        return bar_code;
    }

    public void setBar_code(int bar_code) {
        this.bar_code = bar_code;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Timestamp getChegada() {
        return chegada;
    }

    public void setChegada(Timestamp chegada) {
        this.chegada = chegada;
    }

    public Timestamp getSaida() {
        return saida;
    }

    public void setSaida(Timestamp saida) {
        this.saida = saida;
    }

    public boolean isFechada() {
        return fechada;
    }

    public void setFechada(boolean fechada) {
        this.fechada = fechada;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }
    
 
    
}
