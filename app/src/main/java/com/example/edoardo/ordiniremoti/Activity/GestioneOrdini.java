package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipiConfigurazione;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Destinazione;
import com.example.edoardo.ordiniremoti.database.Progressivo;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.database.RigaOrdine;
import com.example.edoardo.ordiniremoti.database.TabellaSconto;
import com.example.edoardo.ordiniremoti.database.TestataOrdine;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GestioneOrdini extends AppCompatActivity {
    TestataOrdine testata;
    int operazione;
    int operazioneprecedente;
    String codicearticolo;
    Float importototale;
    Context context;
    List<RigaOrdine> righeordine = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testata_ordini);

        //controllo se è attivata l'opzione di inserimento nuovo cliente
        SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);
        if(sharedpreferences.getString(TipiConfigurazione.nuovocliente,"").equals("SI")){
            ((TextView) findViewById(R.id.gonuovocliente)).setVisibility(View.VISIBLE);
        }

        context = this;

        //controllo se è una modifica o un inserimento
        Bundle extras = getIntent().getExtras();
        operazione = extras.getInt(TipoExtra.tipoop);

        if(operazione == TipoOp.OP_INSERISCI) {
            mostracampi(false);
            // assegno un progressivo negativo dal momento che è un ordine inserito
            int progressivo = Progressivo.getLastProgressivoOrdine();
            if(!Progressivo.aumentaProgressivoOrdine()) Utility.creaDialogoVeloce(context,"Rivolgersi Immediatamente all'assistenza","Errore Grave");
            testata = new TestataOrdine(progressivo, "", "", "", "", "","");

        }

        if(operazione == TipoOp.OP_MODIFICA){
            mostracampi(true);
            //vado a modificare un ordine
            Long idordine = extras.getLong(TipoExtra.idordine);
            testata = TestataOrdine.findById(TestataOrdine.class, idordine);
            //recupero il cliente
            Cliente myclient = Query.getClientefromCode(testata.getCodicecliente());
            // carico i campi nella maschera
            loadField(myclient);

            //aggiorno la lista di righe
            righeordine = Query.getAllRigheFromProgressivoOrdine(testata.getProgressivo());

        }

        if(operazione == TipoOp.SCELTOCLIENTE  ){
            mostracampi(true);
            // ho scelto il cliente

            // recupero l'ordine
            Long idordine = extras.getLong(TipoExtra.idordine);
            testata = TestataOrdine.findById(TestataOrdine.class, idordine);

            //recupero il cliente
            Long idcliente = extras.getLong(TipoExtra.idcliente);
            Cliente myclient = Cliente.findById(Cliente.class, idcliente);

            //modifico il cliente nella testata dell'ordine
            testata.setCodicecliente(myclient.getCodice());

            loadField(myclient);

            //mi salvo l'operazione precedente per la gestione del back button
            operazioneprecedente=extras.getInt(TipoExtra.tipoopprecedente);

            //ritorno al dualismo inserisci / modifica
            operazione = operazioneprecedente;
            righeordine = Query.getAllRigheFromProgressivoOrdine(testata.getProgressivo());

        }

        if(operazione == TipoOp.INSERITARIGA){
            mostracampi(true);
            // recupero l'ordine
            Long idordine = extras.getLong(TipoExtra.idordine);
            testata = TestataOrdine.findById(TestataOrdine.class, idordine);

            Cliente myclient = Query.getClientefromCode(testata.getCodicecliente());
            // carico i campi nella maschera
            loadField(myclient);

            //aggiorno la lista di righe
            righeordine = Query.getAllRigheFromProgressivoOrdine(testata.getProgressivo());

        }


        if(righeordine != null) {
            // imposto l'adapter per la lista
            ListView listarighe = ((ListView) findViewById(R.id.listrighe));

            ListAdapter simpleAdapter = aggiornaLista();

            listarighe.setAdapter(simpleAdapter);

            registerForContextMenu(listarighe);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        // il primo elemento è la descrizione
        menu.setHeaderTitle(righeordine.get(info.position).getCodicearticolo() + " " + righeordine.get(info.position).getProgressivo() + " " + righeordine.get(info.position).getDataconsegna() );
        String[] menuitems = getResources().getStringArray(R.array.menu);
        for(int i = 0; i< menuitems.length; i++){
            menu.add(Menu.NONE, i, i, menuitems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        Long ID;

        ListView lw = (ListView) findViewById(((ListView)info.targetView.getParent()).getId());
        Adapter ad = lw.getAdapter();
        Object item1 = ad.getItem(info.position);

        @SuppressWarnings("unchecked")
        HashMap<String, String> transf = (HashMap<String, String>) item1;
        ID = Long.parseLong(transf.get("IDRIGA"));
        final RigaOrdine riga = RigaOrdine.findById(RigaOrdine.class, ID);


        switch(menuItemIndex) {
            case 0:
                //modifica
                Intent i = new Intent(this,GestioneRighe.class);
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_MODIFICA);
                i.putExtra(TipoExtra.idordine, testata.getId());
                i.putExtra(TipoExtra.idriga, ID);
                startActivity(i);
                return true;
            case 1:
                //Elimina
                if(riga.getProgressivo()>=0){
                    Toast.makeText(this, "Non è possibile eliminare Righe non generate da Tablet", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Avviso");
                    builder.setMessage("L'Eliminazione è irreversibile, Continuare?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            riga.delete();
                            //aggiorno la lista di righe
                            righeordine = Query.getAllRigheFromProgressivoOrdine(testata.getProgressivo());

                            ListView listaordini = ((ListView) findViewById(R.id.listrighe));
                            ListAdapter simpleAdapter = aggiornaLista();
                            listaordini.setAdapter(simpleAdapter);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                return true;
        }
        return true;
    }

    public void confermaOrdine(View view) {

           // inserisco l'ordine solo se ho una riga
           if(righeordine.size() != 0) {
               //se riesco ad aggiornare le informazioni e a salvare
               if(updateAndSave(true)) {

                   //ritorno alla schermata precedente
                   Intent i = new Intent(this, ListaOrdini.class);
                   i.putExtra(TipoExtra.tipoop,TipoOp.DEFAULT);
                   startActivity(i);
               }
           }
           else{
               AlertDialog.Builder builder=new AlertDialog.Builder(this);
               builder.setTitle("Avviso");
               builder.setMessage("Impossibile creare un ordine senza almeno una riga \n Volete Eliminare l'ordine?");
               builder.setCancelable(false);
               builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       if(testata!= null){
                           testata.save();
                           testata.delete();
                       }

                       dialog.dismiss();

                       //ritorno alla schermata precedente
                       Intent i = new Intent(context, ListaOrdini.class);
                       i.putExtra(TipoExtra.tipoop,TipoOp.DEFAULT);
                       startActivity(i);

                   }
               });
               builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });
               builder.create().show();
           }

    }

    public void cercaCliente(View view) {
        if(operazione == TipoOp.OP_INSERISCI) {
            updateAndSave(false);
            testata.save();
            Intent i = new Intent(this, CercaCliente.class);
            i.putExtra(TipoExtra.tipoop, TipoOp.SELEZIONACLIENTE);
            i.putExtra(TipoExtra.tipoopprecedente, operazione);
            i.putExtra(TipoExtra.idordine, testata.getId());
            startActivity(i);
        }
        else{
            Utility.creaDialogoVeloce(context,"Impossibile modificare Cliente per un ordine già inserito","Attenzione").create().show();
        }
    }

    public void nuovoCliente(View view) {
        if(operazione == TipoOp.OP_INSERISCI) {
            updateAndSave(false);
            testata.save();
            Intent i = new Intent(this, InserimentoCliente.class);
            i.putExtra(TipoExtra.tipoop, TipoOp.SELEZIONACLIENTE);
            i.putExtra(TipoExtra.tipoopprecedente, operazione);
            i.putExtra(TipoExtra.idordine, testata.getId());
            startActivity(i);
        }
        else{
            Utility.creaDialogoVeloce(context,"Impossibile modificare Cliente per un ordine già inserito","Attenzione").create().show();
        }
    }

    private boolean updateAndSave(boolean verbose){
        //TODO controllare dimensioni codicecliente
        int codicelenght = 8;

        // faccio il controllo delle date
        sistemaDate();
        if(verbose) {
            if (!controllaDate()) {
                return false;
            }
        }
        if(verbose) {
            //faccio il controllo del cliente
            if (testata.getCodicecliente().equals("") || testata.getCodicecliente().length() > codicelenght) {
                Utility.creaDialogoVeloce(this, "Errore nella scelta del cliente, prego selezionare un cliente", "Testata Ordini, Errore Cliente").create().show();
                return false;
            }
        }

        // prendo i primi sei caratteri dello spinner (codice = 6 caratteri)
        if(((Spinner)findViewById(R.id.spinnerdestinazione)).getSelectedItem() != null){
            String destinazione = ((Spinner)findViewById(R.id.spinnerdestinazione)).getSelectedItem().toString().substring(0,6);
            testata.setDestinazione(destinazione);
        }

        // riempio le altre informazioni
        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegna)).getText().toString();
        String dataordine = ((EditText) findViewById(R.id.edittextdataordine)).getText().toString();
        //TODO impostato a 30 chiedere a maurizio
        String numeroordine = ((EditText) findViewById(R.id.edittextnumeroordine)).getText().toString();
        //TODO impostato a 60 chiedere a maurizio
        String notetestata = ((EditText) findViewById(R.id.edittextnotetestata)).getText().toString();

        testata.setDataconsegna(dataconsegna);
        testata.setDataordine(dataordine);
        testata.setNumeroordine(numeroordine);
        testata.setNotetestata(notetestata);

        //salvo l'ordine
        testata.save();
        return true;
    }

    private boolean controllaDate() {

        String dataconsegna = ((EditText) findViewById(R.id.edittextdataconsegna)).getText().toString();
        String dataordine = ((EditText) findViewById(R.id.edittextdataordine)).getText().toString();
        // faccio il controllo delle date


        SimpleDateFormat FORMATO_STANDARD = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        if (!dataconsegna.isEmpty()){
            try {
                FORMATO_STANDARD.parse(dataconsegna);
            } catch (ParseException e) {
                Utility.creaDialogoVeloce(this, "Errore nella data di consegna, inserire data nel formato gg/MM/YY o gg/MM/YYYY", "Testata Ordini, Errore con le date").create().show();
                return false;
            }
        }
        if (!dataordine.isEmpty()) {
            try {
                FORMATO_STANDARD.parse(dataordine);
            } catch (ParseException e) {
                Utility.creaDialogoVeloce(this, "Errore nella data dell'ordine, inserire data nel formato gg/MM/YY o gg/MM/YYYY", "Testata Ordini, Errore con le date").create().show();
                return false;
            }
            return true;
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

    private void loadField(Cliente myclient){

        if(myclient == null){
            Utility.creaDialogoVeloce(this,"Errore Grave, Rivolgersi all'assistenza","Errore Caricamento Cliente");
            return;
        }

        //inserisco le operazioni legate all'ordine
        if (testata.getDataconsegna() != null)((EditText) findViewById(R.id.edittextdataconsegna)).setText(testata.getDataconsegna());
        if (testata.getDataordine() != null) ((EditText) findViewById(R.id.edittextdataordine)).setText(testata.getDataordine());
        if (testata.getNumeroordine() != null) ((EditText) findViewById(R.id.edittextnumeroordine)).setText(testata.getNumeroordine());
        if (testata.getNotetestata() != null) ((EditText) findViewById(R.id.edittextnotetestata)).setText(testata.getNotetestata());

        //inserisco le operazioni legate al cliente
        if (!myclient.getCodice().equals(""))((TextView) findViewById(R.id.codicecliente)).setText(myclient.getCodice());
        if (!myclient.getDescrizione().equals("") && !myclient.getDescrizione2().equals(""))((TextView) findViewById(R.id.descrizionecliente)).setText(myclient.getDescrizione() + " " + myclient.getDescrizione2());
        if (!myclient.getCitta().equals("") && !myclient.getVia().equals("") && !myclient.getProvincia().equals(""))((TextView) findViewById(R.id.indirizzocliente)).setText(myclient.getVia() + " " + myclient.getCitta() + " " + myclient.getProvincia());
        if (!myclient.getTelefono().equals(""))((TextView) findViewById(R.id.telefono)).setText(myclient.getTelefono());
        if (!myclient.getEmail().equals(""))((TextView) findViewById(R.id.email)).setText(myclient.getEmail());
        if (!myclient.getDescrizionepagamento().equals("") && !myclient.getBanca().equals(""))((TextView) findViewById(R.id.descrizionipagamento)).setText(myclient.getDescrizionepagamento() + " " + myclient.getBanca());
        if (!myclient.getCodice().equals(""))((TextView) findViewById(R.id.codicecliente)).setText(myclient.getCodice());

        //inserisco informazioni nello spinner delle destinazioni
        List <Destinazione> destinazioni = setSpinnerList(myclient.getCodice());

        //prendo la posizione della destinazione dell'operazione per settare lo spinner
        int pos = getDesPosition(destinazioni, testata.getDestinazione());
        Spinner myspinner = (Spinner)findViewById(R.id.spinnerdestinazione);
        myspinner.setSelection(pos);
    }

    public void goBack(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Avviso");
        builder.setMessage("Tornando indietro ogni modifica andrà persa, Continuare?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (operazione == TipoOp.OP_INSERISCI) {
                    // se annullo in inserimento elimino la riga
                    if(testata!= null){
                        testata.save();
                        testata.delete();
                    }
                }
                if (operazione == TipoOp.SCELTOCLIENTE){
                    if(operazioneprecedente == TipoOp.OP_INSERISCI){
                        // se annullo in inserimento elimino la riga
                        testata.delete();
                    }
                    // in caso di modifica torno all'operazione precedente
                }
                //TODO Gestire Annullamenti Errori
                Intent i = new Intent(context, ListaOrdini.class);
                i.putExtra(TipoExtra.tipoop,TipoOp.DEFAULT);
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

    private List<Destinazione> setSpinnerList(String codicecliente){
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
        return destinazioni;
    }

    public int getDesPosition(List<Destinazione> destinazioni, String destinazione){
        for(Destinazione d : destinazioni) {
            if (d.getCodice().equals(destinazione)){
                return destinazioni.indexOf(d);
            }
        }
        return 0;
    }

    public void creaRiga(View view) {
        if(!(testata.getCodicecliente().equals(""))) {
            updateAndSave(false);
            testata.save();
            Intent i = new Intent(this, GestioneRighe.class);
            //se creo una riga gli passo le operazioni e il codice ordine
            i.putExtra(TipoExtra.tipoop, TipoOp.OP_INSERISCI);
            i.putExtra(TipoExtra.tipoopprecedente, operazione);
            i.putExtra(TipoExtra.idordine, testata.getId());
            startActivity(i);
        }
        else{
            Utility.creaDialogoVeloce(context,"Non è possibile inserire una riga senza prima aver selezionato il cliente", "Attenzione").create().show();
        }
    }

    public ListAdapter aggiornaLista(){
        importototale = Float.parseFloat("0");

        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        for(RigaOrdine riga: righeordine){
            HashMap<String, String> elementolista = new HashMap<>();

            Articolo art = Query.getArticolofromCode(riga.getCodicearticolo());
            //TODO mettere qualche informazione in magazzino e ragione sociale
            elementolista.put("IDRIGA", "" + riga.getId());
            elementolista.put("CODICEART", "" + riga.getCodicearticolo());
            elementolista.put("DESCRART", "" + art.getDescrizione());
            elementolista.put("DATAORDINE", "" + riga.getDataconsegna());
            elementolista.put("QUANTITA", "" + riga.getQuantita());
            elementolista.put("PREZZO", "" + riga.getPrezzo());
            data.add(elementolista);

            //quando aggiorno la lista devo aggiornare anche l'importo totale
            importototale += calcolaImportoTotale(riga);
        }

        TextView importototaletv = (TextView) findViewById(R.id.goimportototale);
        importototaletv.setText(importototale.toString());



        String[] from = {"IDRIGA","CODICEART", "DESCRART", "DATAORDINE","QUANTITA", "PREZZO"};
        int[] to = {R.id.idriga, R.id.codicearticolo, R.id.descrizionearticolo, R.id.data,R.id.quantita, R.id.prezzo};
        return new SimpleAdapter(getApplicationContext(), data, R.layout.rigalista,from, to);




    }

    private void mostracampi(boolean mostra){
        int visible;
        if (mostra) visible = View.VISIBLE;
        else visible = View.INVISIBLE;
        findViewById(R.id.tl1).setVisibility(visible);
        findViewById(R.id.tl2).setVisibility(visible);
        findViewById(R.id.tl3).setVisibility(visible);
        findViewById(R.id.tl4).setVisibility(visible);
        findViewById(R.id.tl5).setVisibility(visible);
        findViewById(R.id.tl6).setVisibility(visible);
        findViewById(R.id.tl7).setVisibility(visible);
        findViewById(R.id.tl8).setVisibility(visible);
    }


    private float calcolaImportoTotale(RigaOrdine r){
        Float sconto1;
        Float sconto2;
        Float sconto3;
        Float sconto4;
        Float sconto5;

        //noinspection UnusedAssignment
        Articolo myart = Query.getArticolofromCode(r.getCodicearticolo());

        float quantita;
        float prezzo;

        try {
            quantita = Float.parseFloat(r.getQuantita());
            prezzo = Float.parseFloat(r.getPrezzo());
        }catch (Exception e){
            Utility.creaDialogoVeloce(context, "Riga senza Quantità", "Avviso").create().show();
            return 0;
        }

        // prendo lo sconto dalla riga dell'articolo
        String scontoarticolo = r.getScontoarticolo();
        String scontocliente = r.getScontocliente();
        Float importototale = prezzo * quantita;

        TabellaSconto tabellascontoarticolo = GestioneRighe.getTabellaSconto(scontoarticolo);
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
                return 0;
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
        TabellaSconto tabellascontocliente = GestioneRighe.getTabellaSconto(scontocliente);
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
                return 0;
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
        return importototale;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void onBackPressed() {//ritorno alla schermata precedente
    }

}
