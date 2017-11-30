package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipiConfigurazione;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Destinazione;
import com.example.edoardo.ordiniremoti.database.Listino;
import com.example.edoardo.ordiniremoti.database.ListinoCliente;
import com.example.edoardo.ordiniremoti.database.Progressivo;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.database.RigaOrdine;
import com.example.edoardo.ordiniremoti.database.ScontoC;
import com.example.edoardo.ordiniremoti.database.ScontoCA;
import com.example.edoardo.ordiniremoti.database.ScontoCM;
import com.example.edoardo.ordiniremoti.database.TabellaSconto;
import com.example.edoardo.ordiniremoti.database.TestataOrdine;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.edoardo.ordiniremoti.Activity.Impostazioni.preferences;

public class GestioneRighe extends AppCompatActivity {
    int operazione;
    int operazioneprecedente;
    Context context;
    Long idlsc;
    Long idlis;
    TestataOrdine testata;
    RigaOrdine riga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_righe_ordini);
        context = this;

        //controllo se è una modifica o un inserimento
        Bundle extras = getIntent().getExtras();
        operazione = extras.getInt(TipoExtra.tipoop);
        operazioneprecedente = extras.getInt(TipoExtra.tipoopprecedente);

        // carico i dati dello spinner
        setSpinnerList();

        if(operazione == TipoOp.OP_INSERISCI) {
            mostracampi(false);
            // mi creo un progressivo negativo
            int progressivo = -1;

            // mi salvo l'ordine da cui provengo
            Long idtestata = extras.getLong(TipoExtra.idordine);
            testata = TestataOrdine.findById(TestataOrdine.class, idtestata);

            //creo una nuova riga
            riga = new RigaOrdine(progressivo, testata.getProgressivo(),"","","","","","","","");

            //imposto nella data consegna la data dell'ordine
            if(testata.getDataconsegna()!= null) ((EditText) findViewById(R.id.edittextdataconsegnaart)).setText(testata.getDataconsegna());

        }
        if (operazione == TipoOp.OP_MODIFICA){
            mostracampi(true);
            //vado a modificare un ordine
            Long idtestata = extras.getLong(TipoExtra.idordine);
            Long idriga = extras.getLong(TipoExtra.idriga);
            //recupero testata e riga
            testata = TestataOrdine.findById(TestataOrdine.class, idtestata);
            riga = RigaOrdine.findById(RigaOrdine.class, idriga);
            //recupero l'articolo
            Articolo art = Query.getArticolofromCode(riga.getCodicearticolo());
            // carico i campi nella maschera
            loadField(art);
        }

        if (operazione == TipoOp.SCELTOARTICOLO){
            mostracampi(true);
            operazioneprecedente = extras.getInt(TipoExtra.tipoopprecedente);
            Long idarticolo = extras.getLong(TipoExtra.idarticolo);
            Long idtestata = extras.getLong(TipoExtra.idordine);
            Long idriga = extras.getLong(TipoExtra.idriga);

            //recupero riga
            riga = RigaOrdine.findById(RigaOrdine.class, idriga);

            //recupero testata
            testata = TestataOrdine.findById(TestataOrdine.class, idtestata);

            //recupero articolo
            Articolo myart = Articolo.findById(Articolo.class, idarticolo);

            //modifico l'articolo nella riga dell'ordine
            riga.setCodicearticolo(myart.getCodice());

            loadField(myart);

            //mi salvo l'operazione precedente per la gestione del back button
            operazioneprecedente=extras.getInt(TipoExtra.tipoopprecedente);

            //ritorno al dualismo inserisci / modifica
            operazione = operazioneprecedente;

        }


        //imposto un evento sul cambio di quantita
        ((EditText)findViewById(R.id.edittextquantita)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            /* When focus is lost check that the text field
            * has valid values.
            */
                if (!hasFocus) {
                    Articolo myart = Query.getArticolofromCode(riga.getCodicearticolo());
                    assegnaListino(myart);
                    String prezzo = calcolaPrezzo();
                    if(operazione != TipoOp.OP_MODIFICA) {
                        String scontoarticolo = calcolascontoArticolo();
                        String scontocliente = calcolaScontoCliente();
                        ((EditText) findViewById(R.id.edittextprezzo)).setText(prezzo);
                        ((EditText) findViewById(R.id.edittextscontoarticolo)).setText(scontoarticolo);
                        ((EditText) findViewById(R.id.edittextscontocliente)).setText(scontocliente);
                        String descrizionescontocliente = getDescrizioneSconto(scontocliente);
                        String descrizionescontoarticolo = getDescrizioneSconto(scontoarticolo);
                        ((TextView) findViewById(R.id.descrizionescontoarticolo)).setText(descrizionescontoarticolo);
                        ((TextView) findViewById(R.id.descrizionescontocliente)).setText(descrizionescontocliente);
                    }
                    String importo = calcolaImportoTotale();
                    ((TextView)findViewById(R.id.textviewimportototale)).setText(importo);
                }
            }
        });

        //imposto un evento sul cambio di prezzo
        ((EditText)findViewById(R.id.edittextprezzo)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            /* When focus is lost check that the text field
            * has valid values.
            */
                if (!hasFocus) {
                    String importo = calcolaImportoTotale();
                    ((TextView)findViewById(R.id.textviewimportototale)).setText(importo);
                }
            }
        });

        //imposto un evento sul cambio di scontocliente
        ((EditText)findViewById(R.id.edittextscontocliente)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            /* When focus is lost check that the text field
            * has valid values.
            */
                if (!hasFocus) {

                    // controllo se esiste lo sconto e se esiste setto la descrizione
                    String codicesconto = ((EditText) findViewById(R.id.edittextscontocliente)).getText().toString();
                    List<TabellaSconto> tabelle = Query.getTabelleSconto(codicesconto);
                    if(tabelle.size() == 0){
                        //messaggio di errore e azzero sconto
                        Utility.creaDialogoVeloce(context, "Codice Sconto non esistente, prego digitare un altro codice", "Avviso").create().show();
                        ((EditText) findViewById(R.id.edittextscontocliente)).setText(calcolaScontoCliente());

                    }
                    else{
                        String descrizionescontocliente = getDescrizioneSconto(tabelle.get(0).getCodice());
                        ((TextView) findViewById(R.id.descrizionescontocliente)).setText(descrizionescontocliente);
                    }
                    // calcolo l'importo
                    String importo = calcolaImportoTotale();
                    ((TextView)findViewById(R.id.textviewimportototale)).setText(importo);
                }
            }
        });

        //imposto un evento sul cambio di scontoarticolo
        ((EditText)findViewById(R.id.edittextscontoarticolo)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            /* When focus is lost check that the text field
            * has valid values.
            */
                if (!hasFocus) {
                    // controllo se esiste lo sconto e se esiste setto la descrizione

                    // controllo se esiste lo sconto e se esiste setto la descrizione
                    String codicesconto = ((EditText) findViewById(R.id.edittextscontoarticolo)).getText().toString();
                    List<TabellaSconto> tabelle = Query.getTabelleSconto(codicesconto);
                    if(tabelle.size() == 0){
                        //messaggio di errore e azzero sconto
                        Utility.creaDialogoVeloce(context, "Codice Sconto non esistente, prego digitare un altro codice", "Avviso").create().show();
                        ((EditText) findViewById(R.id.edittextscontoarticolo)).setText(calcolascontoArticolo());

                    }
                    else{
                        String descrizionescontoarticolo = getDescrizioneSconto(tabelle.get(0).getCodice());
                        ((TextView) findViewById(R.id.descrizionescontoarticolo)).setText(descrizionescontoarticolo);
                    }
                    // calcolo l'importo
                    String importo = calcolaImportoTotale();
                    ((TextView)findViewById(R.id.textviewimportototale)).setText(importo);
                }
            }
        });

    }



    private void loadField(Articolo articolo){
        if(articolo == null){
            Utility.creaDialogoVeloce(this,"Errore Grave, Rivolgersi all'assistenza","Errore Caricamento Cliente").create().show();
            return;
        }

        if(riga.getDataconsegna() != null) ((EditText) findViewById(R.id.edittextdataconsegnaart)).setText(riga.getDataconsegna());
        else if (testata.getDataconsegna() != null) ((EditText) findViewById(R.id.edittextdataconsegnaart)).setText(testata.getDataconsegna());

        if(riga.getQuantita() != null) ((EditText) findViewById(R.id.edittextquantita)).setText(riga.getQuantita());
        if(riga.getPrezzo() != null) ((EditText) findViewById(R.id.edittextprezzo)).setText(riga.getPrezzo());
        if(riga.getScontocliente() != null) ((EditText) findViewById(R.id.edittextscontocliente)).setText(riga.getScontocliente());
        if(riga.getScontoarticolo() != null) ((EditText) findViewById(R.id.edittextscontoarticolo)).setText(riga.getScontoarticolo());
        if(riga.getNoteriga() != null) ((EditText) findViewById(R.id.editTextnoteriga)).setText(riga.getNoteriga());


        if(articolo.getCodice() != null) ((TextView) findViewById(R.id.codicearticolo)).setText(articolo.getCodice());
        if(articolo.getDescrizione() != null && articolo.getDescrizione2() != null) {
            ((TextView) findViewById(R.id.nomearticolo)).setText(articolo.getDescrizione() + " " + articolo.getDescrizione2());
        }
        if(articolo.getUM() != null) ((TextView) findViewById(R.id.unitamisura)).setText(articolo.getUM());
        if(articolo.getMe() != null) ((TextView) findViewById(R.id.gruppomerceologico)).setText(articolo.getMe());
        if(articolo.getEsistenza() != null) ((TextView) findViewById(R.id.esistenza)).setText(articolo.getEsistenza());


        int pos = getSpinnerPosition(riga.getTiporiga());
        Spinner myspinner = (Spinner)findViewById(R.id.spinneromaggio);
        myspinner.setSelection(pos);
    }


    private void setSpinnerList(){
        ArrayList<String> descrdest = new ArrayList<String>();

        descrdest.add("N");
        descrdest.add("O");
        descrdest.add("M");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, descrdest);

        Spinner myspinner = (Spinner)findViewById(R.id.spinneromaggio);
        myspinner.setAdapter(adapter);
    }

    public int getSpinnerPosition(String tiporiga){
        switch (tiporiga){
            case "N":
                return 0;
            case "O":
                return 1;
            case "M":
                return 2;
            default:
                return 0;
        }
    }

    private boolean updateAndSave(boolean verbose){
        //TODO controllare dimensioni codicecliente
        int codicelenght = 15;

        // faccio il controllo delle date
        sistemaDate();
        if(verbose) {
            if (!controllaDate()) {
                return false;
            }
        }
        if(verbose) {
            //faccio il controllo deL'ARTICOLO
            if (riga.getCodicearticolo() == "" || riga.getCodicearticolo().length() > codicelenght) {
                Utility.creaDialogoVeloce(this, "Errore nella scelta del'articolo, prego selezionare un articolo", "Testata Ordini, errore articolo").create().show();
                return false;


            }
        }

        // prendo il primo carattere dello spinner
        if(((Spinner)findViewById(R.id.spinneromaggio)).getSelectedItem() != null){
            String tiporiga = ((Spinner)findViewById(R.id.spinneromaggio)).getSelectedItem().toString().substring(0,1);
            riga.setTiporiga(tiporiga);
        }

        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegnaart)).getText().toString();
        String quantità = ((EditText) findViewById(R.id.edittextquantita)).getText().toString();
        String prezzo = ((EditText) findViewById(R.id.edittextprezzo)).getText().toString();
        String scontocliente = ((EditText) findViewById(R.id.edittextscontocliente)).getText().toString();
        String scontoarticolo = ((EditText) findViewById(R.id.edittextscontoarticolo)).getText().toString();
        String noteriga = ((EditText) findViewById(R.id.editTextnoteriga)).getText().toString();

        riga.setDataconsegna(dataconsegna);
        riga.setNoteriga(noteriga);
        riga.setScontoarticolo(scontoarticolo);
        riga.setScontocliente(scontocliente);
        riga.setPrezzo(prezzo);
        riga.setQuantita(quantità);

        riga.save();

        return true;
    }

    private boolean controllaDate(){

        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegnaart)).getText().toString();
        // faccio il controllo delle date


        SimpleDateFormat FORMATO_STANDARD = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);

        try {
            FORMATO_STANDARD.parse(dataconsegna);
        } catch (ParseException e) {
            Utility.creaDialogoVeloce(this, "Errore nella data di consegna, inserire data nel formato gg/MM/YY o gg/MM/YYYY", "Testata Ordini, Errore con le date").create().show();
            return false;
        }

        return true;
    }

    private void sistemaDate(){

        EditText consegna = ((EditText) findViewById(R.id.edittextdataconsegnaart));
        String dataconsegna = consegna.getText().toString();

        // se la lunghezza della data è 6 è del tipo ggMMYY
        if(dataconsegna.length() == 6){
            dataconsegna = dataconsegna.substring(0,2) + "/" + dataconsegna.substring(2,4) + "/20" + dataconsegna.substring(4,6);
            consegna.setText(dataconsegna);
        }

        if(dataconsegna.length()==8){
            if(!dataconsegna.contains("/")){
                // se la lunghezza della data è 8 e non contiene / allora è del tipo ggMMYYYY
                dataconsegna = dataconsegna.substring(0,2) + "/" + dataconsegna.substring(2,4) + "/" + dataconsegna.substring(4,8);
                consegna.setText(dataconsegna);
            }
        }
    }

    public void cercaArticolo(View view) {
        if(operazione != TipoOp.OP_MODIFICA) {
            updateAndSave(false);
            Intent i = new Intent(this, CercaArticolo.class);
            i.putExtra(TipoExtra.tipoop, TipoOp.SELEZIONAARTICOLO);
            i.putExtra(TipoExtra.tipoopprecedente, operazione);
            i.putExtra(TipoExtra.idordine, testata.getId());
            i.putExtra(TipoExtra.idriga, riga.getId());
            startActivity(i);
        }
        else{
            Utility.creaDialogoVeloce(context,"Impossibile modificare Cliente per un ordine già inserito","Attenzione").create().show();
        }
        //TODO GESTIRE ANCHE MODIFICA DEL CLIENTE TORNATO DA RIGHE
    }

    public void confermaRiga(View view) {
        //se riesco ad aggiornare le informazioni e a salvare
        if(updateAndSave(true)) {

            //ritorno alla schermata precedente
            Intent i = new Intent(context, GestioneOrdini.class);
            i.putExtra(TipoExtra.tipoop,TipoOp.INSERITARIGA);
            i.putExtra(TipoExtra.tipoopprecedente, operazioneprecedente);
            i.putExtra(TipoExtra.idordine, testata.getId());
            startActivity(i);
        }
    }

    public void annullaRiga(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Avviso");
        builder.setMessage("Tornando indietro ogni modifica andrà persa, Continuare?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (operazione == TipoOp.OP_INSERISCI) {
                    // se annullo in inserimento elimino la riga
                    riga.save();
                    riga.delete();
                }
                if (operazione == TipoOp.SCELTOARTICOLO){
                    if(operazioneprecedente == TipoOp.OP_INSERISCI){
                        // se annullo in inserimento elimino la riga
                        riga.delete();
                    }
                    // in caso di modifica torno all'operazione precedente
                }
                //TODO Gestire Annullamenti Errori
                Intent i = new Intent(context, GestioneOrdini.class);
                i.putExtra(TipoExtra.tipoop,TipoOp.INSERITARIGA);
                i.putExtra(TipoExtra.tipoopprecedente, operazioneprecedente);
                i.putExtra(TipoExtra.idordine, testata.getId());
                startActivity(i);
            }
        });
        builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void assegnaListino(Articolo myart){
        List<Listino> listinolist;

        Cliente c = Query.getClientefromCode(testata.getCodicecliente());
        SharedPreferences sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        String codicelistino = sharedpreferences.getString(TipiConfigurazione.listinodefault,"");
        // se ho un listino di default prevale per tutto
        if(codicelistino != ""){
            listinolist = Query.getListino(myart.getCodice(), codicelistino);
            if(listinolist.size() == 0){
                idlis = Long.parseLong("-1");
                idlsc = Long.parseLong("-1");
            }
            else {
                idlis = listinolist.get(0).getId();
                idlsc = Long.parseLong("-1");
            }
        }
        else{
            //cerco se esiste un listino nel Listino Cliente
            List <ListinoCliente> listinoclientelist = Query.getListinoCliente(c.getCodice(),myart.getCodice());
            if(listinoclientelist.size() == 0){
                if(c.getListino() != "   "){
                    listinolist = Query.getListino(myart.getCodice(), c.getListino());
                    if(listinolist.size() == 0){
                        idlis = Long.parseLong("-1");
                        idlsc = Long.parseLong("-1");
                    }
                    else {
                        idlis = listinolist.get(0).getId();
                        idlsc = Long.parseLong("-1");
                    }
                }
                else{
                    idlis = Long.parseLong("-1");
                    idlsc = Long.parseLong("-1");
                }
            }
            else{
                // se esiste gli assegno il listino cliente
                idlsc = listinoclientelist.get(0).getId();
                idlis = Long.parseLong("-1");
            }
        }
    }

    private String calcolaPrezzo(){
        Float qt1;
        Float qt2;
        Float qt3;
        Float qt4;
        float quantita;

        try {
            quantita = Float.parseFloat(((EditText) findViewById(R.id.edittextquantita)).getText().toString());
        }catch (Exception e){
            Utility.creaDialogoVeloce(context, "Scegliere la quantità", "Avviso");
            return "0";
        }

        if(idlis == -1 && idlsc == -1){
            return "0";
        }
        else if (idlsc != -1){
            // recupero il listino cliente
            ListinoCliente lsc = ListinoCliente.findById(ListinoCliente.class, idlsc);
            String q1 = lsc.getQt1();
            String q2 = lsc.getQt2();
            String q3 = lsc.getQt3();
            String q4 = lsc.getQt4();
            try{
                qt1 = Float.parseFloat(q1);
                qt2 = Float.parseFloat(q2);
                qt3 = Float.parseFloat(q3);
                qt4 = Float.parseFloat(q4);
            }
            catch (Exception e){
                Utility.creaDialogoVeloce(context, "Errore sul parsing dei numeri, rivolgersi all'assistenza", "Errore Grave").create().show();
                return "0";
            }
            if(quantita <= qt1){
                return lsc.getPrezzo1();
            }
            if(quantita <= qt2){
                return lsc.getPrezzo2();
            }
            if(quantita <= qt3){
                return lsc.getPrezzo2();
            }
            if(quantita <= qt4){
                return lsc.getPrezzo4();
            }
            else return "0";
        }
        else if (idlis != -1){
            Listino ls = Listino.findById(Listino.class, idlis);
            String q1 = ls.getQt1();
            String q2 = ls.getQt2();
            String q3 = ls.getQt3();
            String q4 = ls.getQt4();
            try{
                qt1 = Float.parseFloat(q1);
                qt2 = Float.parseFloat(q2);
                qt3 = Float.parseFloat(q3);
                qt4 = Float.parseFloat(q4);
            }
            catch (Exception e){
                Utility.creaDialogoVeloce(context, "Errore sul parsing dei numeri, rivolgersi all'assistenza", "Errore Grave").create().show();
                return "0";
            }
            if(quantita <= qt1){
                return ls.getPrezzo1();
            }
            if(quantita <= qt2){
                return ls.getPrezzo2();
            }
            if(quantita <= qt3){
                return ls.getPrezzo2();
            }
            if(quantita <= qt4){
                return ls.getPrezzo4();
            }
            else return "0";
        }
        Utility.creaDialogoVeloce(context, "Errore Non Specificato", "Errore").create().show();
        return "0";
    }

    private String calcolascontoArticolo(){
        Float qt1;
        Float qt2;
        Float qt3;
        Float qt4;
        float quantita;

        try {
            quantita = Float.parseFloat(((EditText) findViewById(R.id.edittextquantita)).getText().toString());
        }catch (Exception e){
            Utility.creaDialogoVeloce(context, "Scegliere la quantità", "Avviso").create().show();
            return "";
        }

        if(idlis == -1 && idlsc == -1){
            return "";
        }
        else if (idlsc != -1){
            // recupero il listino cliente
            ListinoCliente lsc = ListinoCliente.findById(ListinoCliente.class, idlsc);
            String q1 = lsc.getQt1();
            String q2 = lsc.getQt2();
            String q3 = lsc.getQt3();
            String q4 = lsc.getQt4();
            try{
                qt1 = Float.parseFloat(q1);
                qt2 = Float.parseFloat(q2);
                qt3 = Float.parseFloat(q3);
                qt4 = Float.parseFloat(q4);
            }
            catch (Exception e){
                Utility.creaDialogoVeloce(context, "Errore sul parsing dei numeri, rivolgersi all'assistenza", "Errore Grave").create().show();
                return "";
            }
            if(quantita <= qt1){
                return lsc.getSconto1();
            }
            if(quantita <= qt2){
                return lsc.getSconto2();
            }
            if(quantita <= qt3){
                return lsc.getSconto3();
            }
            if(quantita <= qt4){
                return lsc.getSconto4();
            }
            else return "";
        }
        else if (idlis != -1){
            Listino ls = Listino.findById(Listino.class, idlis);
            String q1 = ls.getQt1();
            String q2 = ls.getQt2();
            String q3 = ls.getQt3();
            String q4 = ls.getQt4();
            try{
                qt1 = Float.parseFloat(q1);
                qt2 = Float.parseFloat(q2);
                qt3 = Float.parseFloat(q3);
                qt4 = Float.parseFloat(q4);
            }
            catch (Exception e){
                Utility.creaDialogoVeloce(context, "Errore sul parsing dei numeri, rivolgersi all'assistenza", "Errore Grave").create().show();
                return "";
            }
            if(quantita <= qt1){
                return ls.getSconto1();
            }
            if(quantita <= qt2){
                return ls.getSconto2();
            }
            if(quantita <= qt3){
                return ls.getSconto3();
            }
            if(quantita <= qt4){
                return ls.getSconto4();
            }
            else return "";
        }
        Utility.creaDialogoVeloce(context, "Errore Non Specificato", "Errore").create().show();
        return "";
    }

    private String calcolaScontoCliente(){
        Articolo a = Query.getArticolofromCode(riga.getCodicearticolo());

        String merceologico = a.getMe();
        String codicearticolo = riga.getCodicearticolo();
        String codicecliente = testata.getCodicecliente();

        List<ScontoCA> scontocas = Select.from(ScontoCA.class)
                .where(Condition.prop("codicecliente").like(codicecliente)).where(Condition.prop("codicearticolo").like(codicearticolo))
                .list();
        if(scontocas.size() > 0) return scontocas.get(0).getSconto();
        else{
            List<ScontoCM> scontocm = Select.from(ScontoCM.class)
                    .where(Condition.prop("codicecliente").like(codicecliente)).where(Condition.prop("codicemerceologico").like(merceologico))
                    .list();
            if(scontocm.size() > 0) return scontocm.get(0).getSconto();
            else{
                List<ScontoC> scontoc = Select.from(ScontoC.class)
                        .where(Condition.prop("codicecliente").like(codicecliente))
                        .list();
                if(scontoc.size() > 0) return scontoc.get(0).getSconto();
                else return "";
            }
        }
    }

    private void mostracampi(boolean mostra){
        int visible;
        if (mostra == true) visible = View.VISIBLE;
        else visible = View.INVISIBLE;
        findViewById(R.id.rl1).setVisibility(visible);
        findViewById(R.id.rl2).setVisibility(visible);
        findViewById(R.id.rl3).setVisibility(visible);
        findViewById(R.id.rl4).setVisibility(visible);
        findViewById(R.id.rl5).setVisibility(visible);
        findViewById(R.id.rl6).setVisibility(visible);
        findViewById(R.id.rl7).setVisibility(visible);
    }

    private TabellaSconto getTabellaSconto(String codicesconto){
        List <TabellaSconto> tabelle = Query.getTabelleSconto(codicesconto);
        if (tabelle.size() > 0){
            return tabelle.get(0);
        }
        else return null;
    }

    private String getDescrizioneSconto(String codicesconto){
        String result = "";
        List <TabellaSconto> tabelle = Query.getTabelleSconto(codicesconto);
        if (tabelle.size() > 0){
            TabellaSconto mytab = tabelle.get(0);
            result += mytab.getSconto1() + " " + mytab.getSconto2() + " " + mytab.getSconto3() + " " + mytab.getSconto4();
        }
        return result;
    }

    private String calcolaImportoTotale(){
        Float sconto1;
        Float sconto2;
        Float sconto3;
        Float sconto4;
        Float sconto5;

        Articolo myart = Query.getArticolofromCode(riga.getCodicearticolo());
        assegnaListino(myart);
        Float prezzo;

        float quantita;

        try {
            quantita = Float.parseFloat(((EditText) findViewById(R.id.edittextquantita)).getText().toString());
            prezzo = Float.parseFloat(calcolaPrezzo());
        }catch (Exception e){
            Utility.creaDialogoVeloce(context, "Scegliere la quantità", "Avviso").create().show();
            return "0";
        }

        // l'importo lo calcolo prendendo la stringa dallo sconto
        String scontoarticolo = ((EditText)findViewById(R.id.edittextscontoarticolo)).getText().toString();
        String scontocliente = ((EditText)findViewById(R.id.edittextscontocliente)).getText().toString();
        Float importototale = prezzo * quantita;

        TabellaSconto tabellascontoarticolo = getTabellaSconto(scontoarticolo);
        // calcolo sconto articolo
        if(tabellascontoarticolo != null){

                try{
                    sconto1 = Float.parseFloat(tabellascontoarticolo.getSconto1());
                    sconto2 = Float.parseFloat(tabellascontoarticolo.getSconto2());
                    sconto3 = Float.parseFloat(tabellascontoarticolo.getSconto3());
                    sconto4 = Float.parseFloat(tabellascontoarticolo.getSconto4());
                    sconto5 = Float.parseFloat(tabellascontoarticolo.getSconto5());
                }
                catch (Exception e){
                    Utility.creaDialogoVeloce(context, "Errore con gli sconti", "Avviso").create().show();
                    return "0";
                }
                if(sconto1 != 0){
                    importototale = importototale + (importototale * (sconto1/100));
                    importototale = round(importototale,6);
                    if(sconto2 != 0){
                        importototale = importototale + (importototale * (sconto2/100));
                        importototale = round(importototale,6);
                        if(sconto3 != 0){
                            importototale = importototale + (importototale * (sconto3/100));
                            importototale = round(importototale,6);
                            if(sconto4 != 0){
                                importototale = importototale + (importototale * (sconto4/100));
                                importototale = round(importototale,6);
                                if(sconto5 != 0){
                                    importototale = importototale + (importototale * (sconto5/100));
                                    importototale = round(importototale,6);
                                }
                            }
                        }
                    }
                }

        }
        TabellaSconto tabellascontocliente = getTabellaSconto(scontocliente);
        if(tabellascontocliente != null){

            try{
                sconto1 = Float.parseFloat(tabellascontocliente.getSconto1());
                sconto2 = Float.parseFloat(tabellascontocliente.getSconto2());
                sconto3 = Float.parseFloat(tabellascontocliente.getSconto3());
                sconto4 = Float.parseFloat(tabellascontocliente.getSconto4());
                sconto5 = Float.parseFloat(tabellascontocliente.getSconto5());
            }
            catch (Exception e){
                Utility.creaDialogoVeloce(context, "Errore con gli sconti", "Avviso").create().show();
                return "0";
            }
            if(sconto1 != 0){
                importototale = importototale + (importototale * (sconto1/100));
                importototale = round(importototale,6);
                if(sconto2 != 0){
                    importototale = importototale + (importototale * (sconto2/100));
                    importototale = round(importototale,6);
                    if(sconto3 != 0){
                        importototale = importototale + (importototale * (sconto3/100));
                        importototale = round(importototale,6);
                        if(sconto4 != 0){
                            importototale = importototale + (importototale * (sconto4/100));
                            importototale = round(importototale,6);
                            if(sconto5 != 0){
                                importototale = importototale + (importototale * (sconto5/100));
                                importototale = round(importototale,6);
                            }
                        }
                    }
                }
            }

        }
        importototale = round(importototale,4);
        return importototale.toString();
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
