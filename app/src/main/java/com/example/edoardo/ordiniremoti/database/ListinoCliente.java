package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class ListinoCliente extends SugarRecord<Listino> {
    String codicecliente;
    String codicearticolo;
    Float qt1;
    Float prezzo1;
    String sconto1;
    Float qt2;
    Float prezzo2;
    String sconto2;
    Float qt3;
    Float prezzo3;
    String sconto3;
    Float qt4;
    Float prezzo4;
    String sconto4;

    public ListinoCliente() {
    }

    public ListinoCliente(String codicecliente, String codicearticolo, Float qt1, Float prezzo1, String sconto1, Float qt2, Float prezzo2, String sconto2, Float qt3, Float prezzo3, String sconto3, Float qt4, Float prezzo4, String sconto4) {
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

    public Float getQt1() {
        return qt1;
    }

    public void setQt1(Float qt1) {
        this.qt1 = qt1;
    }

    public Float getPrezzo1() {
        return prezzo1;
    }

    public void setPrezzo1(Float prezzo1) {
        this.prezzo1 = prezzo1;
    }

    public String getSconto1() {
        return sconto1;
    }

    public void setSconto1(String sconto1) {
        this.sconto1 = sconto1;
    }

    public Float getQt2() {
        return qt2;
    }

    public void setQt2(Float qt2) {
        this.qt2 = qt2;
    }

    public Float getPrezzo2() {
        return prezzo2;
    }

    public void setPrezzo2(Float prezzo2) {
        this.prezzo2 = prezzo2;
    }

    public String getSconto2() {
        return sconto2;
    }

    public void setSconto2(String sconto2) {
        this.sconto2 = sconto2;
    }

    public Float getQt3() {
        return qt3;
    }

    public void setQt3(Float qt3) {
        this.qt3 = qt3;
    }

    public Float getPrezzo3() {
        return prezzo3;
    }

    public void setPrezzo3(Float prezzo3) {
        this.prezzo3 = prezzo3;
    }

    public String getSconto3() {
        return sconto3;
    }

    public void setSconto3(String sconto3) {
        this.sconto3 = sconto3;
    }

    public Float getQt4() {
        return qt4;
    }

    public void setQt4(Float qt4) {
        this.qt4 = qt4;
    }

    public Float getPrezzo4() {
        return prezzo4;
    }

    public void setPrezzo4(Float prezzo4) {
        this.prezzo4 = prezzo4;
    }

    public String getSconto4() {
        return sconto4;
    }

    public void setSconto4(String sconto4) {
        this.sconto4 = sconto4;
    }
}
