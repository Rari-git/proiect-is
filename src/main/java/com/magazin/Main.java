package com.magazin;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SistemManager manager = SistemManager.getInstanta();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            salveazaToateDatele(manager);
        }));

        try {
            List<Produs> produseIncarcate = DataService.incarcaProduse();
            if (produseIncarcate != null)
                manager.setProduse(produseIncarcate);

            List<Utilizator> uInc = DataService.incarcaUtilizatori();
            if (uInc != null)
                manager.setUtilizatori(uInc);

            List<Oferta> oInc = DataService.incarcaOferte();
            if (oInc != null)
                manager.setOferteActive(oInc);

            List<String> iInc = DataService.incarcaIstoric();
            if (iInc != null)
                manager.setIstoricVanzari(iInc);

        } catch (IOException e) {
            System.out.println("Eroare la incarcarea datelor: " + e.getMessage());
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            com.magazin.gui.MainFrame frame = new com.magazin.gui.MainFrame(manager);
            frame.setVisible(true);
        });
    }

    private static void salveazaToateDatele(SistemManager manager) {
        try {
            DataService.salveazaProduse(manager.getProduse());
            DataService.salveazaUtilizatori(manager.getUtilizatori());
            DataService.salveazaOferte(manager.getOferteActive());
            DataService.salveazaIstoric(manager.getIstoricVanzari());
            System.out.println("\n[SISTEM] Toate datele au fost salvate in siguranta!");
        } catch (IOException e) {
            System.err.println("\n[EROARE] Nu s-au putut salva datele la inchidere: " + e.getMessage());
        }
    }
}