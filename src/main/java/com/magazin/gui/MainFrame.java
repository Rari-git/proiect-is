package com.magazin.gui;

import com.magazin.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private AdminPanel adminPanel;
    private BuyerPanel buyerPanel;
    private SellerPanel sellerPanel;

    public MainFrame(SistemManager manager) {
        setTitle("Magazin Online");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, manager);
        adminPanel = new AdminPanel(this, manager);
        buyerPanel = new BuyerPanel(this, manager);
        sellerPanel = new SellerPanel(this, manager);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(adminPanel, "Admin");
        mainPanel.add(buyerPanel, "Buyer");
        mainPanel.add(sellerPanel, "Seller");

        add(mainPanel);
        showLoginPanel();
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "Login");
    }

    public void showAdminPanel(Administrator admin) {
        adminPanel.refreshData();
        cardLayout.show(mainPanel, "Admin");
    }

    public void showBuyerPanel(Cumparator buyer) {
        buyerPanel.setCumparator(buyer);
        buyerPanel.refreshData();
        cardLayout.show(mainPanel, "Buyer");
    }

    public void showSellerPanel(Vanzator seller) {
        sellerPanel.setVanzator(seller);
        sellerPanel.refreshData();
        cardLayout.show(mainPanel, "Seller");
    }
}