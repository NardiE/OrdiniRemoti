package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class ListinoCliente extends SugarRecord<ListinoCliente> {
    String codicecliente;
    String codicearticolo;
    String qt1;
    String prezzo1;
    String sconto1;
    String qt2;
    String prezzo2;
    String sconto2;
    String qt3;
    String prezzo3;
    String sconto3;
    String qt4;
    String prezzo4;
    String sconto4;

    public ListinoCliente() {
    }

    public ListinoCliente(String codicecliente, String codicearticolo, String qt1, String prezzo1, String sconto1, String qt2, String prezzo2, String sconto2, String qt3, String prezzo3, String sconto3, String qt4, String prezzo4, String sconto4) {
        this.codicecliente = codicecliente;
        this.codicearticolo = codicearticolo;
        this.qt1 = qt1;
        this.prezzo1 = prezzo1;
        this.sconto1 = sconto1;
        this.qt2 = qt2;
        this.prezzo2 = prezzo2;
        this.sconto2 = sconto2;
        this.qt3 = qt3;
        this.prezzo3 = prezzo3;
        this.sconto3 = sconto3;
        this.qt4 = qt4;
        this.prezzo4 = prezzo4;
        this.sconto4 = sconto4;
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

    public String getQt1() {
        return qt1;
    }

    public void setQt1(String qt1) {
        this.qt1 = qt1;
    }

    public String getPrezzo1() {
        return prezzo1;
    }

    public void setPrezzo1(String prezzo1) {
        this.prezzo1 = prezzo1;
    }

    public String getSconto1() {
        return sconto1;
    }

    public void setSconto1(String sconto1) {
        this.sconto1 = sconto1;
    }

    public String getQt2() {
        return qt2;
    }

    public void setQt2(String qt2) {
        this.qt2 = qt2;
    }

    public String getPrezzo2() {
        return prezzo2;
    }

    public void setPrezzo2(String prezzo2) {
        this.prezzo2 = prezzo2;
    }

    public String getSconto2() {
        return sconto2;
    }

    public void setSconto2(String sconto2) {
        this.sconto2 = sconto2;
    }

    public String getQt3() {
        return qt3;
    }

    public void setQt3(String qt3) {
        this.qt3 = qt3;
    }

    public String getPrezzo3() {
        return prezzo3;
    }

    public void setPrezzo3(String prezzo3) {
        this.prezzo3 = prezzo3;
    }

    public String getSconto3() {
        return sconto3;
    }

    public void setSconto3(String sconto3) {
        this.sconto3 = sconto3;
    }

    public String getQt4() {
        return qt4;
    }

    public void setQt4(String qt4) {
        this.qt4 = qt4;
    }

    public String getPrezzo4() {
        return prezzo4;
    }

    public void setPrezzo4(String prezzo4) {
        this.prezzo4 = prezzo4;
    }

    public String getSconto4() {
        return sconto4;
    }

    public void setSconto4(String sconto4) {
        this.sconto4 = sconto4;
    }
}
