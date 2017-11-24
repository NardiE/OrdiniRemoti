package com.example.edoardo.ordiniremoti.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;

public class ListaOrdini extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ordini);
    }

    public void inserisciordine(View view) {
        Intent i = new Intent(this,TestataOrdini.class);
        i.putExtra(TipoExtra.tipoop, TipoOp.OP_INSERISCI);
        startActivity(i);
    }
}
