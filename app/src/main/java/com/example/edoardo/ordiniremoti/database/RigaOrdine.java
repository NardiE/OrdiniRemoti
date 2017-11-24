package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

public class RigaOrdine extends SugarRecord<RigaOrdine> {
    // codice, descrizione, descrizione2, UM, esistenza, numeropezzi
    private int progressivoordine;
    private String codicearticolo;
    private String quantita;
    private String prezzo;
    private String scontocliente;
    private String scontoarticolo;
    private String dataconsegna;
    private String noteriga;

    public RigaOrdine(){
    }

    public RigaOrdine(int progressivoordine, String codicearticolo, String quantita, String prezzo, String scontocliente, String scontoarticolo, String dataconsegna, String noteriga) {
        this.progressivoordine = progressivoordine;
        this.codicearticolo = codicearticolo;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.scontocliente = scontocliente;
        this.scontoarticolo = scontoarticolo;
        this.dataconsegna = dataconsegna;
        this.noteriga = noteriga;
    }

    public int getProgressivoordine() {
        return progressivoordine;
    }

    public void setProgressivoordine(int progressivoordine) {
        this.progressivoordine = progressivoordine;
    }

    public String getCodicearticolo() {
        return codicearticolo;
    }

    public void setCodicearticolo(String codicearticolo) {
        this.codicearticolo = codicearticolo;
    }

    public String getQuantita() {
        return quantita;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getScontocliente() {
        return scontocliente;
    }

    public void setScontocliente(String scontocliente) {
        this.scontocliente = scontocliente;
    }

    public String getScontoarticolo() {
        return scontoarticolo;
    }

    public void setScontoarticolo(String scontoarticolo) {
        this.scontoarticolo = scontoarticolo;
    }

    public String getDataconsegna() {
        return dataconsegna;
    }

    public void setDataconsegna(String dataconsegna) {
        this.dataconsegna = dataconsegna;
    }

    public String getNoteriga() {
        return noteriga;
    }

    public void setNoteriga(String noteriga) {
        this.noteriga = noteriga;
    }
}