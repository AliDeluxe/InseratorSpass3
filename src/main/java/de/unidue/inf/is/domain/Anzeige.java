package de.unidue.inf.is.domain;

import java.util.Date;

public final class Anzeige {

    private short id;
    private String titel;
    private double preis;
    private String beschreibung;
    private String[] kategorien;
    private Date erstellungsDatum;
    private String ersteller;
    private String status;

    public Anzeige() {
        this.titel = "404";
        this.preis = 0;
        this.beschreibung = "";
        this.erstellungsDatum = null;
        this.ersteller = "404";
    }


    public Anzeige(String titel, double preis, String beschreibung, String[] kategorien, Date datum, String ersteller) {
        this.titel = titel;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.kategorien = kategorien;
        this.erstellungsDatum = datum;
        this.ersteller = ersteller;

    }

    public String getTitel() {
        return titel;
    }

    public double getPreis() {
        return preis;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String[] getKategorien() {
        return kategorien;
    }
    public Date getErstellungsDatum() {
        return erstellungsDatum;
    }

    public String getErsteller() {
        return ersteller;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setKategorien(String[] kategorien) {
        this.kategorien = kategorien;
    }

    public void setErstellungsDatum(Date erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }

    public void setErsteller(String user) {
        this.ersteller = user;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}