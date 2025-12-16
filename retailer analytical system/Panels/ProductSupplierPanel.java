package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProductSupplierPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtProductID, txtSupplierID;
    private JButton btnAdd, btnUpdate, btnDelete, btnExit;

    public ProductSupplierPanel() {
        setLayout(new BorderLayout());

        // ----- Input Panel -----
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product-Supplier Details"));

        txtProductID = new JTextField();
        txtSupplierID = new JTextField();

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(txtProductID);
        inputPanel.add(new JLabel("Supplier ID:"));
        inputPanel.add(txtSupplierID);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnExit = new JButton("Exit");

        inputPanel.add(btnAdd);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete);
        inputPanel.add(btnExit);

        add(inputPanel, BorderLayout.NORTH);

        // ----- Table -----
        model = new DefaultTableModel(new String[]{"ProductID", "SupplierID"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- Load data -----
        loadData();

        // ----- Button Actions using lambdas -----
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addProductSupplier();
            }
        });

        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateProductSupplier();
            }
        });

        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteProductSupplier();
            }
        });

        btnExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                exitApplication();
            }
        });

        // ----- Table click -----
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtProductID.setText(model.getValueAt(row, 0).toString());
                    txtSupplierID.setText(model.getValueAt(row, 1).toString());
                }
            }
        });
    }

    // ----- Load data -----
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM productsupplier")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getInt("SupplierID")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ----- Add -----
    private void addProductSupplier() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO productsupplier (ProductID, SupplierID) VALUES (?, ?)")) {

            ps.setInt(1, Integer.parseInt(txtProductID.getText()));
            ps.setInt(2, Integer.parseInt(txtSupplierID.getText()));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Product-Supplier added successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for ProductID and SupplierID.");
        }
    }

    // ----- Update -----
    private void updateProductSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to update.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE productsupplier SET SupplierID=? WHERE ProductID=?")) {

            ps.setInt(1, Integer.parseInt(txtSupplierID.getText()));
            ps.setInt(2, Integer.parseInt(txtProductID.getText()));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Product-Supplier updated successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for ProductID and SupplierID.");
        }
    }

    // ----- Delete -----
    private void deleteProductSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM productsupplier WHERE ProductID=? AND SupplierID=?")) {

            ps.setInt(1, Integer.parseInt(txtProductID.getText()));
            ps.setInt(2, Integer.parseInt(txtSupplierID.getText()));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Record deleted successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ----- Clear -----
    private void clearFields() {
        txtProductID.setText("");
        txtSupplierID.setText("");
        table.clearSelection();
    }

    // ----- Exit -----
    private void exitApplication() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
    }

    // ----- Main method -----
    public static void main(String[] args) {
        JFrame frame = new JFrame("Product-Supplier Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.add(new ProductSupplierPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
