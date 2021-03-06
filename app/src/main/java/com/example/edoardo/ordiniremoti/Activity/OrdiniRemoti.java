package com.example.edoardo.ordiniremoti.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.classivarie.TipiConfigurazione;
import com.example.edoardo.ordiniremoti.classivarie.TipoExtra;
import com.example.edoardo.ordiniremoti.classivarie.TipoOp;
import com.example.edoardo.ordiniremoti.database.Articolo;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.importazione.ImportazioneHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class OrdiniRemoti extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordini_remoti);

        context = this;

        //chiedo i permessi
        getPermission();

        //Query.cleanUp();
        //Query.insertSample();

        //copio db
        backupDb();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Ordini Remoti");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);


        // controllo che le impostazioni siano inserite
        SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);

        String serverftp = sharedpreferences.getString(TipiConfigurazione.serverftp, "errore");
        String portaftp = sharedpreferences.getString(TipiConfigurazione.portaftp, "errore");
        String nomeutente = sharedpreferences.getString(TipiConfigurazione.nomeutente, "errore");
        String password = sharedpreferences.getString(TipiConfigurazione.password,"errore");
        String listino = sharedpreferences.getString(TipiConfigurazione.listinodefault,"errore");

        String nuovocliente = sharedpreferences.getString(TipiConfigurazione.nuovocliente,"errore");
        if(nuovocliente.equals("errore") || serverftp.equals("errore") || portaftp.equals("errore") || nomeutente.equals("errore") || password.equals("errore") || listino.equals("errore")){
            Intent i = new Intent(this,Impostazioni.class);
            startActivity(i);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.impostazioni) {
            avviaImpostazioni();
            return true;
        }
        if (id == R.id.info){
            AlertDialog.Builder myb = Utility.creaDialogoVeloce(this, "Versione 1.0 \n\n Sviluppato da Signorini & C. SRL \n\n Per informazioni contattare: edoardo@signorini.it", "Informazioni");
            myb.create().show();
        }

        if (id == R.id.clear_database){

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Avviso");
            builder.setMessage("La procedura elimina qualsiasi dato dal database e va usata solo in caso di malfunzionamenti. Al termine del ripristino sarà necessario lanciare l'allineamento clienti. Procedere?");
            builder.setCancelable(false);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Query.cleanUp();
                    //TODO togliere una volta finito
                    Query.insertSample();
                    dialog.dismiss();
                    return;
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    return;
                }
            });

            builder.create().show();


        }


        return super.onOptionsItemSelected(item);
    }

    public void sincronizza(View v){
        Intent i = new Intent(this,Sincronizza.class);
        startActivity(i);
    }

    public void backupDb(){
        try {
            String state = Environment.getExternalStorageState();
            File sd = Environment.getExternalStorageDirectory();
            //noinspection UnusedAssignment
            File data = Environment.getDataDirectory();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Log.d("Test", "sdcard mounted and writable");
            }
            else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.d("Test", "sdcard mounted readonly");
            }
            else {
                Log.d("Test", "sdcard state: " + state);
            }
            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/ordiniremoti.db";
                String backupDBPath = "ordiniremoti.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
        }
    }
    public void getPermission(){
        //chiedo permesso scrivere su sd
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
    }

    public void iniziaGestioneOrdini(View view) {
        Intent i = new Intent(this,ListaOrdini.class);
        i.putExtra(TipoExtra.tipoop, TipoOp.DEFAULT);
        startActivity(i);
    }

    //alertbuilder con password per impostazioni
    public void avviaImpostazioni() {
        //noinspection UnusedAssignment
        boolean result = false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.customdialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edittextpassword);

        dialogBuilder.setTitle("Inserisci Password");
        dialogBuilder.setMessage("Se non si conosce la password, contattare l'assistenza");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                if( (edt.getText().toString()).equals(getResources().getString(R.string.password))){
                    Intent i = new Intent(context, Impostazioni.class);
                    startActivity(i);
                }
                else{
                    Utility.creaDialogoVeloce(context, "Password Errata", "Errore").create().show();
                }
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onBackPressed() {
    }
}
