package com.magazin;

import java.util.ArrayList;
import java.util.List;

public class SistemManager {
    private static SistemManager instanta;
    private List<Utilizator> utilizatori;
    private List<String> istoricVanzari;

    private SistemManager() {
        this.utilizatori = new ArrayList<>();
        this.istoricVanzari = new ArrayList<>();
        utilizatori.add(new Administrator("admin@email.com", "admin"));
    }

    public static synchronized SistemManager getInstanta() {
        if (instanta == null)
            instanta = new SistemManager();
        return instanta;
    }

    public void reset() {
        this.utilizatori.clear();
        this.istoricVanzari.clear();
        this.utilizatori.add(new Administrator("admin@email.com", "admin"));
    }
    public void adaugaProdus(Produs p) {
        if (p != null)
            this.produse.add(p);
    }

    public void anuleazaVanzare(int idProdus, String emailVanzator) {
        produse.removeIf(p -> p.getId() == idProdus && p.getVanzatorEmail().equals(emailVanzator));
        oferteActive.removeIf(o -> o.getIdProdus() == idProdus);
        System.out.println("Vanzarea a fost anulata.");
    }

    // Modifică temporar metoda login în SistemManager.java
    public Utilizator login(String email, String parola) {
        for (Utilizator u : utilizatori) {
            if (u.getEmail().equals(email) && u.getParola().equals(parola)) {
                if (u instanceof Vanzator) {
                    if (!((Vanzator) u).isContAprobat()) {
                        System.out.println("Eroare: Contul de vanzator nu este inca aprobat de administrator.");
                        return null;
                    }
                }
                return u;
            }
        }
        System.out.println("Eroare: Email sau parola incorecta.");
        return null;
    }

    public void inregistrare(Utilizator u) {
        if (u.getEmail() == null || !u.getEmail().matches("^(.+)@(.+)$")) {
            System.out.println("Eroare: Email-ul '" + u.getEmail() + "' este invalid (trebuie să conțină @).");
            return;
        }

        this.utilizatori.add(u);
        if (u instanceof Cumparator) {
            System.out.println("Cont cumparator creat cu succes.");
        } else {
            System.out.println("Cerere trimisa. Contul de vanzator asteapta aprobarea adminului.");
        }
    }

    public List<Vanzator> getVanzatoriNeaprobati() {
        List<Vanzator> lista = new ArrayList<>();
        for (Utilizator u : utilizatori) {
            if (u instanceof Vanzator && !((Vanzator) u).isContAprobat())
                lista.add((Vanzator) u);
        }
        return lista;
    }

    public void setStatusVanzator(String email, boolean status) {
        boolean gasit = false;
        for (Utilizator u : utilizatori) {
            if (u instanceof Vanzator && u.getEmail().equals(email)) {
                ((Vanzator) u).setContAprobat(status);
                gasit = true;
                System.out.println(
                        "Contul vanzatorului " + email + (status ? " a fost activat." : " a fost dezactivat."));
            }
        }
        if (!gasit)
            System.out.println("Eroare: Vanzatorul cu email-ul " + email + " nu a fost gasit.");
    }

    public List<String> getIstoricVanzari() {
        return istoricVanzari;
    }

    public void setIstoricVanzari(List<String> istoric) {
        if (istoric != null)
            this.istoricVanzari = istoric;
    }

    public List<Utilizator> getUtilizatori() {
        return utilizatori;
    }

    public void setUtilizatori(List<Utilizator> utilizatori) {
        if (utilizatori != null) {
            this.utilizatori = utilizatori;
            // Ne asigurăm că administratorul default există mereu dacă lista e nouă
            if (utilizatori.stream().noneMatch(u -> u.getEmail().equals("admin@email.com"))) {
                utilizatori.add(new Administrator("admin@email.com", "admin"));
            }
        }
    }
}