package com.magazin;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Magazin Online ---");
            System.out.println("1. Login");
            System.out.println("2. Inregistrare Cumparator");
            System.out.println("3. Inregistrare Vanzator");
            System.out.println("0. Iesire");

            while (!scanner.hasNextInt()) {
                System.out.println("Eroare: Va rugam sa introduceti o cifra valida.");
                scanner.next();
            }
            int opt = scanner.nextInt();
            scanner.nextLine();
            switch (opt) {
                case 1:
                    System.out.print("Email: ");
                    String e = scanner.nextLine();
                    System.out.print("Parola: ");
                    String p = scanner.nextLine();
                    Utilizator u = manager.login(e, p);
                    if (u != null)
                        meniuUtilizator(u, scanner, manager);
                    else
                        System.out.println("Eroare login.");
                    break;
                case 2:
                    System.out.print("Email: ");
                    String ec = scanner.nextLine();
                    System.out.print("Parola: ");
                    String pc = scanner.nextLine();
                    manager.inregistrare(new Cumparator(ec, pc));
                    break;
                case 3:
                    System.out.print("Email: ");
                    String ev = scanner.nextLine();
                    System.out.print("Parola: ");
                    String pv = scanner.nextLine();
                    manager.inregistrare(new Vanzator(ev, pv));
                    break;
                case 0:
                    running = false;
                    System.out.println("Se închide aplicația...");
                    break;
            }
        }
    }

    private static void meniuUtilizator(Utilizator u, Scanner sc, SistemManager sm) {
        boolean logat = true;
        while (logat) {
            System.out.println("\n--- Dashboard " + u.getClass().getSimpleName() + " ---");
            System.out.println("Sunteti logat ca: " + u.getEmail());
            if (u instanceof Administrator) {
                System.out.println("1. Aproba vanzator, 2. Dezactiveaza vanzator (Anulare cont), 0. Logout");
                while (!sc.hasNextInt()) {
                    System.out.println("Eroare: Introduceti un numar.");
                    sc.next();
                }
                int opt = sc.nextInt();
                sc.nextLine();
                if (opt == 0) {
                    logat = false;
                } else if (opt == 1) {
                    sm.getVanzatoriNeaprobati().forEach(v -> System.out.println(v.getEmail()));
                    System.out.print("Email de aprobat: ");
                    sm.setStatusVanzator(sc.nextLine(), true);
                } else if (opt == 2) {
                    System.out.print("Email vanzator de dezactivat: ");
                    sm.setStatusVanzator(sc.nextLine(), false);
                }
            } else if (u instanceof Cumparator) {
                System.out.println("1. Vizualizeaza produse, 2. Cumpara pret fix, 3. Fa oferta negociabil, 0. Logout");
                while (!sc.hasNextInt()) {
                    System.out.println("Eroare: Introduceti un numar.");
                    sc.next();
                }
                int opt = sc.nextInt();
                sc.nextLine();
                if (opt == 0) {
                    logat = false;
                } else if (opt == 1) {
                    System.out.println("\n--- Lista Produse ---");
                    sm.getProduse()
                            .forEach(p -> {
                                String tip = (p instanceof ProdusFix) ? "FIX" : "NEGOCIABIL";
                                System.out.println("[" + tip + "] ID: " + p.getId() + " | " + p.getNume() + " | Pret: " + p.getPret()
                                    + " EUR | Vanzator: " + p.getVanzatorEmail() + "\n   Descriere: " + p.getDescriere());
                            });
                } else if (opt == 2) {
                    System.out.print("ID Produs: ");
                    while (!sc.hasNextInt()) {
                        System.out.println("Eroare: ID invalid.");
                        sc.next();
                    }
                    int id = sc.nextInt();
                    sm.cumparaProdusFix(id, u.getEmail());
                } else if (opt == 3) {
                    System.out.print("ID Produs: ");
                    while (!sc.hasNextInt()) {
                        System.out.println("Eroare: ID invalid.");
                        sc.next();
                    }
                    int id = sc.nextInt();
                    System.out.print("Pret propus: ");
                    while (!sc.hasNextDouble()) {
                        System.out.println("Eroare: Introduceti un pret valid (ex: 10.5).");
                        sc.next();
                    }
                    double pret = sc.nextDouble();
                    sm.proceseazaOferta(id, u.getEmail(), pret);
                }
            } else if (u instanceof Vanzator) {
                System.out.println(
                        "1. Pune produs (Fix), 2. Pune produs (Negociabil), 3. Gestionare oferte, 4. Anuleaza vanzare, 0. Logout");
                while (!sc.hasNextInt()) {
                    System.out.println("Eroare: Introduceti un numar.");
                    sc.next();
                }
                int opt = sc.nextInt();
                sc.nextLine();
                switch (opt) {
                    case 0:
                        logat = false;
                        break;
                    case 1:
                        System.out.print("Nume: ");
                        String n1 = sc.nextLine();
                        System.out.print("Pret: ");
                        while (!sc.hasNextDouble()) {
                            System.out.println("Eroare: Introduceti un pret valid.");
                            sc.next();
                        }
                        double p1 = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Descriere: ");
                        String d1 = sc.nextLine();
                        sm.adaugaProdus(new ProdusFix(n1, p1, d1, u.getEmail()));
                        break;
                    case 2:
                        System.out.print("Nume: ");
                        String n2 = sc.nextLine();
                        System.out.print("Pret afisat: ");
                        while (!sc.hasNextDouble()) {
                            System.out.println("Eroare: Introduceti un pret valid.");
                            sc.next();
                        }
                        double p2 = sc.nextDouble();
                        System.out.print("Pret minim (secret): ");
                        while (!sc.hasNextDouble()) {
                            System.out.println("Eroare: Introduceti un pret valid.");
                            sc.next();
                        }
                        double pm = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Descriere: ");
                        String d2 = sc.nextLine();
                        sm.adaugaProdus(new ProdusNegociabil(n2, p2, d2, u.getEmail(), pm));
                        break;
                    case 3:
                        List<Oferta> oferte = sm.getOfertePentruVanzator(u.getEmail());
                        for (int i = 0; i < oferte.size(); i++)
                            System.out.println(i + ". " + oferte.get(i));
                        System.out.print("Selectati index oferta (sau -1): ");
                        while (!sc.hasNextInt()) {
                            System.out.println("Eroare: Index invalid.");
                            sc.next();
                        }
                        int idx = sc.nextInt();
                        if (idx >= 0 && idx < oferte.size()) {
                            System.out.println("1. Aproba, 2. Respinge/Anula");
                            while (!sc.hasNextInt()) {
                                System.out.println("Eroare: Introduceti 1 sau 2.");
                                sc.next();
                            }
                            int decizie = sc.nextInt();
                            if (decizie == 1)
                                sm.aprobaOferta(oferte.get(idx));
                            else if (decizie == 2)
                                sm.respingeOferta(oferte.get(idx));
                        }
                        break;
                    case 4:
                        System.out.print("ID Produs de anulat: ");
                        while (!sc.hasNextInt()) {
                            System.out.println("Eroare: ID invalid.");
                            sc.next();
                        }
                        sm.anuleazaVanzare(sc.nextInt(), u.getEmail());
                        break;
                }
            }
        }
    }

    private static void salveazaToateDatele(SistemManager manager) {
        try {
            DataService.salveazaProduse(manager.getProduse());
            DataService.salveazaUtilizatori(manager.getUtilizatori());
            DataService.salveazaOferte(manager.getOferteActive());
            DataService.salveazaIstoric(manager.getIstoricVanzari());
            System.out.println("\n[SISTEM] Toate datele au fost salvate în siguranță!");
        } catch (IOException e) {
            System.err.println("\n[EROARE] Nu s-au putut salva datele la închidere: " + e.getMessage());
        }
    }
}