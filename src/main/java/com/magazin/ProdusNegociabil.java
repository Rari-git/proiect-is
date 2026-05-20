package com.magazin;

public class ProdusNegociabil extends Produs {
    private double pretMinim;

    public ProdusNegociabil() {
    }

    public ProdusNegociabil(String nume, double pret, String descriere, String vanzatorEmail, double pretMinim) {
        super(nume, pret, descriere, vanzatorEmail);
        this.pretMinim = pretMinim;
    }

    public double getPretMinim() {
        return pretMinim;
    }

    public void setPretMinim(double pretMinim) {
        this.pretMinim = pretMinim;
    }
}