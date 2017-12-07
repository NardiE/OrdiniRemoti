package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.database.RigaOrdine;
import com.example.edoardo.ordiniremoti.database.TestataOrdine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ListaOrdini extends AppCompatActivity {
    List<TestataOrdine> testate = new ArrayList<>();
    String codicecliente;
    int operazione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ordini);

        Bundle extras = getIntent().getExtras();
        operazione = extras.getInt(TipoExtra.tipoop);


        if(operazione == TipoOp.SCELTOCLIENTE){
            //filtro per cliente
            codicecliente = extras.getString(TipoExtra.codicecliente);
            Long idcliente = extras.getLong(TipoExtra.idcliente);
            Cliente c = Cliente.findById(Cliente.class, idcliente);
            if(codicecliente != null) testate = Query.getOrdiniFromClient(codicecliente);
            //aggiorno i campi codice e descrizione
            compilaCampi(c.getCodice(),c.getDescrizione() + " " + c.getDescrizione2());
        }
        else {
            //scarico tutti gli ordini
            testate = TestataOrdine.listAll(TestataOrdine.class);
        }

        // imposto l'adapter per la lista
        ListView listaordini = ((ListView) findViewById(R.id.listviewordini));

        ListAdapter simpleAdapter = aggiornaLista();

        listaordini.setAdapter(simpleAdapter);

        registerForContextMenu(listaordini);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        // il primo elemento è la descrizione
        menu.setHeaderTitle(testate.get(info.position).getCodicecliente() + " " + testate.get(info.position).getProgressivo() + " " + testate.get(info.position).getDataordine() );
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
        ID = Long.parseLong(transf.get("IDORDINE"));
        final TestataOrdine t = TestataOrdine.findById(TestataOrdine.class, ID);


        switch(menuItemIndex) {
            case 0:
                //modifica
                Intent i = new Intent(this,GestioneOrdini.class);
                i.putExtra(TipoExtra.tipoop, TipoOp.OP_MODIFICA);
                i.putExtra(TipoExtra.idordine, ID);
                startActivity(i);
                return true;
            case 1:
                //Elimina
                if(t.getProgressivo()>=0){
                    Toast.makeText(this, "Non è possibile eliminare Ordini non generati da Tablet", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Avviso");
                    builder.setMessage("L'Eliminazione è irreversibile, Continuare?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            t.delete();

                            //elimino anche tutte le righe asccociate a quell'ordine
                            List<RigaOrdine> righeordine = Query.getAllRigheFromProgressivoOrdine(t.getProgressivo());
                            for(RigaOrdine r : righeordine){
                                r.delete();
                            }

                            //TODO Eliminare anche tutte le righe
                            if(operazione == TipoOp.SCELTOCLIENTE && codicecliente != null){
                                // se ho scelto il cliente aggiorno le testate di quel cliente
                                testate = Query.getOrdiniFromClient(codicecliente);
                            }
                            else {
                                //scarico tutti gli ordini
                                testate = TestataOrdine.listAll(TestataOrdine.class);
                            }

                            ListView listaordini = ((ListView) findViewById(R.id.listviewordini));
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

    public void inserisciordine(View view) {
        Intent i = new Intent(this,GestioneOrdini.class);
        i.putExtra(TipoExtra.tipoop, TipoOp.OP_INSERISCI);
        startActivity(i);
    }


    public ListAdapter aggiornaLista(){

        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        for(TestataOrdine testata: testate){
            HashMap<String, String> elementolista = new HashMap<>();
            Cliente c = Query.getClientefromCode(testata.getCodicecliente());
            String descrizione = "";
            if(c != null){
                descrizione = c.getDescrizione();
            }
            //TODO mettere qualche informazione in magazzino e ragione sociale
            elementolista.put("IDORDINE", "" + testata.getId());
            elementolista.put("CODICECLIENTE", "" + testata.getCodicecliente());
            elementolista.put("RAGIONESOCIALE", "" + descrizione);
            elementolista.put("NUMEROORDINE", "" + testata.getNumeroordine());
            elementolista.put("DATAORDINE", "" + testata.getDataordine());
            elementolista.put("MAGAZZINO", "");
            data.add(elementolista);
        }

        String[] from = {"IDORDINE","CODICECLIENTE", "RAGIONESOCIALE", "NUMEROORDINE", "DATAORDINE", "MAGAZZINO"};
        int[] to = {R.id.listaordiniidordine, R.id.listaordinicodicecliente, R.id.listaordiniragionesociale, R.id.listaordininumeroordine, R.id.listaordinidataordine, R.id.listaordinimagazzino};
        return new SimpleAdapter(getApplicationContext(), data, R.layout.ordinerigalista,from, to);
    }


    public void filtra(View view) {
        Intent i = new Intent(this, CercaCliente.class);
        i.putExtra(TipoExtra.tipoop,TipoOp.FILTRACLIENTE);
        startActivity(i);
    }

    public void rimuoviFiltri(View view) {
        testate = TestataOrdine.listAll(TestataOrdine.class);
        // imposto l'adapter per la lista
        ListView listaordini = ((ListView) findViewById(R.id.listviewordini));

        ListAdapter simpleAdapter = aggiornaLista();

        listaordini.setAdapter(simpleAdapter);

        compilaCampi("Tutti","Tutti");

    }

    private void compilaCampi(String codice, String ragionesociale){
        TextView codicetw = (TextView) findViewById(R.id.codiceclienteselezionato);
        TextView ragionetw = (TextView) findViewById(R.id.descrizioneclienteselezionato);

        codicetw.setText(codice);
        ragionetw.setText(ragionesociale);
    }

    public void goBack(View view) {
        Intent i = new Intent(this, OrdiniRemoti.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
    }

}
