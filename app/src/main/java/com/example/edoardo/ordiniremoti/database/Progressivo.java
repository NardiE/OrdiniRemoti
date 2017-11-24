package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by edoardo on 23/11/2017.
 */

public class Progressivo extends SugarRecord<Progressivo> {
    public static String ORDINE = "ordine";
    private String codice;
    private int valore;

    public Progressivo() {
    }

    public Progressivo(String codice, int valore) {
        this.codice = codice;
        this.valore = valore;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    public static int getLastProgressivoOrdine(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").like(Progressivo.ORDINE)).list();
        if (lp.size() == 1) {
            return lp.get(0).getValore();
        }
        else{
            return 1;
        }
    }
}
