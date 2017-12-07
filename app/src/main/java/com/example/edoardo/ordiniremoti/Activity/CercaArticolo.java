package com.example.edoardo.ordiniremoti.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.CustomAdapter2;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Query;

import java.util.ArrayList;
import java.util.List;


public class CercaArticolo extends AppCompatActivity {
    ArrayList<Articolo> dataModels;
    Context context;
    String param;
    CercaArticolo sa;
    ListView listView;
    private int operazione;
    private int operazioneprecedente;
    Long idordine;
    Long idriga;
    private static CustomAdapter2 adapter;
    private int preLast;
    static int searchnumber = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataModels = new ArrayList<>();
        this.context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_articolo);

        try{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }catch (Exception e){
        }
        // setto il titolo
        getSupportActionBar().setTitle("Ricerca Articoli");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);


        //controllo se devo filtrare gli ordini o selezionare un cliente
        Bundle extras = getIntent().getExtras();
        operazione = extras.getInt(TipoExtra.tipoop);

        if(operazione == TipoOp.SELEZIONAARTICOLO) {
                // se devo selezionare un cliente ricevo un idOrdine
                operazioneprecedente = extras.getInt(TipoExtra.tipoopprecedente);
                idordine = extras.getLong(TipoExtra.idordine);
                idriga = extras.getLong(TipoExtra.idriga);
            }

            listView = (ListView) findViewById(R.id.articolo_list);
            param = ((EditText)findViewById(R.id.cercaarticolo)).getText().toString();
            List<Articolo> articoli = Query.getTopItemsbyParam(param,searchnumber);
            updateList(articoli);

            // listener per la ricerca veloce
            setEditTextListener();

            adapter = new CustomAdapter2(dataModels, getApplicationContext());

            listView.setAdapter(adapter);

            sa = this;

            //TODO procedura avvio azioni clienti
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = dataModels.get(position);
                    Articolo art = (Articolo) object;
                    if (operazione == TipoOp.SELEZIONAARTICOLO) {
                        Intent i = new Intent(context, GestioneRighe.class);
                        i.putExtra(TipoExtra.tipoop, TipoOp.SCELTOARTICOLO);
                        i.putExtra(TipoExtra.tipoopprecedente, operazioneprecedente);
                        i.putExtra(TipoExtra.idarticolo, art.getId());
                        i.putExtra(TipoExtra.idordine, idordine);
                        i.putExtra(TipoExtra.idriga, idriga);
                        startActivity(i);
                    }

                }
            });

            adapter.notifyDataSetChanged();

        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;


                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        //to avoid multiple calls for last item
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                param = ((EditText)findViewById(R.id.cercaarticolo)).getText().toString();
                                List<Articolo> returnlist = Query.getItemsSearching(param);
                                sa.updateList(returnlist);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sa.onTaskComplete();
                                    }
                                });

                            }
                        };
                        thread.start();
                        view.setSelection(lastItem);
                        preLast = lastItem;
                    }
                }
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
    }

    public void delete(View view) {
        EditText edittext = (EditText) findViewById(R.id.cercaarticolo);
        edittext.setText("");
    }

    public void updateList(List <Articolo> listaclienti){
        dataModels.clear();
        for(Articolo a: listaclienti){
            dataModels.add(a);
        }
    }

    public void setEditTextListener(){
        EditText edittext = (EditText) findViewById(R.id.cercaarticolo);
        edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable editable) {

                param = ((EditText)findViewById(R.id.cercaarticolo)).getText().toString();


                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        List<Articolo> returnlist = Query.getTopItemsbyParam(param,searchnumber);
                        sa.updateList(returnlist);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sa.onTaskComplete();
                            }
                        });

                    }
                };
                thread.start();
            }


        });
    }

    private void onTaskComplete() {
        adapter.notifyDataSetChanged();
    }
}

