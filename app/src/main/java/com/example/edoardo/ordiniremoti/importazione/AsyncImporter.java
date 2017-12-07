package com.example.edoardo.ordiniremoti.importazione;

import android.os.AsyncTask;

import com.example.edoardo.ordiniremoti.Activity.ImportaAnagrafiche;
import com.example.edoardo.ordiniremoti.Activity.Sincronizza;
import com.example.edoardo.ordiniremoti.database.Query;

import static com.example.edoardo.ordiniremoti.database.Query.cleanUp;


/**
 * Created by edoardo on 21/12/16.
 */
public class AsyncImporter extends AsyncTask<String, Void, Void> {
// downloadAndSaveFile(String server, int portNumber,String user, String password, String filename, File localFile)
    private ImportaAnagrafiche sincronizza;



    public AsyncImporter(ImportaAnagrafiche sincronizza) {
        this.sincronizza = sincronizza;
    }

    @Override
    protected Void doInBackground(String... params) {
        Query.cleanUp();
        sincronizza.preparaImportazione("CLI.TXT", ImportazioneHelper.CLIENTE_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("ART.TXT", ImportazioneHelper.ARTICOLO_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("LIS.TXT", ImportazioneHelper.LISTINO_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("BAR.TXT", ImportazioneHelper.BARCODE_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("DES.TXT", ImportazioneHelper.DESTINAZIONE_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("LSC.TXT", ImportazioneHelper.LISTINOCLIENTE_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("TBL.TXT", ImportazioneHelper.TABELLASCONTO_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("SCC.TXT", ImportazioneHelper.SCONTOC_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("SCG.TXT", ImportazioneHelper.SCONTOCM_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        sincronizza.preparaImportazione("SCA.TXT", ImportazioneHelper.SCONTOCA_DIMENSIONESTRINGA);
        sincronizza.updateBarHandler.post(new Runnable() {

            public void run() {

                sincronizza.barProgressDialog.incrementProgressBy(10);

            }

        });
        return null;
    }

    protected void onPostExecute(Void unused) {

        sincronizza.onTaskComplete();
    }
}
