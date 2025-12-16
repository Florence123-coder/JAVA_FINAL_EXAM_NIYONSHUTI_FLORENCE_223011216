package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerPanel extends JPanel {
    private JTextField txtUsername, txtEmail, txtFullName, txtRole;
    private JPasswordField txtPassword;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;

    // Database Configuration - CHECK THESE VALUES
    private final String DB_URL = "jdbc:mysql://localhost:3308/retailer_system";
    private final String USER = "root";
    private final String PASS = ""; 

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Input Panel (North)
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Changed to 6 rows, 2 columns for better layout
        inputPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        inputPanel.setPreferredSize(new Dimension(800, 200)); // Give it a fixed height

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtEmail = new JTextField();
        txtFullName = new JTextField();
        txtRole = new JTextField();

        inputPanel.add(new JLabel("Username:")); inputPanel.add(txtUsername);
        inputPanel.add(new JLabel("Password:")); inputPanel.add(txtPassword);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Full Name:")); inputPanel.add(txtFullName);
        inputPanel.add(new JLabel("Role:")); inputPanel.add(txtRole);
        
        // Button Panel inside Input Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAdd = new JButton("Add"); btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete"); btnClear = new JButton("Clear");
        
        buttonPanel.add(btnAdd); buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete); buttonPanel.add(btnClear);
        
        inputPanel.add(new JLabel("")); // Empty label for spacing
        inputPanel.add(buttonPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Table (Center)
        tableModel = new DefaultTableModel(new String[]{"ID","Username","Email","FullName","Role","CreatedAt","LastLogin"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load data immediately on panel creation
        loadData();

        // Attach Action Listeners
     // Corrected button actions using anonymous inner classes

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        // Table click to populate fields
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0){
                    // Note: Skipping Password (column 2 in DB, but not in tableModel) for security reasons
                    txtUsername.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    txtFullName.setText(tableModel.getValueAt(row, 3).toString());
                    txtRole.setText(tableModel.getValueAt(row, 4).toString());
                    txtPassword.setText(""); // Clear password field for safety
                }
            }
        });
    }

    private Connection getConnection() throws SQLException {
        // Register the driver only once if needed (modern JDBC often does this automatically)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure the JAR is in the classpath.");
            throw new SQLException("JDBC Driver Error", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        String query = "SELECT CustomerID, Username, Email, FullName, Role, CreatedAt, LastLogin FROM Customer";
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()){
                tableModel.addRow(new Object[]{
                    rs.getInt("CustomerID"),
                    rs.getString("Username"),
                    rs.getString("Email"),
                    rs.getString("FullName"),
                    rs.getString("Role"),
                    rs.getTimestamp("CreatedAt"),
                    rs.getTimestamp("LastLogin")
                });
            }
        } catch(SQLException ex) {
            // *** CRUCIAL DEBUGGING STEP: Display the real error ***
            JOptionPane.showMessageDialog(this, 
                "Database Load Error: " + ex.getMessage() + 
                "\n\nCheck if MySQL is running on port 3308 and if the JDBC driver is in the classpath.", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Print stack trace to the console
        }
    }

    private void addCustomer() {
        // Simple validation
        if (txtUsername.getText().isEmpty() || txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.");
            return;
        }
        
        // NOTE: Use proper password hashing in a production system (e.g., BCrypt)!
        String sql = "INSERT INTO Customer (Username, PasswordHash, Email, FullName, Role, CreatedAt) VALUES (?, SHA2(?, 256), ?, ?, ?, NOW())";
        
        executeUpdate(sql,
                txtUsername.getText(),
                new String(txtPassword.getPassword()), // Send plaintext to SHA2 hash function
                txtEmail.getText(),
                txtFullName.getText(), 
                txtRole.getText());
    }

    private void updateCustomer() {
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this, "Select a customer to update."); return; }
        
        String sql;
        String password = new String(txtPassword.getPassword());
        Object[] params;

        // Check if password field is filled (user wants to change password)
        if(password.isEmpty()){
             sql = "UPDATE Customer SET Username=?, Email=?, FullName=?, Role=? WHERE CustomerID=?";
             params = new Object[]{
                 txtUsername.getText(), txtEmail.getText(), txtFullName.getText(), 
                 txtRole.getText(), tableModel.getValueAt(row, 0)
             };
        } else {
            // Update password using SHA2 (NOTE: Use BCrypt instead of SHA2 for real security)
            sql = "UPDATE Customer SET Username=?, PasswordHash=SHA2(?, 256), Email=?, FullName=?, Role=? WHERE CustomerID=?";
            params = new Object[]{
                txtUsername.getText(), password, txtEmail.getText(), txtFullName.getText(), 
                txtRole.getText(), tableModel.getValueAt(row, 0)
            };
        }

        executeUpdate(sql, params);
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this, "Select a customer to delete."); return; }
        if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Deletion", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        
        executeUpdate("DELETE FROM Customer WHERE CustomerID=?", tableModel.getValueAt(row, 0));
    }

    private void executeUpdate(String sql, Object... params){
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            for(int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            loadData(); 
            clearFields();
            JOptionPane.showMessageDialog(this, "Operation successful!");
        } catch(SQLException ex){ 
            JOptionPane.showMessageDialog(this, "Database Operation Failed: " + ex.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE); 
            ex.printStackTrace();
        }
    }

    private void clearFields(){
        txtUsername.setText(""); 
        txtPassword.setText(""); 
        txtEmail.setText("");
        txtFullName.setText(""); 
        txtRole.setText("");
        table.clearSelection();
    }

    // Main method for testing this panel independently
    public static void main(String[] args){
        // Ensure application look and feel is set (optional, but good practice)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JFrame frame = new JFrame("Customer Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); // Increased size for better view
        frame.add(new CustomerPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}