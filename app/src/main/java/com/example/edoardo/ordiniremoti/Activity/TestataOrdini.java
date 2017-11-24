package com.example.edoardo.ordiniremoti.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Progressivo;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;

import static android.support.design.R.styleable.Spinner;

public class TestataOrdini extends AppCompatActivity {
    int progressivo;
    Cliente cliente;
    ArrayList<Articolo> righeordine = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testata_ordini);

        //controllo se è una modifica o un inserimento
        Bundle extras = getIntent().getExtras();
        int tipoop = extras.getInt(TipoExtra.tipoop);

        if(tipoop == TipoOp.OP_INSERISCI) {
            // se faccio un inserimento mi assegno un progressivo negativo
           int progressivo = Progressivo.getLastProgressivoOrdine();
            if(progressivo > 0){
                // se inserisco non posso avere un progressivo > 0
                Utility.creaDialogoVeloce(this, "Errore, Ripristinare il Database", "Errore in assegnazione Progressivo").create().show();
            }
            else {
                this.progressivo = progressivo;
            }

        }

        if(tipoop == TipoOp.OP_MODIFICA){
            //TODO è una modifica
            int numero_ordine = extras.getInt(TipoExtra.numeroordine);
            String codicecliente = extras.getString(TipoExtra.codicecliente);
            String destinazione = extras.getString(TipoExtra.destinazione);



        }


    }


}
