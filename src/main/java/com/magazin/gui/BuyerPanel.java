package com.magazin.gui;

import com.magazin.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BuyerPanel extends JPanel {
    private SistemManager manager;
    private Cumparator currentBuyer;
    private DefaultTableModel tableModel;
    private JTable productsTable;

    public BuyerPanel(MainFrame mainFrame, SistemManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Panou Cumparator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "ID", "Nume", "Tip", "Pret", "Descriere", "Vanzator" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(tableModel);
        productsTable.removeColumn(productsTable.getColumnModel().getColumn(0));
        add(new JScrollPane(productsTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton buyFixBtn = new JButton("Cumpara Produs Fix");
        JButton makeOfferBtn = new JButton("Fa Oferta (Negociabil)");
        JButton logoutBtn = new JButton("Logout");

        buttonPanel.add(buyFixBtn);
        buttonPanel.add(makeOfferBtn);
        buttonPanel.add(logoutBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        buyFixBtn.addActionListener(e -> buyFix());
        makeOfferBtn.addActionListener(e -> makeOffer());
        logoutBtn.addActionListener(e -> {
            currentBuyer = null;
            mainFrame.showLoginPanel();
        });
    }

    public void setCumparator(Cumparator buyer) {
        this.currentBuyer = buyer;
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Produs p : manager.getProduse()) {
            String tip = (p instanceof ProdusFix) ? "FIX" : "NEGOCIABIL";
            tableModel.addRow(
                    new Object[] { p.getId(), p.getNume(), tip, p.getPret(), p.getDescriere(), p.getVanzatorEmail() });
        }
    }

    private void buyFix() {
        int row = productsTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            String tip = (String) tableModel.getValueAt(row, 2);
            if ("FIX".equals(tip)) {
                manager.cumparaProdusFix(id, currentBuyer.getEmail());
                refreshData();
                JOptionPane.showMessageDialog(this, "Produs cumparat cu succes!", "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Acest produs este negociabil, foloseste butonul 'Fa Oferta'.",
                        "Eroare", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza un produs!", "Eroare", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void makeOffer() {
        int row = productsTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            String tip = (String) tableModel.getValueAt(row, 2);
            if ("NEGOCIABIL".equals(tip)) {
                String input = JOptionPane.showInputDialog(this, "Introdu pretul propus:");
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        double pret = Double.parseDouble(input);
                        boolean success = manager.proceseazaOferta(id, currentBuyer.getEmail(), pret);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Oferta a fost trimisa!", "Succes",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Oferta respinsa automat (sub pretul minim).", "Eroare",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Pret invalid!", "Eroare", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Acest produs are pret fix.", "Eroare",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza un produs!", "Eroare", JOptionPane.WARNING_MESSAGE);
        }
    }
}