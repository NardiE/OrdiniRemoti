package com.example.edoardo.ordiniremoti.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.edoardo.ordiniremoti.R;
import com.example.edoardo.ordiniremoti.classivarie.TipiConfigurazione;
import com.example.edoardo.ordiniremoti.importazione.AsyncFTPDownloader;

import java.io.File;
import java.util.ArrayList;

public class Sincronizza extends AppCompatActivity {
    public Context context;
    public ProgressDialog barProgressDialog = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizza);
    }

    public void importaAnagrafiche(View view) {
        Intent i = new Intent(this, ImportaAnagrafiche.class);
        startActivity(i);
    }

    public void esportaOrdini(View view) {
        Intent i = new Intent(this,EsportaOrdini.class);
        startActivity(i);
    }


}
