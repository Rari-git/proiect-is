package com.magazin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProdusFix.class, name = "fix"),
        @JsonSubTypes.Type(value = ProdusNegociabil.class, name = "negociabil")
})
public abstract class Produs {
    private static int idCounter = 0;
    private int id;
    private String nume;
    private double pret;
    private String descriere;
    private String vanzatorEmail;

    public Produs(String nume, double pret, String descriere, String vanzatorEmail) {
        this.id = ++idCounter;
        this.nume = nume;
        this.pret = pret;
        this.descriere = descriere;
        this.vanzatorEmail = vanzatorEmail;
    }

    // Constructor gol necesar pentru deserializarea JSON
    public Produs() {
    }

    public int getId() {
        return id;
    }

    // Setter pentru id necesar pentru ca Jackson să poată restaura ID-ul salvat
    public void setId(int id) {
        this.id = id;
    }

    public static void setIdCounter(int value) {
        idCounter = value;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    public String getVanzatorEmail() {
        return vanzatorEmail;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setVanzatorEmail(String vanzatorEmail) {
        this.vanzatorEmail = vanzatorEmail;
    }
}