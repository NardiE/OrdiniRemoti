package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.CustomAdapter2;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchArticolo extends AppCompatActivity {
    ArrayList<Articolo> dataModels;
    Context context;
    String param;
    SearchArticolo sa;
    ListView listView;
    private static CustomAdapter2 adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataModels = new ArrayList<>();
        this.context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_articolo);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Ricerca Articoli");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);

        listView = (ListView) findViewById(R.id.articolo_list);

        List<Articolo> articoli = Query.getTop100Items();
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
                Object object= dataModels.get(position);
                Articolo artic=(Articolo)object;
                new AlertDialog.Builder(context)
                        .setTitle(artic.getCodice().toString())
                        .setMessage("Descrizione: " + artic.getDescrizione() + " " + artic.getDescrizione2() + "\n" + "UM: " + artic.getUM() + "\n" + "Esistenza: " + artic.getEsistenza() )
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        adapter.notifyDataSetChanged();

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
            }


        });
    }

    private void onTaskComplete() {
        adapter.notifyDataSetChanged();
    }
}

