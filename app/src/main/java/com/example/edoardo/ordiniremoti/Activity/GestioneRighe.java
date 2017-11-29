package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Destinazione;
import com.example.edoardo.ordiniremoti.database.Progressivo;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.database.RigaOrdine;
import com.example.edoardo.ordiniremoti.database.TestataOrdine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GestioneRighe extends AppCompatActivity {
    int operazione;
    int operazioneprecedente;
    Context context;
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
            operazione = operazioneprecedente;
        }

    }



    private void loadField(Articolo articolo){
        if(articolo == null){
            Utility.creaDialogoVeloce(this,"Errore Grave, Rivolgersi all'assistenza","Errore Caricamento Cliente");
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
}
