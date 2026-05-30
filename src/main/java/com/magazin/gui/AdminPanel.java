package com.magazin.gui;

import com.magazin.*;
import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    private SistemManager manager;
    private DefaultListModel<String> listModel;
    private JList<String> unapprovedSellersList;

    public AdminPanel(MainFrame mainFrame, SistemManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Panou Administrator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        unapprovedSellersList = new JList<>(listModel);
        add(new JScrollPane(unapprovedSellersList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton approveBtn = new JButton("Aproba Vanzator");
        JButton deactivateBtn = new JButton("Dezactiveaza Vanzator");
        JButton logoutBtn = new JButton("Logout");

        buttonPanel.add(approveBtn);
        buttonPanel.add(deactivateBtn);
        buttonPanel.add(logoutBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        approveBtn.addActionListener(e -> setStatus(true));
        deactivateBtn.addActionListener(e -> setStatus(false));
        logoutBtn.addActionListener(e -> mainFrame.showLoginPanel());
    }

    public void refreshData() {
        listModel.clear();
        for (Vanzator v : manager.getVanzatoriNeaprobati()) {
            listModel.addElement(v.getEmail());
        }
    }

    private void setStatus(boolean status) {
        String selected = unapprovedSellersList.getSelectedValue();
        if (selected != null) {
            manager.setStatusVanzator(selected, status);
            refreshData();
            JOptionPane.showMessageDialog(this, "Status actualizat cu succes pentru " + selected, "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza un vanzator din lista!", "Eroare",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}