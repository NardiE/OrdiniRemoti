package com.example.edoardo.ordiniremoti.Activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchClient extends AppCompatActivity {
    ArrayList<Cliente> dataModels;
    Context context;
    Long idordine;
    ListView listView;
    String param;
    SearchClient sc;
    private int operazione;
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

        //controllo se devo filtrare gli ordini o selezionare un cliente
        Bundle extras = getIntent().getExtras();
        operazione = extras.getInt(TipoExtra.tipoop);

        if(operazione == TipoOp.SELEZIONACLIENTE){
            // se devo selezionare un cliente ricevo un idOrdine
            idordine = extras.getLong(TipoExtra.idordine);
        }

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
                if(operazione == TipoOp.SELEZIONACLIENTE){
                    Intent i = new Intent(context, GestioneOrdini.class);
                    i.putExtra(TipoExtra.tipoop,TipoOp.SCELTOCLIENTE);
                    i.putExtra(TipoExtra.idcliente,client.getId());
                    i.putExtra(TipoExtra.idordine,idordine);
                    startActivity(i);
                }
                else{

                }
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

