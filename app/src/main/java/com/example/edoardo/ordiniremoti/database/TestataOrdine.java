package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

public class TestataOrdine extends SugarRecord<TestataOrdine> {
    private int progressivo;
    private String codicecliente;
    private String destinazione;
    private String dataconsegna;
    private String dataordine;
    private String numeroordine;
    private String notetestata;

    public TestataOrdine(){
    }

    public TestataOrdine(int progressivo, String codicecliente, String destinazione, String dataconsegna, String dataordine, String numeroordine, String notetestata) {
        this.progressivo = progressivo;
        this.codicecliente = codicecliente;
        this.destinazione = destinazione;
        this.dataconsegna = dataconsegna;
        this.dataordine = dataordine;
        this.numeroordine = numeroordine;
        this.notetestata = notetestata;
    }

    public int getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(int progressivo) {
        this.progressivo = progressivo;
    }

    public String getCodicecliente() {
        return codicecliente;
    }

    public void setCodicecliente(String codicecliente) {
        this.codicecliente = codicecliente;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getDataconsegna() {
        return dataconsegna;
    }

    public void setDataconsegna(String dataconsegna) {
        this.dataconsegna = dataconsegna;
    }

    public String getDataordine() {
        return dataordine;
    }

    public void setDataordine(String dataordine) {
        this.dataordine = dataordine;
    }

    public String getNumeroordine() {
        return numeroordine;
    }

    public void setNumeroordine(String numeroordine) {
        this.numeroordine = numeroordine;
    }

    public String getNotetestata() {
        return notetestata;
    }

    public void setNotetestata(String notetestata) {
        this.notetestata = notetestata;
    }
}