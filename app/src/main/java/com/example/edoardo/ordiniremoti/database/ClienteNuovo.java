package com.example.edoardo.ordiniremoti.database;

import com.orm.SugarRecord;

public class ClienteNuovo extends SugarRecord<ClienteNuovo> {
    private String codice;
    private String descrizione;
    private String descrizione2;
    private String via;
    private String cap;
    private String citta;
    private String provincia;
    private String telefono;
    private String email;
    private String partitaiva;
    private String notelibere;

    public ClienteNuovo(){
    }

    public ClienteNuovo(String codice, String descrizione, String descrizione2, String via, String cap, String citta, String provincia, String telefono, String email, String partitaiva, String notelibere) {
        this.codice = codice;
        this.descrizione = descrizione;
        this.descrizione2 = descrizione2;
        this.via = via;
        this.cap = cap;
        this.citta = citta;
        this.provincia = provincia;
        this.telefono = telefono;
        this.email = email;
        this.partitaiva = partitaiva;
        this.notelibere = notelibere;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione2() {
        return descrizione2;
    }

    public void setDescrizione2(String descrizione2) {
        this.descrizione2 = descrizione2;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPartitaiva() {
        return partitaiva;
    }

    public void setPartitaiva(String partitaiva) {
        this.partitaiva = partitaiva;
    }

    public String getNotelibere() {
        return notelibere;
    }

    public void setNotelibere(String notelibere) {
        this.notelibere = notelibere;
    }
}
