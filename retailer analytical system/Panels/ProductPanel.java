package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import Panels.DBConnection;

public class ProductPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtDescription, txtCategory, txtPrice, txtStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnExit;

    public ProductPanel() {
        setLayout(new BorderLayout());

               JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        txtName = new JTextField();
        txtDescription = new JTextField();
        txtCategory = new JTextField();
        txtPrice = new JTextField();
        txtStatus = new JTextField();

        inputPanel.add(new JLabel("Name:")); inputPanel.add(txtName);
        inputPanel.add(new JLabel("Description:")); inputPanel.add(txtDescription);
        inputPanel.add(new JLabel("Category:")); inputPanel.add(txtCategory);
        inputPanel.add(new JLabel("Price/Value:")); inputPanel.add(txtPrice);
        inputPanel.add(new JLabel("Status:")); inputPanel.add(txtStatus);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnExit = new JButton("Exit");

        inputPanel.add(btnAdd);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete);
        inputPanel.add(btnExit);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ProductID", "Name", "Description", "Category", "PriceOrValue", "Status", "CreatedAt"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

     // ----- Button Actions -----
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });

        // ----- Table Click -----
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtDescription.setText(model.getValueAt(row, 2).toString());
                    txtCategory.setText(model.getValueAt(row, 3).toString());
                    txtPrice.setText(model.getValueAt(row, 4).toString());
                    txtStatus.setText(model.getValueAt(row, 5).toString());
                }
            }
        });
    }

    // ----- Load Data from Database -----
    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Product")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("Category"),
                        rs.getDouble("PriceOrValue"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedAt")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ----- Add Product -----
    private void addProduct() {
        String sql = "INSERT INTO Product (Name, Description, Category, PriceOrValue, Status, CreatedAt) VALUES (?,?,?,?,?,NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtName.getText());
            ps.setString(2, txtDescription.getText());
            ps.setString(3, txtCategory.getText());
            ps.setDouble(4, Double.parseDouble(txtPrice.getText()));
            ps.setString(5, txtStatus.getText());
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Product added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Price/Value.");
        }
    }

    // ----- Update Product -----
    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to update.");
            return;
        }

        String sql = "UPDATE Product SET Name=?, Description=?, Category=?, PriceOrValue=?, Status=? WHERE ProductID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtName.getText());
            ps.setString(2, txtDescription.getText());
            ps.setString(3, txtCategory.getText());
            ps.setDouble(4, Double.parseDouble(txtPrice.getText()));
            ps.setString(5, txtStatus.getText());
            ps.setInt(6, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Price/Value.");
        }
    }

    // ----- Delete Product -----
    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?");
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Product WHERE ProductID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int) model.getValueAt(row, 0));
            ps.executeUpdate();
            loadData();
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ----- Clear Fields -----
    private void clearFields() {
        txtName.setText("");
        txtDescription.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtStatus.setText("");
        table.clearSelection();
    }

    // ----- Exit Application -----
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this, "Do you really want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
    }

    // ----- For testing -----
    public static void main(String[] args) {
        JFrame frame = new JFrame("Product Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.add(new ProductPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
