package com.example.edoardo.ordiniremoti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.edoardo.ordiniremoti.database.Articolo;

import java.util.ArrayList;

/**
 * Created by Edoardo on 18/05/2017.
 */


    public class CustomAdapter2 extends ArrayAdapter<Articolo> implements View.OnClickListener{

        private ArrayList<Articolo> dataSet;
        Context mContext;



        public CustomAdapter2(ArrayList<Articolo> data, Context context) {
            super(context, R.layout.articolo_list_elem, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            Articolo artic=(Articolo)object;
            //TODO ON Click
            new AlertDialog.Builder(mContext)
                    .setTitle(artic.getCodice().toString())
                    .setMessage("Descrizione: " + artic.getDescrizione() + " " + artic.getDescrizione2() + "\n" + "UM: " + artic.getUM() + "\n" + "Esistenza: " + artic.getEsistenza() )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            /*Intent i = new Intent(v.getContext(),AzioniCliente.class);
            v.getContext().startActivity(i);*/

            /*switch (v.getId())
            {
                case R.id.item_info:
                    Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
            }*/
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            // Get the data item for this position
            Articolo artic = getItem(position);
            if (v==null)
            {
                v=LayoutInflater.from(mContext).inflate(R.layout.client_list_elem, null);
            }
            Articolo at=(Articolo) getItem(position);
            TextView codice=(TextView) v.findViewById(R.id.codice);
            TextView descrizione=(TextView) v.findViewById(R.id.descrizione);
            TextView indirizzo=(TextView) v.findViewById(R.id.indirizzo);
            codice.setText(at.getCodice());
            descrizione.setText(at.getDescrizione());
            indirizzo.setText((at.getEsistenza()).toString());
            return v;
        }
    }

