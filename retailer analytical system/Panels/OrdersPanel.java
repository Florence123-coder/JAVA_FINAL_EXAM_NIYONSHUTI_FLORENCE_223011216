package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrdersPanel extends JPanel {

    private JTable orderTable;
    private DefaultTableModel tableModel;

    public OrdersPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(224, 255, 255)); // light cyan

        // ===== Header =====
        JLabel header = new JLabel("Customer Orders", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setForeground(new Color(0, 102, 204));
        add(header, BorderLayout.NORTH);

        // ===== Table =====
        tableModel = new DefaultTableModel();
        orderTable = new JTable(tableModel);
        orderTable.setFillsViewportHeight(true);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add columns
        tableModel.addColumn("OrderID");
        tableModel.addColumn("CustomerID");
        tableModel.addColumn("ProductID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("TotalPrice");
        tableModel.addColumn("OrderDate");

        // Load data from database
        loadOrders();

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Refresh Button =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton refreshButton1 = new JButton("Refresh");
        refreshButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadOrders();
            }
        });
        buttonPanel.add(refreshButton1);

        buttonPanel.add(refreshButton1);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try {
            Connection conn = DBConnection.getConnection(); // use your DB connection class
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM orders"; // change table name if needed
            ResultSet rs = stmt.executeQuery(sql);

            // Clear existing rows
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("OrderID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("ProductID"),
                        rs.getInt("Quantity"),
                        rs.getDouble("TotalPrice"),
                        rs.getTimestamp("OrderDate")
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
