package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsersPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtUsername, txtPassword;
    private JButton btnAdd, btnUpdate, btnDelete, btnExit;

    public UsersPanel() {
        setLayout(new BorderLayout());

        // ----- Input Panel -----
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        txtUsername = new JTextField();
        txtPassword = new JTextField();

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtUsername);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtPassword);

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
        model = new DefaultTableModel(new String[]{"ID", "Username", "Password"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addUser();
            }
        });

        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateUser();
            }
        });

        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteUser();
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
                    txtUsername.setText(model.getValueAt(row,1).toString());
                    txtPassword.setText(model.getValueAt(row,2).toString());
                }
            }
        });
    }

    // ----- Load Data -----
    private void loadData(){
        model.setRowCount(0);
        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users")) {

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                });
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Add User -----
    private void addUser(){
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?,?)")) {

            ps.setString(1, txtUsername.getText());
            ps.setString(2, txtPassword.getText());
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this,"User added successfully!");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Update User -----
    private void updateUser(){
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this,"Select a user to update."); return; }

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET username=?, password=? WHERE id=?")) {

            ps.setString(1, txtUsername.getText());
            ps.setString(2, txtPassword.getText());
            ps.setInt(3, (int) model.getValueAt(row,0));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this,"User updated successfully!");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Delete User -----
    private void deleteUser(){
        int row = table.getSelectedRow();
        if(row<0){ JOptionPane.showMessageDialog(this,"Select a user to delete."); return; }

        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this user?");
        if(confirm != JOptionPane.YES_OPTION) return;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {

            ps.setInt(1,(int) model.getValueAt(row,0));
            ps.executeUpdate();
            loadData();
            clearFields();
            JOptionPane.showMessageDialog(this,"User deleted successfully!");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    // ----- Clear Fields -----
    private void clearFields(){
        txtUsername.setText(""); txtPassword.setText("");
        table.clearSelection();
    }

    // ----- Exit -----
    private void exitApplication(){
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
    }

    // ----- Main method -----
    public static void main(String[] args){
        JFrame frame = new JFrame("Users Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.add(new UsersPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
