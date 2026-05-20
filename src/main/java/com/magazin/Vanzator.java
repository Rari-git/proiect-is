package com.magazin;

public class Vanzator extends Utilizator {
    private boolean contAprobat;

    public Vanzator() {
    }

    public Vanzator(String email, String parola) {
        super(email, parola);
        this.contAprobat = false;
    }

    public boolean isContAprobat() {
        return contAprobat;
    }

    public void setContAprobat(boolean status) {
        this.contAprobat = status;
    }
}