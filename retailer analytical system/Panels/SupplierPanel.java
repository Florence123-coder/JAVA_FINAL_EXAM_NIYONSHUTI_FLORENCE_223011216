package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SupplierPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtContact, txtAddress;
    private JButton btnAdd, btnUpdate, btnDelete, btnExit;

    public SupplierPanel() {
        setLayout(new BorderLayout());

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));

        txtName = new JTextField();
        txtContact = new JTextField();
        txtAddress = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(txtContact);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(txtAddress);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnExit = new JButton("Exit");

        inputPanel.add(btnAdd);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete);
        inputPanel.add(btnExit);

        add(inputPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"SupplierID", "Name", "Contact", "Address", "CreatedAt"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        // ===== BUTTON ACTIONS =====
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSupplier();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSupplier();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });

        // ===== TABLE CLICK =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtContact.setText(
                            model.getValueAt(row, 2) == null ? "" : model.getValueAt(row, 2).toString()
                    );
                    txtAddress.setText(
                            model.getValueAt(row, 3) == null ? "" : model.getValueAt(row, 3).toString()
                    );
                }
            }
        });
    }

    // ===== LOAD DATA =====
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM supplier")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("SupplierID"),
                        rs.getString("Name"),
                        rs.getString("Contact"),
                        rs.getString("Address"),
                        rs.getTimestamp("CreatedAt")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== ADD SUPPLIER =====
    private void addSupplier() {
        String sql = "INSERT INTO supplier (Name, Contact, Address, CreatedAt) VALUES (?,?,?,NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtName.getText());
            ps.setString(2, txtContact.getText());
            ps.setString(3, txtAddress.getText());

            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Supplier added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== UPDATE SUPPLIER =====
    private void updateSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a supplier to update.");
            return;
        }

        String sql = "UPDATE supplier SET Name=?, Contact=?, Address=? WHERE SupplierID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtName.getText());
            ps.setString(2, txtContact.getText());
            ps.setString(3, txtAddress.getText());
            ps.setInt(4, (int) model.getValueAt(row, 0));

            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== DELETE SUPPLIER =====
    private void deleteSupplier() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a supplier to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this supplier?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM supplier WHERE SupplierID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Supplier deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== CLEAR FIELDS =====
    private void clearFields() {
        txtName.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        table.clearSelection();
    }

    // ===== EXIT =====
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Do you really want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
    }
}
