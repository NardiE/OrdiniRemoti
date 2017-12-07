package com.example.edoardo.ordiniremoti.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.ClienteNuovo;
import com.example.edoardo.ordiniremoti.database.Progressivo;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.database.RigaOrdine;
import com.example.edoardo.ordiniremoti.database.TestataOrdine;

import java.util.List;

public class InserimentoCliente extends AppCompatActivity {
    Context context;
    Long idordine;
    private int operazione;
    private int operazioneprecedente;
    Cliente cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_cliente);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Nuovo Cliente");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);

        // mi salvo il contesto
        context = this;

        Bundle extras = getIntent().getExtras();

        //riceverò op. op. precedente e id ordine
        operazione = extras.getInt(TipoExtra.tipoop);

        operazioneprecedente = extras.getInt(TipoExtra.tipoopprecedente);

        idordine = extras.getLong(TipoExtra.idordine);

        // mi creo il codice
        TextView codicetw = ((TextView) findViewById(R.id.nccodice));

        // assegno un progressivo negativo dal momento che è un ordine inserito
        int progressivo = Progressivo.getLastProgressivoCliente();
        String codice = Integer.toString(progressivo);
        codice = formattacodice(codice, Cliente.codicelenght);
        codicetw.setText(codice);
        if(!Progressivo.aumentaProgressivoCliente()) Utility.creaDialogoVeloce(context,"Rivolgersi Immediatamente all'assistenza","Errore Grave");

    }


    private boolean updateAndSave(){
        //noinspection UnusedAssignment

        // faccio il controllo delle date

        String codice = ((TextView) findViewById(R.id.nccodice)).getText().toString();
        String descr1 = ((EditText) findViewById(R.id.ncd1)).getText().toString();
        String descr2 = ((EditText) findViewById(R.id.ncd2)).getText().toString();
        String via = ((EditText) findViewById(R.id.ncvia)).getText().toString();
        String citta = ((EditText) findViewById(R.id.nccitta)).getText().toString();
        String cap = ((EditText) findViewById(R.id.nccap)).getText().toString();
        String provincia = ((EditText) findViewById(R.id.ncprovincia)).getText().toString();
        String telefono = ((EditText) findViewById(R.id.nctelefono)).getText().toString();
        String email = ((EditText) findViewById(R.id.ncemail)).getText().toString();
        String piva = ((EditText) findViewById(R.id.ncpiva)).getText().toString();
        String note = ((EditText) findViewById(R.id.ncnote)).getText().toString();

        cliente = new Cliente(codice, descr1, descr2, "", telefono, "", "", via, cap, citta, provincia, "", "", "", email,"","","","","", piva, note);

        cliente.save();

        return true;
    }

    public void confermaCliente(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Avviso");
        builder.setMessage("Una volta inserito un cliente, non è possibile cancellarlo da Tablet. \nVolete procedere comunque?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {        // salvo le info
                updateAndSave();

                // chiamo la gestione degli ordini passandogli id ordine e nuovo cliente
                Intent i = new Intent(context, GestioneOrdini.class);
                i.putExtra(TipoExtra.tipoop,TipoOp.SCELTOCLIENTE);
                i.putExtra(TipoExtra.tipoopprecedente,operazioneprecedente);
                i.putExtra(TipoExtra.idcliente,cliente.getId());
                i.putExtra(TipoExtra.idordine,idordine);
                startActivity(i);
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

    public String formattacodice(String s, int lenght){
        while (s.length() < lenght - 1){
            s = "0" + s;
        }
        s = "$" + s;
        return s;
    }
}
