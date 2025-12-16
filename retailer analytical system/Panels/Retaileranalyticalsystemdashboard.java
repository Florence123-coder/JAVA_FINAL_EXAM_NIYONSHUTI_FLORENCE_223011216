package Panels;

import javax.swing.*;
import java.awt.*;

public class Retaileranalyticalsystemdashboard extends JFrame {

    public Retaileranalyticalsystemdashboard(String role, String username) {

        setTitle("Unified Dashboard - Retail Analytical System (" + role + ")");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ====== MAIN TAB PANEL ======
        JTabbedPane tabs = new JTabbedPane();

        // ========= ADMIN FEATURES =========
        UsersPanel usersPanel = new UsersPanel();
        usersPanel.setBackground(new Color(255, 228, 225)); // light pink
        tabs.addTab("Users (Admin)", usersPanel);

        ProductPanel productsPanel = new ProductPanel();
        productsPanel.setBackground(new Color(224, 255, 255)); // light cyan
        tabs.addTab("Products (Admin)", productsPanel);

        SupplierPanel suppliersPanel = new SupplierPanel();
        suppliersPanel.setBackground(new Color(240, 255, 240)); // honeydew
        tabs.addTab("Suppliers (Admin)", suppliersPanel);

        CustomerPanel customersPanel = new CustomerPanel();
        customersPanel.setBackground(new Color(255, 250, 205)); // lemon chiffon
        tabs.addTab("Customers (Admin)", customersPanel);

        InventoryPanel inventoryPanel = new InventoryPanel();
        inventoryPanel.setBackground(new Color(255, 239, 213)); // papaya whip
        tabs.addTab("Inventory (Admin)", inventoryPanel);

        SalePanel salesPanel = new SalePanel();
        salesPanel.setBackground(new Color(221, 160, 221)); // plum
        tabs.addTab("Sales (Admin)", salesPanel);

        // ========= MANAGER FEATURES =========
        ProductPanel managerProductsPanel = new ProductPanel();
        managerProductsPanel.setBackground(new Color(224, 255, 255));
        tabs.addTab("Products (Manager)", managerProductsPanel);

        SupplierPanel managerSuppliersPanel = new SupplierPanel();
        managerSuppliersPanel.setBackground(new Color(240, 255, 240));
        tabs.addTab("Suppliers (Manager)", managerSuppliersPanel);

        InventoryPanel managerInventoryPanel = new InventoryPanel();
        managerInventoryPanel.setBackground(new Color(255, 239, 213));
        tabs.addTab("Inventory (Manager)", managerInventoryPanel);

        ReportPanel managerReportsPanel = new ReportPanel();
        managerReportsPanel.setBackground(new Color(255, 228, 225));
        tabs.addTab("Reports (Manager)", managerReportsPanel);

        // ========= CASHIER FEATURES =========
        SalePanel cashierSalesPanel = new SalePanel();
        cashierSalesPanel.setBackground(new Color(221, 160, 221));
        tabs.addTab("Sales (Cashier)", cashierSalesPanel);

        ReportPanel cashierReportsPanel = new ReportPanel();
        cashierReportsPanel.setBackground(new Color(255, 228, 225));
        tabs.addTab("Reports (Cashier)", cashierReportsPanel);

        // ========= CUSTOMER FEATURES =========
        ProductPanel customerProductsPanel = new ProductPanel();
        customerProductsPanel.setBackground(new Color(224, 255, 255));
        tabs.addTab("Products (Customer)", customerProductsPanel);

        OrdersPanel customerOrdersPanel = new OrdersPanel();
        customerOrdersPanel.setBackground(new Color(255, 250, 205));
        tabs.addTab("Orders (Customer)", customerOrdersPanel);

        // ==== HEADER PANEL WITH USER INFO ====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setPreferredSize(new Dimension(1000, 50));

        JLabel title = new JLabel("Unified Retail Analytical Dashboard", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel userInfo = new JLabel("Logged in as: " + role + " - " + username + "  ");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        header.add(title, BorderLayout.CENTER);
        header.add(userInfo, BorderLayout.EAST);

        // Add to frame
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Retaileranalyticalsystemdashboard("Admin", "SuperUser");
    }
}
