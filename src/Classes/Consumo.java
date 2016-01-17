/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Time;

/**
 *
 * @author PedroMazarini
 */
public class Consumo {
    
    int idconsumo;
    int idcomanda;
    int idproduto;
    float quantidade;
    Time hr_consumo;

    public Time getHr_consumo() {
        return hr_consumo;
    }

    public void setHr_consumo(Time hr_consumo) {
        this.hr_consumo = hr_consumo;
    }
    
    

    public int getIdconsumo() {
        return idconsumo;
    }

    public void setIdconsumo(int idconsumo) {
        this.idconsumo = idconsumo;
    }

    public int getIdcomanda() {
        return idcomanda;
    }

    public void setIdcomanda(int idcomanda) {
        this.idcomanda = idcomanda;
    }

    public int getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(int idproduto) {
        this.idproduto = idproduto;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }
    
    
    

    
}
