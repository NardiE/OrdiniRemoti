package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.edoardo.ordiniremoti.R;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.edoardo.ordiniremoti.*;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchClient extends AppCompatActivity {
    ArrayList<Cliente> dataModels;
    Context context;
    ListView listView;
    String param;
    SearchClient sc;
    private static CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataModels = new ArrayList<>();
        this.context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_cliente);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Ricerca Clienti");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);

        listView = (ListView) findViewById(R.id.client_list);

        List<Cliente> clienti = Query.getTop100Clienti();
        updateList(clienti);

        // listener per la ricerca veloce
        setEditTextListener();



        adapter = new CustomAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);

        sc = this;

        //TODO procedura avvio azioni clienti
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object= dataModels.get(position);
                Cliente client=(Cliente)object;

                new AlertDialog.Builder(context)
                        .setTitle(client.getCodice().toString())
                        .setMessage("Descrizione: " + client.getDescrizione() + " " + client.getDescrizione2() + "\n" + "Recapito: " + client.getVia()+ " " + client.getCitta()+ " " + client.getProvincia()+ "\n" + "Telefono: " + client.getTelefono() )
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
        EditText edittext = (EditText) findViewById(R.id.cercacliente);
        edittext.setText("");
    }

    public void updateList(List <Cliente> listaclienti){
        dataModels.clear();
        for(Cliente c: listaclienti){
            dataModels.add(c);
        }
    }

    public void setEditTextListener(){
        EditText edittext = (EditText) findViewById(R.id.cercacliente);
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
                param = ((EditText)findViewById(R.id.cercacliente)).getText().toString();


                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        List<Cliente> returnlist = Query.getClientsSearching(param);
                        sc.updateList(returnlist);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sc.onTaskComplete();
                            }
                        });

                    }
                };
                thread.start();


            }


        });
    }

    public void onTaskComplete(){
        adapter.notifyDataSetChanged();
    }
}

