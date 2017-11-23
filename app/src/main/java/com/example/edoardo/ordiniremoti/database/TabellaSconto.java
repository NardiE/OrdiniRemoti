package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class TabellaSconto extends SugarRecord<Listino> {
    String codice;
    Float sconto1;
    Float sconto2;
    Float sconto3;
    Float sconto4;
    Float sconto5;

    public TabellaSconto() {
    }

    public TabellaSconto(String codice, Float sconto1, Float sconto2, Float sconto3, Float sconto4, Float sconto5) {
        this.codice = codice;
        this.sconto1 = sconto1;
        this.sconto2 = sconto2;
        this.sconto3 = sconto3;
        this.sconto4 = sconto4;
        this.sconto5 = sconto5;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Float getSconto1() {
        return sconto1;
    }

    public void setSconto1(Float sconto1) {
        this.sconto1 = sconto1;
    }

    public Float getSconto2() {
        return sconto2;
    }

    public void setSconto2(Float sconto2) {
        this.sconto2 = sconto2;
    }

    public Float getSconto3() {
        return sconto3;
    }

    public void setSconto3(Float sconto3) {
        this.sconto3 = sconto3;
    }

    public Float getSconto4() {
        return sconto4;
    }

    public void setSconto4(Float sconto4) {
        this.sconto4 = sconto4;
    }

    public Float getSconto5() {
        return sconto5;
    }

    public void setSconto5(Float sconto5) {
        this.sconto5 = sconto5;
    }
}
