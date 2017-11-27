package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

/**
 * Created by edoardo on 23/11/2017.
 */

public class ScontoCM extends SugarRecord<ScontoCM> {
    String codicecliente;
    String codicemerceologico;
    String sconto;

    public ScontoCM() {
    }

    public ScontoCM(String codicecliente, String codicemerceologico, String sconto) {
        this.codicecliente = codicecliente;
        this.codicemerceologico = codicemerceologico;
        this.sconto = sconto;
    }

    public String getCodicecliente() {
        return codicecliente;
    }

    public void setCodicecliente(String codicecliente) {
        this.codicecliente = codicecliente;
    }

    public String getCodicemerceologico() {
        return codicemerceologico;
    }

    public void setCodicemerceologico(String codicemerceologico) {
        this.codicemerceologico = codicemerceologico;
    }

    public String getSconto() {
        return sconto;
    }

    public void setSconto(String sconto) {
        this.sconto = sconto;
    }
}
