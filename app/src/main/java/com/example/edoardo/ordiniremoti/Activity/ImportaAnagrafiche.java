package com.example.edoardo.ordiniremoti.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.classivarie.TipiConfigurazione;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.gestioneerrori.LOGClass;
import com.example.edoardo.ordiniremoti.importazione.AsyncFTPDownloader;
import com.example.edoardo.ordiniremoti.importazione.ImportazioneHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ImportaAnagrafiche extends AppCompatActivity {
    public static Context context;
    public ProgressDialog barProgressDialog = null;
    public final Handler updateBarHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importa_anagrafiche);
        //setto la barra superiore
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // setto il titolo
        getSupportActionBar().setTitle("Ordini Remoti");
        getSupportActionBar().setIcon(R.drawable.logomin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logomin);
        //salvo il contesto
        context = this;

    }

    public void downloadFile(View v){
        //TODO controllare se esterno o interno
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //decido cosa importare
        ArrayList<File> files = new ArrayList<>(10);
        files.add(new File(path, "CLI.txt"));
        files.add(new File(path, "ART.txt"));
        files.add(new File(path, "LIS.txt"));
        files.add(new File(path, "BAR.txt"));
        files.add(new File(path, "DES.txt"));
        files.add(new File(path, "LSC.txt"));
        files.add(new File(path, "TBL.txt"));
        files.add(new File(path, "SCC.txt"));
        files.add(new File(path, "SCG.txt"));
        files.add(new File(path, "SCA.txt"));

        ArrayList<String> filesname = new ArrayList<String>(10);
        filesname.add("CLI.txt");
        filesname.add("ART.txt");
        filesname.add("LIS.txt");
        filesname.add("BAR.txt");
        filesname.add("DES.txt");
        filesname.add("LSC.txt");
        filesname.add("TBL.txt");
        filesname.add("SCC.txt");
        filesname.add("SCG.txt");
        filesname.add("SCA.txt");

        Boolean result = false;
        //TODO impostare parametri in impostazione
        SharedPreferences sharedpreferences = getSharedPreferences(Impostazioni.preferences, Context.MODE_PRIVATE);
        String serverftp = sharedpreferences.getString(TipiConfigurazione.serverftp,"ftp.signorini.it");
        String portaftp = sharedpreferences.getString(TipiConfigurazione.portaftp,"21");
        String nomeutente = sharedpreferences.getString(TipiConfigurazione.nomeutente,"signoriniftp");
        String password = sharedpreferences.getString(TipiConfigurazione.password,"signorini");
        try{
            Integer porta = Integer.parseInt(portaftp);
            AsyncFTPDownloader downloader = new AsyncFTPDownloader(serverftp, porta, nomeutente, password, filesname, files, this);
            downloader.execute();
        }catch (Exception e){
            Toast.makeText(this,"Errore Porta Controlllare Impostazioni",Toast.LENGTH_LONG).show();
        }




        barProgressDialog = new ProgressDialog(ImportaAnagrafiche.this);

        barProgressDialog.setTitle("Download");
        barProgressDialog.setMessage("Sto Scaricando...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(100);
        barProgressDialog.show();
    }

    public void onDownloadComplete(){
        //richiamato una volta effettuato download archivi, chiama Task per importazione nel database di tutte le tabelle necessarie

        barProgressDialog.setProgress(0);
        barProgressDialog.setTitle("Importazione");
        barProgressDialog.setMessage("Sto Importando...");
        startImport();
    }

    public void startImport(){
        //richiamato alla fine di download ed importazione
        Query.cleanUp();
        preparaImportazione("CLI.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("ART.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("LIS.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("BAR.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("DES.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("LSC.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("TBL.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("SCC.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("SCG.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        preparaImportazione("SCA.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        updateBarHandler.post(new Runnable() {

            public void run() {
                barProgressDialog.incrementProgressBy(10);
            }

        });
        findViewById(R.id.downloadend).setVisibility(View.VISIBLE);
        findViewById(R.id.finito).setVisibility(View.VISIBLE);
    }

    public void preparaImportazione(String filename, int max_lenght){
        FileInputStream is;
        BufferedReader reader;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final File file = new File(path,filename);
        int nonimportati = 0;
        if (file.exists()) {
            try {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while (line != null) {
                    if(line.length() >= max_lenght) {
                        switch (filename){
                            case "CLI.TXT":
                                ImportazioneHelper.importaCliente(line);
                                break;
                            case "ART.TXT":
                                ImportazioneHelper.importaArticolo(line);
                                break;
                            case "LIS.TXT":
                                ImportazioneHelper.importaListino(line);
                                break;
                            case "BAR.TXT":
                                ImportazioneHelper.importaBarcode(line);
                                break;
                            case "DES.TXT":
                                ImportazioneHelper.importaDestinazione(line);
                                break;
                            case "LSC.TXT":
                                ImportazioneHelper.importaListinoCliente(line);
                                break;
                            case "TBL.TXT":
                                ImportazioneHelper.importaTabellaSconto(line);
                                break;
                            case "SCC.TXT":
                                ImportazioneHelper.importaScontoC(line);
                                break;
                            case "SCG.TXT":
                                ImportazioneHelper.importaScontoCM(line);
                                break;
                            case "SCA.TXT":
                                ImportazioneHelper.importaScontoCA(line);
                                break;
                        }
                        line = reader.readLine();
                    }else{
                        nonimportati++;
                        line = reader.readLine();
                    }
                }
                if (nonimportati>0){
                    Toast.makeText(this,"Righe di " + filename + " non importate: " + String.valueOf(nonimportati),Toast.LENGTH_LONG).show();
                }
            } catch(Exception ioe){
                Log.e(LOGClass.IMPORTAZIONE, "Errore nell' importazione di " + filename + " " + ioe.toString());
                ioe.printStackTrace();
            }
        }
    }


}
