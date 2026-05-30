package com.magazin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SistemManagerTest {
    private SistemManager manager;

    @BeforeEach
    public void setUp() {
        manager = SistemManager.getInstanta();
        manager.reset();
    }

    @Test
    public void testLoginAdminImplicit() {
        Utilizator admin = manager.login("admin@email.com", "admin");
        assertNotNull(admin, "Adminul implicit trebuie să se poată loga");
        assertTrue(admin instanceof Administrator, "Utilizatorul trebuie să fie de tip Administrator");
    }

    @Test
    public void testInregistrareSiLoginCumparator() {
        Cumparator c = new Cumparator("buyer@email.com", "1234");
        manager.inregistrare(c);

        Utilizator gasit = manager.login("buyer@email.com", "1234");
        assertNotNull(gasit, "Cumpărătorul se poate loga direct (nu necesită aprobare)");
        assertTrue(gasit instanceof Cumparator, "Tipul returnat trebuie să fie Cumparator");
    }

    @Test
    public void testInregistrareSiAprobareVanzator() {
        Vanzator v = new Vanzator("seller@email.com", "1234");
        manager.inregistrare(v);

        Utilizator neaprobat = manager.login("seller@email.com", "1234");
        assertNull(neaprobat, "Vânzătorul neaprobat NU trebuie să se poată loga");

        manager.setStatusVanzator("seller@email.com", true);
        Utilizator aprobat = manager.login("seller@email.com", "1234");
        assertNotNull(aprobat, "Vânzătorul aprobat trebuie să se poată loga");
        assertTrue(aprobat instanceof Vanzator, "Tipul returnat trebuie să fie Vanzator");
    }

    @Test
    public void testCumparaProdusFix() {
        ProdusFix p = new ProdusFix("Test Fix", 100.0, "Descriere", "v@email.com");
        manager.adaugaProdus(p);

        assertEquals(1, manager.getProduse().size());

        manager.cumparaProdusFix(p.getId(), "buyer@email.com");

        assertEquals(0, manager.getProduse().size(), "Produsul ar trebui sters după cumpărare");
        assertEquals(1, manager.getIstoricVanzari().size(), "Vânzarea trebuie înregistrată în istoric");
    }

    @Test
    public void testProceseazaOfertaPestePretMinim() {
        ProdusNegociabil p = new ProdusNegociabil("Negociabil", 200.0, "Desc", "v@email.com", 150.0);
        manager.adaugaProdus(p);

        boolean acceptata = manager.proceseazaOferta(p.getId(), "buyer@email.com", 160.0);

        assertTrue(acceptata, "Oferta peste prețul minim trebuie acceptată în sistem");
        assertEquals(1, manager.getOferteActive().size(), "Oferta trebuie să se regăsească în listă");
    }

    @Test
    public void testProceseazaOfertaSubPretMinim() {
        ProdusNegociabil p = new ProdusNegociabil("Negociabil", 200.0, "Desc", "v@email.com", 150.0);
        manager.adaugaProdus(p);

        boolean acceptata = manager.proceseazaOferta(p.getId(), "buyer@email.com", 140.0);

        assertFalse(acceptata, "Oferta sub prețul minim trebuie respinsă automat");
        assertEquals(0, manager.getOferteActive().size(), "Nicio ofertă nu ar trebui adăugată");
    }

    @Test
    public void testAprobaOfertaVanzator() {
        ProdusNegociabil p = new ProdusNegociabil("Negociabil", 200.0, "Desc", "v@email.com", 150.0);
        manager.adaugaProdus(p);
        manager.proceseazaOferta(p.getId(), "buyer@email.com", 180.0);

        Oferta oferta = manager.getOferteActive().get(0);
        manager.aprobaOferta(oferta);

        assertEquals(0, manager.getOferteActive().size(), "Ofertele produsului trebuie șterse după finalizare");
        assertEquals(0, manager.getProduse().size(), "Produsul trebuie șters după cumpărare");
        assertEquals(1, manager.getIstoricVanzari().size(),
                "Istoricul trebuie să conțină o înregistrare nouă cu prețul acceptat");
    }

    @Test
    public void testRespingeOferta() {
        ProdusNegociabil p = new ProdusNegociabil("Negociabil", 200.0, "Desc", "v@email.com", 150.0);
        manager.adaugaProdus(p);
        manager.proceseazaOferta(p.getId(), "buyer@email.com", 180.0);

        Oferta oferta = manager.getOferteActive().get(0);
        manager.respingeOferta(oferta);

        assertEquals(0, manager.getOferteActive().size(), "Oferta trebuie să fie eliminată");
        assertEquals(1, manager.getProduse().size(), "Produsul trebuie să rămână disponibil");
    }

    @Test
    public void testAnuleazaVanzareProdus() {
        ProdusFix p = new ProdusFix("Test Anulare", 100.0, "Desc", "v@email.com");
        manager.adaugaProdus(p);

        manager.anuleazaVanzare(p.getId(), "v@email.com");

        assertEquals(0, manager.getProduse().size(), "Produsul anulat trebuie să dispară din sistem");
    }
}