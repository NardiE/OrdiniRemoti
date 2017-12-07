package com.example.edoardo.ordiniremoti.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.classivarie.TipiConfigurazione;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.esportazione.AsyncExporter;
import com.example.edoardo.ordiniremoti.esportazione.AsyncFTPUploader;
import com.example.edoardo.ordiniremoti.gestioneerrori.LOGClass;
import com.example.edoardo.ordiniremoti.importazione.AsyncFTPDownloader;
import com.example.edoardo.ordiniremoti.importazione.AsyncImporter;
import com.example.edoardo.ordiniremoti.importazione.ImportazioneHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EsportaOrdini extends AppCompatActivity {

    public ProgressDialog barProgressDialog = null;
    public final Handler updateBarHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esporta_ordini);
    }

    public void onTaskComplete(boolean result) {
        if(result) {
            barProgressDialog.setProgress(0);
            barProgressDialog.setTitle("Upload");
            barProgressDialog.setMessage("Sto eseguendo l'upload...");

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            //decido cosa importare
            ArrayList<File> files = new ArrayList<>(10);
            files.add(new File(path, "NOC.txt"));
            files.add(new File(path, "NCN.txt"));

            ArrayList<String> filesname = new ArrayList<String>(10);
            filesname.add("ordini.txt");
            filesname.add("contatti.txt");

            SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);
            String serverftp = sharedpreferences.getString(TipiConfigurazione.serverftp,"ftp.signorini.it");
            String portaftp = sharedpreferences.getString(TipiConfigurazione.portaftp,"21");
            String nomeutente = sharedpreferences.getString(TipiConfigurazione.nomeutente,"signoriniftp");
            String password = sharedpreferences.getString(TipiConfigurazione.password,"signorini");
            try{
                Integer porta = Integer.parseInt(portaftp);
                AsyncFTPUploader downloader = new AsyncFTPUploader(serverftp, porta, nomeutente, password, filesname, files, this);
                downloader.execute();
            }catch (Exception e){
                Toast.makeText(this,"Errore Porta Controlllare Impostazioni",Toast.LENGTH_LONG).show();
            }
        }
        else{
            barProgressDialog.dismiss();
            TextView finito = (TextView)findViewById(R.id.uploadend);
            finito.setText("Errore, controllare la connessione");
            finito.setTextColor(Color.RED);
            finito.setVisibility(View.VISIBLE);
            findViewById(R.id.finitoupload).setBackgroundResource(R.drawable.no);
        }
    }

    public void onUploadComplete(boolean result){
        if(result) {
            TextView finito = (TextView)findViewById(R.id.uploadend);
            //richiamato alla fine di download ed importazione
            finito.setText("OK!");
            findViewById(R.id.uploadend).setVisibility(View.VISIBLE);
            findViewById(R.id.finitoupload).setVisibility(View.VISIBLE);
            barProgressDialog.dismiss();
        }
        else{
            barProgressDialog.dismiss();
            TextView finito = (TextView)findViewById(R.id.uploadend);
            finito.setText("Errore, controllare la connessione");
            finito.setTextColor(Color.RED);
            finito.setVisibility(View.VISIBLE);
        }
        // cancello i files
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final File file = new File(path,"FROMNOC.txt");
        file.delete();
        final File file1 = new File(path,"FROMNCN.txt");
        file1.delete();
        //noinspection UnusedAssignment
        final File file2 = new File(path,"NCN.txt");
        file1.delete();
        //noinspection UnusedAssignment
        final File file3 = new File(path,"NCN.txt");
        file1.delete();

        // cancello ordini e clienti dal database
        Query.softCleanUp();
    }

    public void uploadFile(View view) {
        try{
            AsyncExporter exporter = new AsyncExporter(this);
            exporter.execute();
        }catch (Exception e){
            Toast.makeText(this,"Errore Esportazione, Riprovare",Toast.LENGTH_LONG).show();
        }

        barProgressDialog = new ProgressDialog(EsportaOrdini.this);

        barProgressDialog.setTitle("Esportazione");
        barProgressDialog.setMessage("Sto Esportando...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(100);
        barProgressDialog.show();
    }

    public void returntoMain(View view) {
        Intent i = new Intent(this, OrdiniRemoti.class);
        startActivity(i);
    }
}
