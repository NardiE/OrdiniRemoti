package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

public class RigaOrdine extends SugarRecord<RigaOrdine> {
    // codice, descrizione, descrizione2, UM, esistenza, numeropezzi
    private int progressivo;
    private int progressivoordine;
    private String codicearticolo;
    private String quantita;
    private String prezzo;
    private String scontocliente;
    private String scontoarticolo;
    private String dataconsegna;
    private String noteriga;
    private String tiporiga;

    public RigaOrdine() {
    }

    public RigaOrdine(int progressivo, int progressivoordine, String codicearticolo, String quantita, String prezzo, String scontocliente, String scontoarticolo, String dataconsegna, String noteriga, String tiporiga) {
        this.progressivo = progressivo;
        this.progressivoordine = progressivoordine;
        this.codicearticolo = codicearticolo;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.scontocliente = scontocliente;
        this.scontoarticolo = scontoarticolo;
        this.dataconsegna = dataconsegna;
        this.noteriga = noteriga;
        this.tiporiga = tiporiga;
    }

    public int getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(int progressivo) {
        this.progressivo = progressivo;
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

    public String getTiporiga() {
        return tiporiga;
    }

    public void setTiporiga(String tiporiga) {
        this.tiporiga = tiporiga;
    }
}