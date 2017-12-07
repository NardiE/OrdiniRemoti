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
    public static String CLIENTE = "cliente";
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

    public static void creaProgressivoOrdine(){
        Progressivo p = new Progressivo(Progressivo.ORDINE, -1);
        p.save();
    }

    public static boolean eliminaProgressivoOrdine(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").eq(Progressivo.ORDINE)).list();

        if (lp.size() == 1) {
            Progressivo p = lp.get(0);
            p.delete();
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean aumentaProgressivoOrdine(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").eq(Progressivo.ORDINE)).list();

        if (lp.size() == 1) {
            Progressivo p = lp.get(0);
            int valore = p.getValore();
            p.setValore(valore - 1);
            p.save();
            return true;
        }
        else{
            return false;
        }
    }

    public static int getLastProgressivoOrdine(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").eq(Progressivo.ORDINE)).list();
        if (lp.size() == 1) {
            return lp.get(0).getValore();
        }
        else{
            return 1;
        }
    }

    public static void creaProgressivoCliente(){
        Progressivo p = new Progressivo(Progressivo.CLIENTE, 0);
        p.save();
    }

    public static boolean eliminaProgressivoCliente(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").eq(Progressivo.CLIENTE)).list();

        if (lp.size() == 1) {
            Progressivo p = lp.get(0);
            p.delete();
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean aumentaProgressivoCliente(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").eq(Progressivo.CLIENTE)).list();

        if (lp.size() == 1) {
            Progressivo p = lp.get(0);
            int valore = p.getValore();
            p.setValore(valore + 1);
            p.save();
            return true;
        }
        else{
            return false;
        }
    }

    public static int getLastProgressivoCliente(){
        List <Progressivo> lp = Select.from(Progressivo.class).where(Condition.prop("codice").eq(Progressivo.CLIENTE)).list();
        if (lp.size() == 1) {
            return lp.get(0).getValore();
        }
        else{
            return 1;
        }
    }

}
