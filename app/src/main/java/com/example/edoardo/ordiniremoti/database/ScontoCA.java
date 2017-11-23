package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class ScontoCA extends SugarRecord<Listino> {
    private String codicecliente;
    private String codicearticolo;
    private String sconto;

    public ScontoCA(String codicecliente, String codicearticolo, String sconto) {
        this.codicecliente = codicecliente;
        this.codicearticolo = codicearticolo;
        this.sconto = sconto;
    }

    public String getCodicecliente() {
        return codicecliente;
    }

    public void setCodicecliente(String codicecliente) {
        this.codicecliente = codicecliente;
    }

    public String getCodicearticolo() {
        return codicearticolo;
    }

    public void setCodicearticolo(String codicearticolo) {
        this.codicearticolo = codicearticolo;
    }

    public String getSconto() {
        return sconto;
    }

    public void setSconto(String sconto) {
        this.sconto = sconto;
    }

}
