package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SalePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtInventoryID, txtCustomerID, txtOrderNumber, txtStatus, txtTotalAmount, txtPaymentMethod, txtNotes;
    private JButton btnAdd, btnUpdate, btnDelete, btnExit;

    public SalePanel() {
        setLayout(new BorderLayout());

        // ----- Input Panel -----
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Sale Details"));

        txtInventoryID = new JTextField();
        txtCustomerID = new JTextField();
        txtOrderNumber = new JTextField();
        txtStatus = new JTextField();
        txtTotalAmount = new JTextField();
        txtPaymentMethod = new JTextField();
        txtNotes = new JTextField();

        inputPanel.add(new JLabel("Inventory ID:")); inputPanel.add(txtInventoryID);
        inputPanel.add(new JLabel("Customer ID:")); inputPanel.add(txtCustomerID);
        inputPanel.add(new JLabel("Order Number:")); inputPanel.add(txtOrderNumber);
        inputPanel.add(new JLabel("Status:")); inputPanel.add(txtStatus);
        inputPanel.add(new JLabel("Total Amount:")); inputPanel.add(txtTotalAmount);
        inputPanel.add(new JLabel("Payment Method:")); inputPanel.add(txtPaymentMethod);
        inputPanel.add(new JLabel("Notes:")); inputPanel.add(txtNotes);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnExit = new JButton("Exit");

        inputPanel.add(btnAdd); inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete); inputPanel.add(btnExit);

        add(inputPanel, BorderLayout.NORTH);

        // ----- Table -----
        model = new DefaultTableModel(new String[]{
                "SaleID","InventoryID","CustomerID","OrderNumber","Date","Status","TotalAmount","PaymentMethod","Notes"
        }, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addSale();
            }
        });

        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateSale();
            }
        });

        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteSale();
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
                if(row >= 0){
                    txtInventoryID.setText(model.getValueAt(row,1).toString());
                    txtCustomerID.setText(model.getValueAt(row,2).toString());
                    txtOrderNumber.setText(model.getValueAt(row,3).toString());
                    txtStatus.setText(model.getValueAt(row,5).toString());
                    txtTotalAmount.setText(model.getValueAt(row,6).toString());
                    txtPaymentMethod.setText(model.getValueAt(row,7).toString());
                    txtNotes.setText(model.getValueAt(row,8).toString());
                }
            }
        });
    }

    // ----- Load Data -----
    private void loadData(){
        model.setRowCount(0);
        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM sale")) {

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("SaleID"),
                        rs.getInt("InventoryID"),
                        rs.getInt("CustomerID"),
                        rs.getString("OrderNumber"),
                        rs.getTimestamp("Date"),
                        rs.getString("Status"),
                        rs.getDouble("TotalAmount"),
                        rs.getString("PaymentMethod"),
                        rs.getString("Notes")
                });
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Add Sale -----
    private void addSale(){
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO sale (InventoryID, CustomerID, OrderNumber, Date, Status, TotalAmount, PaymentMethod, Notes) " +
                            "VALUES (?,?,?,NOW(),?,?,?,?)")) {

            ps.setInt(1,Integer.parseInt(txtInventoryID.getText()));
            ps.setInt(2,Integer.parseInt(txtCustomerID.getText()));
            ps.setString(3, txtOrderNumber.getText());
            ps.setString(4, txtStatus.getText());
            ps.setDouble(5, Double.parseDouble(txtTotalAmount.getText()));
            ps.setString(6, txtPaymentMethod.getText());
            ps.setString(7, txtNotes.getText());

            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this,"Sale added successfully!");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Enter valid numbers for InventoryID, CustomerID, TotalAmount");
        }
    }

    // ----- Update Sale -----
    private void updateSale(){
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this,"Select a sale to update."); return; }

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE sale SET InventoryID=?, CustomerID=?, OrderNumber=?, Status=?, TotalAmount=?, PaymentMethod=?, Notes=? WHERE SaleID=?")) {

            ps.setInt(1,Integer.parseInt(txtInventoryID.getText()));
            ps.setInt(2,Integer.parseInt(txtCustomerID.getText()));
            ps.setString(3, txtOrderNumber.getText());
            ps.setString(4, txtStatus.getText());
            ps.setDouble(5, Double.parseDouble(txtTotalAmount.getText()));
            ps.setString(6, txtPaymentMethod.getText());
            ps.setString(7, txtNotes.getText());
            ps.setInt(8,(int) model.getValueAt(row,0));

            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this,"Sale updated successfully!");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Delete Sale -----
    private void deleteSale(){
        int row = table.getSelectedRow();
        if(row<0){ JOptionPane.showMessageDialog(this,"Select a sale to delete."); return; }

        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this sale?");
        if(confirm != JOptionPane.YES_OPTION) return;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM sale WHERE SaleID=?")) {

            ps.setInt(1,(int) model.getValueAt(row,0));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this,"Sale deleted successfully!");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Clear -----
    private void clearFields(){
        txtInventoryID.setText(""); txtCustomerID.setText(""); txtOrderNumber.setText("");
        txtStatus.setText(""); txtTotalAmount.setText(""); txtPaymentMethod.setText(""); txtNotes.setText("");
        table.clearSelection();
    }

    // ----- Exit -----
    private void exitApplication(){
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Sale Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,500);
        frame.add(new SalePanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
