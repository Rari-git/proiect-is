package com.magazin;

public class Oferta {
    private int idProdus;
    private String emailCumparator;
    private double pretPropus;
    private boolean aprobata;

    public Oferta() {
    }

    public Oferta(int idProdus, String emailCumparator, double pretPropus) {
        this.idProdus = idProdus;
        this.emailCumparator = emailCumparator;
        this.pretPropus = pretPropus;
        this.aprobata = false;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public String getEmailCumparator() {
        return emailCumparator;
    }

    public double getPretPropus() {
        return pretPropus;
    }

    public boolean isAprobata() {
        return aprobata;
    }

    public void setAprobata(boolean aprobata) {
        this.aprobata = aprobata;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public void setEmailCumparator(String emailCumparator) {
        this.emailCumparator = emailCumparator;
    }

    public void setPretPropus(double pretPropus) {
        this.pretPropus = pretPropus;
    }

    @Override
    public String toString() {
        return "Oferta pt produs ID " + idProdus + " de la " + emailCumparator + ": " + pretPropus + " EUR";
    }
}