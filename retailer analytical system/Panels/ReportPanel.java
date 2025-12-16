package Panels;

import com.retailanalyticalsystem.Databaseconnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(224, 255, 255)); // light cyan

        // ===== Header =====
        JLabel header = new JLabel("Sales Report", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setForeground(new Color(0, 102, 204));
        add(header, BorderLayout.NORTH);

        // ===== Table =====
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add columns
        tableModel.addColumn("SaleID");
        tableModel.addColumn("ProductID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("TotalPrice");
        tableModel.addColumn("SaleDate");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Refresh Button =====
        refreshButton = new JButton("Refresh");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial data
        loadReports();

        // Refresh button action
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                loadReports();
            }
        });
    }

    private void loadReports() {
        try {
            Connection conn = Databaseconnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM salesreport ORDER BY SaleID";
            ResultSet rs = stmt.executeQuery(sql);

            tableModel.setRowCount(0); // clear existing rows

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

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading sales report: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Optional main method to test panel standalone
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sales Report Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.add(new ReportPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
