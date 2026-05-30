package com.magazin.gui;

import com.magazin.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SellerPanel extends JPanel {
    private SistemManager manager;
    private Vanzator currentSeller;

    private DefaultTableModel productsTableModel;
    private JTable productsTable;
    private DefaultTableModel offersTableModel;
    private JTable offersTable;

    public SellerPanel(MainFrame mainFrame, SistemManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Panou Vanzator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel productsTab = new JPanel(new BorderLayout());
        String[] pCols = { "ID", "Nume", "Tip", "Pret Afisat", "Descriere" };
        productsTableModel = new DefaultTableModel(pCols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(productsTableModel);
        productsTab.add(new JScrollPane(productsTable), BorderLayout.CENTER);

        JPanel pBtnPanel = new JPanel();
        JButton addFixBtn = new JButton("Adauga Produs Fix");
        JButton addNegBtn = new JButton("Adauga Produs Negociabil");
        JButton cancelBtn = new JButton("Anuleaza Vanzare");
        pBtnPanel.add(addFixBtn);
        pBtnPanel.add(addNegBtn);
        pBtnPanel.add(cancelBtn);
        productsTab.add(pBtnPanel, BorderLayout.SOUTH);

        JPanel offersTab = new JPanel(new BorderLayout());
        String[] oCols = { "ID Produs", "Nume Produs", "Cumparator", "Pret Propus" };
        offersTableModel = new DefaultTableModel(oCols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        offersTable = new JTable(offersTableModel);
        offersTable.removeColumn(offersTable.getColumnModel().getColumn(0));
        offersTab.add(new JScrollPane(offersTable), BorderLayout.CENTER);

        JPanel oBtnPanel = new JPanel();
        JButton approveBtn = new JButton("Aproba Oferta");
        JButton rejectBtn = new JButton("Respinge Oferta");
        oBtnPanel.add(approveBtn);
        oBtnPanel.add(rejectBtn);
        offersTab.add(oBtnPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Produsele Mele", productsTab);
        tabbedPane.addTab("Oferte Primite", offersTab);

        add(tabbedPane, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        add(logoutBtn, BorderLayout.SOUTH);

        addFixBtn.addActionListener(e -> addProdusFix());
        addNegBtn.addActionListener(e -> addProdusNegociabil());
        cancelBtn.addActionListener(e -> anuleazaVanzare());

        approveBtn.addActionListener(e -> approveOffer());
        rejectBtn.addActionListener(e -> rejectOffer());

        logoutBtn.addActionListener(e -> {
            currentSeller = null;
            mainFrame.showLoginPanel();
        });
    }

    public void setVanzator(Vanzator v) {
        this.currentSeller = v;
    }

    public void refreshData() {
        if (currentSeller == null)
            return;

        productsTableModel.setRowCount(0);
        for (Produs p : manager.getProduse()) {
            if (p.getVanzatorEmail().equals(currentSeller.getEmail())) {
                String tip = (p instanceof ProdusFix) ? "FIX" : "NEGOCIABIL";
                productsTableModel.addRow(new Object[] { p.getId(), p.getNume(), tip, p.getPret(), p.getDescriere() });
            }
        }

        offersTableModel.setRowCount(0);
        for (Oferta o : manager.getOfertePentruVanzator(currentSeller.getEmail())) {
            String numeProd = "";
            for (Produs p : manager.getProduse()) {
                if (p.getId() == o.getIdProdus()) {
                    numeProd = p.getNume();
                    break;
                }
            }
            offersTableModel.addRow(new Object[]{o.getIdProdus(), numeProd, o.getEmailCumparator(), o.getPretPropus()});
        }
    }

    private void addProdusFix() {
        JTextField numeField = new JTextField();
        JTextField pretField = new JTextField();
        JTextField descField = new JTextField();
        Object[] message = { "Nume:", numeField, "Pret:", pretField, "Descriere:", descField };

        int option = JOptionPane.showConfirmDialog(this, message, "Adauga Produs Fix", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double pret = Double.parseDouble(pretField.getText());
                manager.adaugaProdus(
                        new ProdusFix(numeField.getText(), pret, descField.getText(), currentSeller.getEmail()));
                refreshData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pret invalid!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addProdusNegociabil() {
        JTextField numeField = new JTextField();
        JTextField pretField = new JTextField();
        JTextField pretMinField = new JTextField();
        JTextField descField = new JTextField();
        Object[] message = { "Nume:", numeField, "Pret Afisat:", pretField, "Pret Minim (Secret):", pretMinField,
                "Descriere:", descField };

        int option = JOptionPane.showConfirmDialog(this, message, "Adauga Produs Negociabil",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double pret = Double.parseDouble(pretField.getText());
                double pretMin = Double.parseDouble(pretMinField.getText());
                manager.adaugaProdus(new ProdusNegociabil(numeField.getText(), pret, descField.getText(),
                        currentSeller.getEmail(), pretMin));
                refreshData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pret invalid!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void anuleazaVanzare() {
        int row = productsTable.getSelectedRow();
        if (row != -1) {
            int id = (int) productsTableModel.getValueAt(row, 0);
            manager.anuleazaVanzare(id, currentSeller.getEmail());
            refreshData();
            JOptionPane.showMessageDialog(this, "Vanzare anulata cu succes!", "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza un produs!", "Eroare", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Oferta getSelectedOffer() {
        int row = offersTable.getSelectedRow();
        if (row != -1) {
            int idProd = (int) offersTableModel.getValueAt(row, 0);
            String cump = (String) offersTableModel.getValueAt(row, 2);
            List<Oferta> oferte = manager.getOfertePentruVanzator(currentSeller.getEmail());
            for (Oferta o : oferte) {
                if (o.getIdProdus() == idProd && o.getEmailCumparator().equals(cump)) {
                    return o;
                }
            }
        }
        return null;
    }

    private void approveOffer() {
        Oferta o = getSelectedOffer();
        if (o != null) {
            manager.aprobaOferta(o);
            refreshData();
            JOptionPane.showMessageDialog(this, "Oferta aprobata!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza o oferta!", "Eroare", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void rejectOffer() {
        Oferta o = getSelectedOffer();
        if (o != null) {
            manager.respingeOferta(o);
            refreshData();
            JOptionPane.showMessageDialog(this, "Oferta respinsa!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza o oferta!", "Eroare", JOptionPane.WARNING_MESSAGE);
        }
    }
}