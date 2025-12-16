package com.retailanalyticalsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

@SuppressWarnings("serial")
public class Report extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public Report() {
        setTitle("Sales Report");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        tableModel.addColumn("SaleID");
        tableModel.addColumn("ProductID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("TotalPrice");
        tableModel.addColumn("SaleDate");

        JScrollPane scrollPane = new JScrollPane(table);

        // Refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                loadReports();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load data initially
        loadReports();

        setVisible(true);
    }

    private void loadReports() {
        try {
            Connection conn = Databaseconnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM salesreport";
            ResultSet rs = stmt.executeQuery(sql);

            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("SaleID"),
                    rs.getInt("ProductID"),
                    rs.getInt("Quantity"),
                    rs.getDouble("TotalPrice"),
                    rs.getTimestamp("SaleDate")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sales reports: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Test main
    public static void main(String[] args) {
        new Report();
    }
}
