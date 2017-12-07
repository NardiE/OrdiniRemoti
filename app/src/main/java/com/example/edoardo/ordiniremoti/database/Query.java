package com.example.edoardo.ordiniremoti.database;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edoardo on 20/11/2017.
 */

public class Query {
    public static List<Articolo> getArticolo(String codicearticolo){
        String SQL1 = "SELECT * FROM ARTICOLO WHERE CODICE LIKE '%" + codicearticolo + "%'";
        List<Articolo> articolofind;
        articolofind = Articolo.findWithQuery(Articolo.class,SQL1);
        return articolofind;
    }

    public static List<Articolo> getAll(){
        return Select.from(Articolo.class).list();
    }


    public static List<Articolo> getItemsSearching(String param){
        String SQL1 = "SELECT * FROM Articolo WHERE codice LIKE '%" + param + "%' OR descrizione LIKE '%" + param + "%'";
        List<Articolo> articolofind;
        articolofind = Articolo.findWithQuery(Articolo.class,SQL1);
        return articolofind;
    }

    public static List<Articolo> getTop100Items(){
        return Select.from(Articolo.class).limit("100").list();
    }

    public static List<Articolo> getTopItemsbyParam(String param, int limit){
        String SQL1 = "SELECT * FROM Articolo WHERE codice LIKE '%" + param + "%' OR descrizione LIKE '%" + param + "%' LIMIT " + limit + "";
        List<Articolo> articolofind;
        articolofind = Articolo.findWithQuery(Articolo.class,SQL1);
        return articolofind;
    }

    public static List<Cliente> getClientsSearching(String param){
        String SQL1 = "SELECT * FROM Cliente WHERE codice LIKE '%" + param + "%' OR descrizione LIKE '%" + param + "%'";
        List<Cliente> clientsfind;
        clientsfind = Cliente.findWithQuery(Cliente.class,SQL1);
        return clientsfind;
    }

    public static List<Cliente> getTopClientbyParam(String param, int limit){
        String SQL1 = "SELECT * FROM Cliente WHERE codice LIKE '%" + param + "%' OR descrizione LIKE '%" + param + "%' LIMIT " + limit + "";
        List<Cliente> clientsfind;
        clientsfind = Cliente.findWithQuery(Cliente.class,SQL1);
        return clientsfind;
    }

    public static List<Cliente> getTop100Clienti(){
        return Select.from(Cliente.class).limit("100").list();
    }

    public static List<TestataOrdine> getOrdiniFromClient(String codicecliente){
        return Select.from(TestataOrdine.class)
                .where(Condition.prop("codicecliente").like(codicecliente))
                .list();
    }

    // cancella ordini e azzera i progressivi
    public static void softCleanUp(){

        List<Cliente> clients = Query.getNuoviClienti();

        for(Cliente c: clients){
            c.delete();
        }

        TestataOrdine.deleteAll(TestataOrdine.class);
        RigaOrdine.deleteAll(RigaOrdine.class);
        TestataOrdine.executeQuery("delete from sqlite_sequence where name='TESTATAORDINE'");
        RigaOrdine.executeQuery("delete from sqlite_sequence where name='RIGAORDINE'");

        Progressivo.eliminaProgressivoOrdine();
        Progressivo.creaProgressivoOrdine();
        Progressivo.eliminaProgressivoCliente();
        Progressivo.creaProgressivoCliente();
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
        TestataOrdine.deleteAll(TestataOrdine.class);
        RigaOrdine.deleteAll(RigaOrdine.class);

        Cliente.executeQuery("delete from sqlite_sequence where name='CLIENTE'");
        Barcode.executeQuery("delete from sqlite_sequence where name='BARCODE'");
        Articolo.executeQuery("delete from sqlite_sequence where name='ARTICOLO'");
        Destinazione.executeQuery("delete from sqlite_sequence where name='DESTINAZIONE'");
        Listino.executeQuery("delete from sqlite_sequence where name='LISTINO'");
        ListinoCliente.executeQuery("delete from sqlite_sequence where name='LISTINOCLIENTE'");
        TabellaSconto.executeQuery("delete from sqlite_sequence where name='TABELLASCONTO'");
        ScontoC.executeQuery("delete from sqlite_sequence where name='SCONTOC'");
        ScontoCA.executeQuery("delete from sqlite_sequence where name='SCONTOCA'");
        ScontoCM.executeQuery("delete from sqlite_sequence where name='SCONTOCM'");
        TestataOrdine.executeQuery("delete from sqlite_sequence where name='TESTATAORDINE'");
        RigaOrdine.executeQuery("delete from sqlite_sequence where name='RIGAORDINE'");

        Progressivo.eliminaProgressivoOrdine();
        Progressivo.creaProgressivoOrdine();
        Progressivo.eliminaProgressivoCliente();
        Progressivo.creaProgressivoCliente();
    }

    public static void insertSample(){

        /*Cliente c1 = new Cliente("CLIENTE1","Cliente Uno","Spa","MIO","057164655","","","Via Rossi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");
        Cliente c2 = new Cliente("CLIENTE2","Cliente Due","Spa","TUO","057164655","","","Via Bianchi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");
        Cliente c3 = new Cliente("CLIENTE3","Cliente Tre","Spa","SUO","057164655","","","Via Rossi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");
        Cliente c4 = new Cliente("CLIENTE3","Cliente Tre","Spa","   ","057164655","","","Via Rossi 46","50051","Castelfiorentino","Firenze","","","","edo--91@hotmail.it","RIB","Ricevuta Bancaria","Banca Popolare di Milano","vettore1","vettore2");

        Articolo a1 = new Articolo("ART1", "Articolo Uno", "Di Tanti","PZ.","ME1","IN1","CM1","40","2","123","124");
        Articolo a2 = new Articolo("ART2", "Articolo Due", "Di Tanti","NR.","ME2","IN1","CM2","40","2","123","124");

        Listino l1 = new Listino("ART1","MIO","2","3","SC1","","4","2","SC2","","6","2","SC3","","8","2","SC2","");
        Listino l2 = new Listino("ART1","TUO","1","3","SC1","","4","2","SC2","","10","2","SC3","","12","2","SC2","");

        Destinazione d1 = new Destinazione("CLIENTE1","DES1  ","Via uno","Colle","via uno 61","50054","Colle","SI");
        Destinazione d2 = new Destinazione("CLIENTE1","DES2  ","Via due","Colle","via uno 61","50054","Colle","SI");
        Destinazione d3 = new Destinazione("CLIENTE2","DES3  ","Via tre","Colle","via uno 61","50054","Colle","SI");
        Destinazione d4 = new Destinazione("CLIENTE3","DES4  ","Via quattro","Colle","via uno 61","50054","Colle","SI");

        ListinoCliente lc1 = new ListinoCliente("CLIENTE1","ART1","2","6.5","SC2","4","5.5","SC2","6","5.5","SC3","8","5.5","SC2");
        ListinoCliente lc2 = new ListinoCliente("CLIENTE2","ART2","1","6.5","SC2","2","5.5","SC2","3","5.5","SC3","10","5.5","SC2");


        TabellaSconto tb1 = new TabellaSconto("SC1","-50.00","-30.00","-15.00","0","0");
        TabellaSconto tb2 = new TabellaSconto("SC2","-55.00","-30.00","-15.00","0","0");
        TabellaSconto tb3 = new TabellaSconto("SC3","-60.00","-30.00","-15.00","0","0");

        ScontoC sc = new ScontoC("CLIENTE 3","SC1");

        ScontoCM scm = new ScontoCM("CLIENTE1","ME2","SC3");

        ScontoCA sca = new ScontoCA("CLIENTE1","ART1","SC2");

        c1.save();c2.save();c3.save();a1.save();a2.save();l1.save();l2.save();d1.save();d2.save();d3.save();d4.save();lc1.save();lc2.save();tb1.save();tb2.save();tb3.save();sc.save();scm.save();
        sca.save();

        Progressivo.creaProgressivoOrdine();*/
    }

    public static Cliente getClientefromCode(String param){
        String SQL1 = "SELECT * FROM Cliente WHERE codice LIKE '%" + param + "%'";
        List<Cliente> clientsfind;
        clientsfind = Cliente.findWithQuery(Cliente.class,SQL1);
        if(clientsfind.size() == 1) {
            return clientsfind.get(0);
        }
        else return null;
    }

    public static Articolo getArticolofromCode(String param){
        String SQL1 = "SELECT * FROM Articolo WHERE codice LIKE '%" + param + "%'";
        List<Articolo> articolifind;
        articolifind = Articolo.findWithQuery(Articolo.class,SQL1);
        if(articolifind.size() == 1) {
            return articolifind.get(0);
        }
        else return null;
    }

    public static List<RigaOrdine> getAllRigheFromProgressivoOrdine(int progressivoordine){
        return Select.from(RigaOrdine.class)
                .where(Condition.prop("progressivoordine").like(progressivoordine))
                .list();
    }

    public static List<Listino> getListino(String codiceart, String codicelis){
        return Select.from(Listino.class)
                .where(Condition.prop("codicearticolo").like(codiceart)).where(Condition.prop("codicelistino").like(codicelis))
                .list();
    }

    public static List<ListinoCliente> getListinoCliente(String codicecli, String codiceart){
        return Select.from(ListinoCliente.class)
                .where(Condition.prop("codicecliente").like(codicecli)).where(Condition.prop("codicearticolo").like(codiceart))
                .list();
    }

    public static List<TabellaSconto> getTabelleSconto(String codicesconto){
         return Select.from(TabellaSconto.class)
                .where(Condition.prop("codice").like(codicesconto)).list();
    }

    public static List <Cliente> getNuoviClienti(){
        String SQL1 = "SELECT * FROM Cliente WHERE codice LIKE '$%'";
        //noinspection UnusedAssignment
        List<Cliente> clientsfind;
        return Cliente.findWithQuery(Cliente.class,SQL1);
    }

    public static TestataOrdine getTestata(int progressivo){
        List<TestataOrdine> testatefind =   Select.from(TestataOrdine.class)
                                            .where(Condition.prop("progressivo").eq(progressivo)).list();
        if(testatefind.size() == 1){
            return testatefind.get(0);
        }
        else return null;
    }
}

