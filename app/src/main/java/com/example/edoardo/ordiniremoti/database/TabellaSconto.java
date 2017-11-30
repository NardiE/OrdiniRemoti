package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class TabellaSconto extends SugarRecord<TabellaSconto> {
    String codice;
    String sconto1;
    String sconto2;
    String sconto3;
    String sconto4;
    String sconto5;

    public TabellaSconto() {
    }

    public TabellaSconto(String codice, String sconto1, String sconto2, String sconto3, String sconto4, String sconto5) {
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

    public String getSconto1() {
        return sconto1;
    }

    public void setSconto1(String sconto1) {
        this.sconto1 = sconto1;
    }

    public String getSconto2() {
        return sconto2;
    }

    public void setSconto2(String sconto2) {
        this.sconto2 = sconto2;
    }

    public String getSconto3() {
        return sconto3;
    }

    public void setSconto3(String sconto3) {
        this.sconto3 = sconto3;
    }

    public String getSconto4() {
        return sconto4;
    }

    public void setSconto4(String sconto4) {
        this.sconto4 = sconto4;
    }

    public String getSconto5() {
        return sconto5;
    }

    public void setSconto5(String sconto5) {
        this.sconto5 = sconto5;
    }
}
