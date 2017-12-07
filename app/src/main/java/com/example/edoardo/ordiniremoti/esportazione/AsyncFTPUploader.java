package com.example.edoardo.ordiniremoti.esportazione;

import android.os.AsyncTask;
import android.util.Log;

import com.example.edoardo.ordiniremoti.Activity.EsportaOrdini;
import com.example.edoardo.ordiniremoti.Activity.ImportaAnagrafiche;
import com.example.edoardo.ordiniremoti.gestioneerrori.LOGClass;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edoardo on 21/12/16.
 */
public class AsyncFTPUploader extends AsyncTask<String, Void, Void> {
// downloadAndSaveFile(String server, int portNumber,String user, String password, String filename, File localFile)
    private String server;
    private int portNumber;
    private String user;
    private String password;
    private ArrayList<String> filenames;
    private ArrayList<File> localFiles;
    private ArrayList<Boolean> success = new ArrayList<Boolean>();
    private EsportaOrdini sincronizza;
    boolean result;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(ArrayList<String> filenames) {
        this.filenames = filenames;
    }

    public List<File> getLocalFiles() {
        return localFiles;
    }

    public void setLocalFiles(ArrayList<File> localFiles) {
        this.localFiles = localFiles;
    }

    public AsyncFTPUploader(String server, int portNumber, String user, String password, ArrayList<String> filenames, ArrayList<File> localFiles, EsportaOrdini sincronizza) {
        this.server = server;
        this.portNumber = portNumber;
        this.user = user;
        this.password = password;
        this.filenames = filenames;
        this.localFiles = localFiles;
        this.sincronizza = sincronizza;
    }

    @Override
    protected Void doInBackground(String... params) {
        result = true;
        //noinspection UnusedAssignment
        FTPClient con = null;
        try
        {
            con = new FTPClient();
            con.connect(server);

            if (con.login(user, password))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.ASCII_FILE_TYPE);
                for(int i = 0 ; i<filenames.size() ; i++) {
                    FileInputStream in = new FileInputStream(localFiles.get(i));
                    result = con.storeFile(filenames.get(i), in);
                    in.close();
                }
                if (result) Log.v("upload result", "succeeded");
                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void unused) {
        sincronizza.onUploadComplete(result);
    }
}
