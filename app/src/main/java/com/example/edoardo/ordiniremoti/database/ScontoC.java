package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class ScontoC extends SugarRecord<Listino> {
    String codicecliente;
    String sconto;

    public ScontoC() {
    }

    public ScontoC(String codicecliente, String sconto) {
        this.codicecliente = codicecliente;
        this.sconto = sconto;
    }

    public String getCodicecliente() {
        return codicecliente;
    }

    public void setCodicecliente(String codicecliente) {
        this.codicecliente = codicecliente;
    }

    public String getSconto() {
        return sconto;
    }

    public void setSconto(String sconto) {
        this.sconto = sconto;
    }
}
