package com.example.edoardo.ordiniremoti.database;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edoardo on 20/11/2017.
 */

public class Query {
/*   public static List<Barcode> getBarcode(String barcode){
        String SQL1 = "SELECT * FROM BARCODE WHERE CODICEABARRE LIKE '%" + barcode + "%'";
        List<Barcode> barcodefind;
        barcodefind = Barcode.findWithQuery(Barcode.class,SQL1);
        return barcodefind;
    }
*/
    public static List<Listino> getListino(String codicearticolo, String codicelistino){
        String SQL1 = "SELECT * FROM LISTINO WHERE CODICEARTICOLO LIKE '%" + codicearticolo + "%' AND CODICELISTINO LIKE '%" + codicelistino + "%'";
        List<Listino> listinofind;
        listinofind = Listino.findWithQuery(Listino.class,SQL1);
        return listinofind;
    }
    public static List<Articolo> getArticolo(String codicearticolo){
        String SQL1 = "SELECT * FROM ARTICOLO WHERE CODICE LIKE '%" + codicearticolo + "%'";
        List<Articolo> articolofind;
        articolofind = Articolo.findWithQuery(Articolo.class,SQL1);
        return articolofind;
    }
    public static void cleanUp(){
        Cliente.deleteAll(Cliente.class);
        Barcode.deleteAll(Barcode.class);
        Articolo.deleteAll(Articolo.class);
        Destinazione.deleteAll(Destinazione.class);
        Listino.deleteAll(Listino.class);
        ListinoCliente.deleteAll(ListinoCliente.class);
        TabellaSconto.deleteAll(TabellaSconto.class);
        ScontoC.deleteAll(ScontoC.class);
        ScontoCA.deleteAll(ScontoCA.class);
        ScontoCM.deleteAll(ScontoCM.class);

        Cliente.executeQuery("delete from sqlite_sequence where name='CLIENTE'");
        Barcode.executeQuery("delete from sqlite_sequence where name='BARCODE'");;
        Articolo.executeQuery("delete from sqlite_sequence where name='ARTICOLO'");
        Destinazione.executeQuery("delete from sqlite_sequence where name='DESTINAZIONE'");
        Listino.executeQuery("delete from sqlite_sequence where name='LISTINO'");
        ListinoCliente.executeQuery("delete from sqlite_sequence where name='LISTINOCLIENTE'");
        TabellaSconto.executeQuery("delete from sqlite_sequence where name='TABELLASCONTO'");
        ScontoC.executeQuery("delete from sqlite_sequence where name='SCONTOC'");
        ScontoCA.executeQuery("delete from sqlite_sequence where name='SCONTOCA'");
        ScontoCM.executeQuery("delete from sqlite_sequence where name='SCONTOCM'");
    }

    public static void insertSample(){
    }

    public static List<Articolo> getItemsSearching(String param){
        String SQL1 = "SELECT * FROM Articolo WHERE codice LIKE '%" + param + "%' OR descrizione LIKE '%" + param + "%'";
        List<Articolo> articolofind;
        articolofind = Articolo.findWithQuery(Articolo.class,SQL1);
        return articolofind;
    }

    public static List<Articolo> getAll(){
        return Select.from(Articolo.class).list();
    }

    public static List<Articolo> getTop100Items(){
        return Select.from(Articolo.class).limit("100").list();
    }

    public static List<Articolo> getTop100ItemsbyParam(String param){
        return Select.from(Articolo.class)
                .where(Condition.prop("codice").like(param),
                        Condition.prop("descrizione").like(param))
                .limit("100").list();
    }

    public static List<Cliente> getClientsSearching(String param){
        String SQL1 = "SELECT * FROM Cliente WHERE codice LIKE '%" + param + "%' OR descrizione LIKE '%" + param + "%'";
        List<Cliente> clientsfind;
        clientsfind = Cliente.findWithQuery(Cliente.class,SQL1);
        return clientsfind;
    }

    public static List<Cliente> getTop100Clienti(){
        return Select.from(Cliente.class).limit("100").list();
    }

}

