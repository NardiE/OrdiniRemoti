package com.example.edoardo.ordiniremoti.database;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edoardo on 20/11/2017.
 */

public class Query {
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

        Cliente c1 = new Cliente("CLIENTE1","Cliente Uno","Spa","MIO","057164655","","","Via Rossi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");
        Cliente c2 = new Cliente("CLIENTE2","Cliente Due","Spa","TUO","057164655","","","Via Bianchi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");
        Cliente c3 = new Cliente("CLIENTE3","Cliente Tre","Spa","SUO","057164655","","","Via Rossi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");

        Articolo a1 = new Articolo("ART1", "Articolo Uno", "Di Tanti","PZ.","ME1","IN1","CM1","40","2","123","124");
        Articolo a2 = new Articolo("ART2", "Articolo Due", "Di Tanti","NR.","ME2","IN1","CM2","40","2","123","124");

        Listino l1 = new Listino("ART1","MIO",new Float(2),new Float(3),"SC1","",new Float(4),new Float(2),"SC2","",new Float(4),new Float(2),"SC2","",new Float(4),new Float(2),"SC2","");
        Listino l2 = new Listino("ART1","TUO",new Float(2),new Float(4),"SC2","",new Float(4),new Float(3),"SC2","",new Float(4),new Float(2),"SC2","",new Float(4),new Float(2),"SC2","");
        Listino l3 = new Listino("ART2","TUO",new Float(2),new Float(8),"SC1","",new Float(4),new Float(6),"SC1","",new Float(4),new Float(2),"SC1","",new Float(4),new Float(2),"SC2","");
        Listino l4 = new Listino("ART2","TUO",new Float(2),new Float(7),"SC2","",new Float(4),new Float(5),"SC2","",new Float(4),new Float(2),"SC2","",new Float(4),new Float(2),"SC2","");

        Destinazione d1 = new Destinazione("CLIENTE1","DES1  ","Via uno","Colle","via uno 61","50054","Colle","SI");
        Destinazione d2 = new Destinazione("CLIENTE1","DES2  ","Via due","Colle","via uno 61","50054","Colle","SI");
        Destinazione d3 = new Destinazione("CLIENTE2","DES3  ","Via tre","Colle","via uno 61","50054","Colle","SI");
        Destinazione d4 = new Destinazione("CLIENTE3","DES4  ","Via quattro","Colle","via uno 61","50054","Colle","SI");

        ListinoCliente lc1 = new ListinoCliente("CLIENTE1","ART1",new Float(2),new Float(6.5),"SC2",new Float(2),new Float(5.5),"SC2",new Float(2),new Float(5.5),"SC2",new Float(2),new Float(5.5),"SC2");
        ListinoCliente lc2 = new ListinoCliente("CLIENTE2","ART2",new Float(2),new Float(2.5),"SC2",new Float(2),new Float(1.5),"",new Float(2),new Float(1.5),"SC2",new Float(2),new Float(1.5),"SC2");

        TabellaSconto tb1 = new TabellaSconto("SC1",new Float(-50.00),new Float(-30.00),new Float(-15.00),new Float(0),new Float(0));
        TabellaSconto tb2 = new TabellaSconto("SC2",new Float(-40.00),new Float(-20.00),new Float(-10.00),new Float(0),new Float(0));
        TabellaSconto tb3 = new TabellaSconto("SC3",new Float(-10.00),new Float(-5.00),new Float(0),new Float(0),new Float(0));

        ScontoC sc = new ScontoC("CLIENTE 3","SC1");

        ScontoCM scm = new ScontoCM("CLIENTE1","CM1","SC3");

        ScontoCA sca = new ScontoCA("CLIENTE1","ART1","SC2");

        c1.save();c2.save();c3.save();a1.save();a2.save();l1.save();l2.save();l3.save();l4.save();d1.save();d2.save();d3.save();d4.save();lc1.save();lc2.save();tb1.save();tb2.save();tb3.save();sc.save();scm.save();
        sca.save();
    }


}

