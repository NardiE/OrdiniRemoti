package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
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
import com.example.edoardo.ordiniremoti.database.TestataOrdine;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.support.design.R.styleable.Spinner;

public class GestioneOrdini extends AppCompatActivity {
    TestataOrdine testata;
    ArrayList<Articolo> righeordine = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testata_ordini);

        //controllo se è una modifica o un inserimento
        Bundle extras = getIntent().getExtras();
        int tipoop = extras.getInt(TipoExtra.tipoop);

        if(tipoop == TipoOp.OP_INSERISCI) {
            // assegno un progressivo negativo dal momento che è un ordine inserito
            int progressivo = -1;
            testata = new TestataOrdine(progressivo, "", "", "", "", "","");

        }

        if(tipoop == TipoOp.OP_MODIFICA){
            //TODO è una modifica
            int numero_ordine = extras.getInt(TipoExtra.numeroordine);
            String codicecliente = extras.getString(TipoExtra.codicecliente);
            String destinazione = extras.getString(TipoExtra.destinazione);
        }

        if(tipoop == TipoOp.SCELTOCLIENTE){
            // ho scelto il cliente

            // recupero l'ordine
            Long idordine = extras.getLong(TipoExtra.idordine);
            testata = TestataOrdine.findById(TestataOrdine.class, idordine);

            //recupero il cliente
            Long idcliente = extras.getLong(TipoExtra.idcliente);
            Cliente myclient = Cliente.findById(Cliente.class, idcliente);

            //modifico il cliente nella testata dell'ordine
            testata.setCodicecliente(myclient.getCodice());


            //inserisco le operazioni legate all'ordine
            if (testata.getDataconsegna() != null)((EditText) findViewById(R.id.edittextdataconsegna)).setText(testata.getDataconsegna());
            if (testata.getDataordine() != null) ((EditText) findViewById(R.id.edittextdataordine)).setText(testata.getDataordine());
            if (testata.getNumeroordine() != null) ((EditText) findViewById(R.id.edittextnumeroordine)).setText(testata.getNumeroordine());
            if (testata.getNotetestata() != null) ((EditText) findViewById(R.id.edittextnotetestata)).setText(testata.getNotetestata());

            //inserisco le operazioni legate al cliente
            if (myclient.getCodice() != "")((TextView) findViewById(R.id.codicecliente)).setText(myclient.getCodice());
            if (myclient.getDescrizione() != "" && myclient.getDescrizione2() != "")((TextView) findViewById(R.id.descrizionecliente)).setText(myclient.getDescrizione() + " " + myclient.getDescrizione2());
            if (myclient.getCitta() != "" && myclient.getVia() != "" && myclient.getProvincia() != "")((TextView) findViewById(R.id.indirizzocliente)).setText(myclient.getVia() + " " + myclient.getCitta() + " " + myclient.getProvincia());
            if (myclient.getTelefono() != "")((TextView) findViewById(R.id.telefono)).setText(myclient.getTelefono());
            if (myclient.getEmail() != "")((TextView) findViewById(R.id.email)).setText(myclient.getEmail());
            if (myclient.getDescrizionepagamento() != "" && myclient.getBanca() != "")((TextView) findViewById(R.id.descrizionipagamento)).setText(myclient.getDescrizionepagamento() + " " + myclient.getBanca());
            if (myclient.getCodice() != "")((TextView) findViewById(R.id.codicecliente)).setText(myclient.getCodice());

            //inserisco informazioni nello spinner delle destinazioni
            setSpinnerList(myclient.getCodice());

        }


    }


    public void cercaCliente(View view) {
        testata.save();
        Intent i = new Intent(this, SearchClient.class);
        i.putExtra(TipoExtra.tipoop, TipoOp.SELEZIONACLIENTE);
        i.putExtra(TipoExtra.idordine, testata.getId());
        startActivity(i);
    }

    private ListAdapter aggiornaListaArticoli() {
        return null;
    }

    private void setSpinnerList(String codicecliente){
        ArrayList<String> descrdest = new ArrayList<String>();

        List <Destinazione> destinazioni =  Select.from(Destinazione.class)
                                            .where(Condition.prop("codicecliente").like(codicecliente))
                                            .list();

        for(Destinazione d : destinazioni){
            descrdest.add(d.getCodice() + " " + d.getVia() + " " + d.getCitta());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, descrdest);

        Spinner myspinner = (Spinner)findViewById(R.id.spinnerdestinazione);
        myspinner.setAdapter(adapter);

    }

    private void updateInfo(){
        //TODO controllare dimensioni codicecliente
        int codicelenght = 8;


        // faccio il controllo delle date
        sistemaDate();
        if(!controllaDate()){
            return;
        }

        //faccio il controllo del cliente
        if(testata.getCodicecliente() == "" || testata.getCodicecliente().length() >= codicelenght){
            AlertDialog.Builder message = Utility.creaDialogoVeloce(this, "Errore nella scelta del cliente, prego selezionare un cliente", "Testata Ordini, Errore Cliente");
            return;
        }

        // prendo i primi sei caratteri dello spinner (codice = 6 caratteri)
        String destinazione = ((Spinner)findViewById(R.id.spinnerdestinazione)).getSelectedItem().toString().substring(0,6);

        // riempio le altre informazioni
        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegna)).getText().toString();
        String dataordine = ((EditText) findViewById(R.id.edittextdataordine)).getText().toString();
        //TODO impostato a 30 chiedere a maurizio
        String numeroordine = ((EditText) findViewById(R.id.edittextnumeroordine)).getText().toString().substring(0,30);
        //TODO impostato a 60 chiedere a maurizio
        String notetestata = ((EditText) findViewById(R.id.edittextnotetestata)).getText().toString().substring(0,60);

        testata.setDestinazione(destinazione);
        testata.setDataconsegna(dataconsegna);
        testata.setDataordine(dataordine);
        testata.setNumeroordine(numeroordine);
        testata.setNotetestata(notetestata);

        //salvo l'ordine
        testata.save();
    }

    private boolean controllaDate(){

        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegna)).getText().toString();
        String dataordine = ((EditText) findViewById(R.id.edittextdataordine)).getText().toString();
        Date consegna_date = new Date();
        Date ordine_date = new Date();
        // faccio il controllo delle date

        SimpleDateFormat formatostandard = new SimpleDateFormat("dd-MM-YYYY", Locale.ITALY);

        try {
            consegna_date = formatostandard.parse(dataconsegna);
        } catch (ParseException e) {
            AlertDialog.Builder message = Utility.creaDialogoVeloce(this, "Errore nella data di consegna, inserire data nel formato gg/MM/YY o gg/MM/YYYY", "Testata Ordini, Errore con le date");
            return false;
        }

        try {
            ordine_date = formatostandard.parse(dataordine);
        } catch (ParseException e) {
            AlertDialog.Builder message = Utility.creaDialogoVeloce(this, "Errore nella data dell'ordine, inserire data nel formato gg/MM/YY o gg/MM/YYYY", "Testata Ordini, Errore con le date");
            return false;
        }
        return true;
    }

    private void sistemaDate(){

        EditText consegna = ((EditText) findViewById(R.id.edittextdataconsegna));
        EditText ordine = ((EditText) findViewById(R.id.edittextdataordine));
        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegna)).getText().toString();
        String dataordine = ((EditText) findViewById(R.id.edittextdataordine)).getText().toString();

        // se la lunghezza della data è 6 è del tipo ggMMYY
        if(dataconsegna.length() == 6){
            dataconsegna = dataconsegna.substring(0,2) + "/" + dataconsegna.substring(2,4) + "/20" + dataconsegna.substring(4,6);
            consegna.setText(dataconsegna);
        }

        // se la lunghezza della data è 6 è del tipo ggMMYY
        if(dataordine.length() == 6){
            dataordine = dataordine.substring(0,2) + "/" + dataordine.substring(2,4) + "/20" + dataordine.substring(4,6);
            ordine.setText(dataordine);
        }

        if(dataconsegna.length()==8){
            if(!dataconsegna.contains("/")){
                // se la lunghezza della data è 8 e non contiene / allora è del tipo ggMMYYYY
                dataconsegna = dataconsegna.substring(0,2) + "/" + dataconsegna.substring(2,4) + "/" + dataconsegna.substring(4,8);
                consegna.setText(dataconsegna);
            }
        }

        if(dataordine.length() == 8){
            if(!dataordine.contains("/")){
                // se la lunghezza della data è 8 e non contiene / allora è del tipo ggMMYYYY
                dataordine = dataordine.substring(0,2) + "/" + dataordine.substring(2,4) + "/" + dataordine.substring(4,8);
                ordine.setText(dataordine);
            }
        }
    }
}
