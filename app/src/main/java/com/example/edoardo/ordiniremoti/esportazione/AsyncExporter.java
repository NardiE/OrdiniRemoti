package com.example.edoardo.ordiniremoti.esportazione;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.edoardo.ordiniremoti.Activity.EsportaOrdini;
import com.example.edoardo.ordiniremoti.Activity.ImportaAnagrafiche;
import com.example.edoardo.ordiniremoti.Utility.Utility;
import com.example.edoardo.ordiniremoti.database.Cliente;
import com.example.edoardo.ordiniremoti.database.Query;
import com.example.edoardo.ordiniremoti.database.RigaOrdine;
import com.example.edoardo.ordiniremoti.database.TestataOrdine;
import com.example.edoardo.ordiniremoti.importazione.ImportazioneHelper;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;


/**
 * Created by edoardo on 21/12/16.
 */
public class AsyncExporter extends AsyncTask<String, Void, Void> {
// downloadAndSaveFile(String server, int portNumber,String user, String password, String filename, File localFile)
    private EsportaOrdini esporta;
    boolean result;



    public AsyncExporter(EsportaOrdini esporta) {
        this.esporta = esporta;
    }

    @Override
    protected Void doInBackground(String... params) {

        result = true;

        //creo il file degli ordini
        BufferedWriter ordwriter;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final File fileord = new File(path,"FROMNOC.txt");

        try{
            ordwriter = new BufferedWriter(new FileWriter(fileord));
            //esporto ordini
            List<RigaOrdine> righe = Select.from(RigaOrdine.class).list();

            for(RigaOrdine r : righe){
                String linea = AsyncExporter.CreaOrdineLinea(r);
                ordwriter.write(linea);
            }
            ordwriter.flush();
            ordwriter.close();

            // non riuscivo a copiare su ftp :::> risolto copiando il file e lanciando il media scanner
            final File filedest = new File(path,"NOC.txt");
            copyDirectoryOneLocationToAnotherLocation(fileord,filedest);
            MediaScannerConnection.scanFile(esporta, new String[]{fileord.getAbsolutePath()}, null, null);
        }catch (Exception e){
            result = false;
        }


        esporta.updateBarHandler.post(new Runnable() {

            public void run() {

                esporta.barProgressDialog.incrementProgressBy(50);

            }

        });

        // creo il file dei contatti
        BufferedWriter ordcliwriter;
        File pathcli = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final File filecli = new File(pathcli,"FROMNCN.txt");

        try{
            ordcliwriter = new BufferedWriter(new FileWriter(filecli));
            //esporto contatti
            List<Cliente> clients = Query.getNuoviClienti();

            for(Cliente c : clients){
                String linea = AsyncExporter.creaLineaCliente(c);
                ordcliwriter.write(linea);
                //c.delete();
            }
            ordcliwriter.flush();
            ordcliwriter.close();
            // non riuscivo a copiare su ftp :::> risolto copiando il file e lanciando il media scanner
            final File filedestcli = new File(path,"NCN.txt");
            copyDirectoryOneLocationToAnotherLocation(filecli,filedestcli);
            MediaScannerConnection.scanFile(esporta, new String[]{filecli.getAbsolutePath()}, null, null);
        }catch (Exception e){
            result = false;
        }
        //esporto contatti
        esporta.updateBarHandler.post(new Runnable() {

            public void run() {

                esporta.barProgressDialog.incrementProgressBy(50);

            }

        });

        //Query.softCleanUp();
        return null;
    }

    protected void onPostExecute(Void unused) {

        esporta.onTaskComplete(result);
    }

    private static String CreaOrdineLinea(RigaOrdine r){
        TestataOrdine t = Query.getTestata(r.getProgressivoordine());
        String line = "";
        line = line + t.getProgressivo();
        line = line + ";";
        line = line + t.getCodicecliente();
        line = line + ";";
        line = line + t.getDestinazione();
        line = line + ";";
        line = line + t.getDataconsegna();
        line = line + ";";
        line = line + t.getDataordine();
        line = line + ";";
        line = line + t.getNumeroordine();
        line = line + ";";
        line = line + t.getNotetestata();
        line = line + ";";
        line = line + r.getProgressivo();
        line = line + ";";
        line = line + r.getCodicearticolo();
        line = line + ";";
        line = line + r.getQuantita();
        line = line + ";";
        line = line + r.getPrezzo();
        line = line + ";";
        line = line + r.getScontocliente();
        line = line + ";";
        line = line + r.getScontoarticolo();
        line = line + ";";
        line = line + r.getDataconsegna();
        line = line + ";";
        line = line + r.getNoteriga();
        line = line + ";";
        line = line + r.getTiporiga();
        line = line + "\n";
        return line;
    }
    private static String creaLineaCliente(Cliente c){
        String line = "";
        line = line + c.getCodice();
        line = line + ";";
        line = line + c.getDescrizione();
        line = line + ";";
        line = line + c.getDescrizione2();
        line = line + ";";
        line = line + c.getVia();
        line = line + ";";
        line = line + c.getCitta();
        line = line + ";";
        line = line + c.getCap();
        line = line + ";";
        line = line + c.getProvincia();
        line = line + ";";
        line = line + c.getTelefono();
        line = line + ";";
        line = line + c.getPartitaiva();
        line = line + ";";
        line = line + c.getEmail();
        line = line + ";";
        line = line + c.getNote();
        line = line + "\n";
        return line;
    }

    public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }

}
