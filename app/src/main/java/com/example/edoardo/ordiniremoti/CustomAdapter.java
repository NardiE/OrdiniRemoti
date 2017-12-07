package com.example.edoardo.ordiniremoti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.edoardo.ordiniremoti.database.Cliente;

import java.util.ArrayList;

/**
 * Created by Edoardo on 18/05/2017.
 */


    public class CustomAdapter extends ArrayAdapter<Cliente> implements View.OnClickListener{

        private ArrayList<Cliente> dataSet;
        Context mContext;



        public CustomAdapter(ArrayList<Cliente> data, Context context) {
            super(context, R.layout.client_list_elem, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            Cliente client=(Cliente)object;

            new AlertDialog.Builder(mContext)
                    .setTitle(client.getCodice().toString())
                    .setMessage("Descrizione: " + client.getDescrizione() + " " + client.getDescrizione2() + "\n" + "Recapito: " + client.getVia()+ " " + client.getCitta()+ " " + client.getProvincia()+ "\n" + "Telefono: " + client.getTelefono() )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            //TODO ON Click
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
            //noinspection UnusedAssignment
            Cliente client = getItem(position);
            if (v==null)
            {
                v=LayoutInflater.from(mContext).inflate(R.layout.client_list_elem, null);
            }
            Cliente cl=(Cliente) getItem(position);
            TextView codice=(TextView) v.findViewById(R.id.codice);
            TextView descrizione=(TextView) v.findViewById(R.id.descrizione);
            TextView indirizzo=(TextView) v.findViewById(R.id.indirizzo);
            codice.setText(cl.getCodice());
            descrizione.setText(cl.getDescrizione());
            indirizzo.setText(cl.getVia() + " " + cl.getCitta() + " " + cl.getProvincia() + " " + cl.getCap());
            return v;
        }
    }

