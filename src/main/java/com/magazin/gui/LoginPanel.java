package com.magazin.gui;

import com.magazin.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private SistemManager manager;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(MainFrame mainFrame, SistemManager manager) {
        this.mainFrame = mainFrame;
        this.manager = manager;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Magazin Online - Autentificare");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Parola:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton regBuyerBtn = new JButton("Inregistrare Cumparator");
        JButton regSellerBtn = new JButton("Inregistrare Vanzator");

        buttonPanel.add(loginButton);
        buttonPanel.add(regBuyerBtn);
        buttonPanel.add(regSellerBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        loginButton.addActionListener(this::handleLogin);
        regBuyerBtn.addActionListener(
                e -> register(new Cumparator(emailField.getText(), new String(passwordField.getPassword()))));
        regSellerBtn.addActionListener(
                e -> register(new Vanzator(emailField.getText(), new String(passwordField.getPassword()))));
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());
        Utilizator u = manager.login(email, pass);

        if (u != null) {
            emailField.setText("");
            passwordField.setText("");
            if (u instanceof Administrator) {
                mainFrame.showAdminPanel((Administrator) u);
            } else if (u instanceof Cumparator) {
                mainFrame.showBuyerPanel((Cumparator) u);
            } else if (u instanceof Vanzator) {
                mainFrame.showSellerPanel((Vanzator) u);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Eroare login. Verificati email/parola sau asteptati aprobarea (Vanzatori).", "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register(Utilizator u) {
        if (u.getEmail() == null || u.getEmail().trim().isEmpty() || !u.getEmail().contains("@")) {
            JOptionPane.showMessageDialog(this, "Email invalid!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String p = new String(passwordField.getPassword());
        if (p.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Parola nu poate fi goala!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Utilizator existing : manager.getUtilizatori()) {
            if (existing.getEmail().equals(u.getEmail())) {
                JOptionPane.showMessageDialog(this, "Email-ul exista deja!", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        manager.inregistrare(u);
        JOptionPane.showMessageDialog(this,
                "Inregistrare cu succes! "
                        + (u instanceof Vanzator ? "Asteapta aprobarea adminului." : "Poti sa te loghezi."),
                "Succes", JOptionPane.INFORMATION_MESSAGE);
    }
}