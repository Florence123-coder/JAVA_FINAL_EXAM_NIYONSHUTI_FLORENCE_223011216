package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import Panels.DBConnection; // Adjust this path if needed

public class InventoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtProductID, txtStoreID, txtQuantity;
    private JButton btnAdd, btnUpdate, btnDelete, btnExit;

    public InventoryPanel() {
        setLayout(new BorderLayout());

        // ----- Input Panel -----
        JPanel input = new JPanel(new GridLayout(4, 2, 10, 10));

        txtProductID = new JTextField();
        txtStoreID = new JTextField();
        txtQuantity = new JTextField();

        input.add(new JLabel("Product ID:")); input.add(txtProductID);
        input.add(new JLabel("Store ID:")); input.add(txtStoreID);
        input.add(new JLabel("Quantity:")); input.add(txtQuantity);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnExit = new JButton("Exit"); // Changed from Clear to Exit

        input.add(btnAdd);
        input.add(btnUpdate);
        input.add(btnDelete);
        input.add(btnExit);

        add(input, BorderLayout.NORTH);

        // ----- Table -----
        model = new DefaultTableModel(
            new String[]{"InventoryID", "ProductID", "StoreID", "Quantity", "CreatedAt"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        // ----- Button Actions -----
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addInventory();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateInventory();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteInventory();
            }
        });

        // Exit button closes the window
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });

        // ----- Table click -----
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtProductID.setText(model.getValueAt(row, 1).toString());
                    txtStoreID.setText(model.getValueAt(row, 2).toString());
                    txtQuantity.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
    }

    // ----- Load data -----
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM inventory")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("InventoryID"),
                    rs.getInt("ProductID"),
                    rs.getInt("StoreID"),
                    rs.getInt("Quantity"),
                    rs.getTimestamp("CreatedAt")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ----- Add Inventory -----
    private void addInventory() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO inventory (ProductID, StoreID, Quantity, CreatedAt) VALUES (?,?,?,NOW())")) {
            ps.setInt(1, Integer.parseInt(txtProductID.getText()));
            ps.setInt(2, Integer.parseInt(txtStoreID.getText()));
            ps.setInt(3, Integer.parseInt(txtQuantity.getText()));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Inventory added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for IDs and Quantity.");
        }
    }

    // ----- Update Inventory -----
    private void updateInventory() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to update.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE inventory SET ProductID=?, StoreID=?, Quantity=? WHERE InventoryID=?")) {
            ps.setInt(1, Integer.parseInt(txtProductID.getText()));
            ps.setInt(2, Integer.parseInt(txtStoreID.getText()));
            ps.setInt(3, Integer.parseInt(txtQuantity.getText()));
            ps.setInt(4, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Inventory updated successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for IDs and Quantity.");
        }
    }

    // ----- Delete Inventory -----
    private void deleteInventory() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this inventory?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM inventory WHERE InventoryID=?")) {
            ps.setInt(1, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadData();
            JOptionPane.showMessageDialog(this, "Inventory deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ----- Clear Fields -----
    private void clearFields() {
        txtProductID.setText("");
        txtStoreID.setText("");
        txtQuantity.setText("");
        table.clearSelection();
    }

    // ----- Exit Application -----
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose(); // Close the window
            }
        }
    }

    // ----- Test run -----
    public static void main(String[] args) {
        JFrame f = new JFrame("Inventory Panel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 500);
        f.add(new InventoryPanel());
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
