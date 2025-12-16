package com.retailanalyticalsystem;

import Panels.AdminDashboard;
import Panels.ManagerDashboard;
import Panels.CashierDashboard;
import Panels.CustomerDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

@SuppressWarnings("serial")
public class LoginForm extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, clearButton, exitButton, registerButton;
    private JComboBox<String> roleComboBox;

    // Predefined passwords for each role
    private final HashMap<String, String> rolePasswords = new HashMap<>();

    public LoginForm() {
        setTitle("Retail Analytical System - Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Predefined passwords
        rolePasswords.put("Admin", "admin123");
        rolePasswords.put("Manager", "manager123");
        rolePasswords.put("Cashier", "cashier123");
        rolePasswords.put("Customer", "customer123");

        // ===== Main Panel =====
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== Header =====
        JLabel header = new JLabel("Retail Analytical System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
        mainPanel.add(header, BorderLayout.NORTH);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(new Color(224, 255, 255));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"Admin", "Manager", "Cashier", "Customer"});
        roleComboBox.setBackground(Color.WHITE);
        formPanel.add(roleComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(224, 255, 255));

        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");
        registerButton = new JButton("Register");

        // Button styling
        styleButton(loginButton, new Color(0, 102, 204), Color.WHITE);
        styleButton(clearButton, new Color(255, 215, 0), Color.BLACK);
        styleButton(exitButton, new Color(220, 20, 60), Color.WHITE);
        styleButton(registerButton, new Color(34, 139, 34), Color.WHITE); // green for register

        // Add buttons to panel
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        loginButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);
        registerButton.addActionListener(this);

        add(mainPanel);
        setVisible(true);
    }

    // Button styling method
    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == loginButton) {
            loginAction();
        } else if (src == clearButton) {
            clearAction();
        } else if (src == exitButton) {
            System.exit(0);
        } else if (src == registerButton) {
            registerAction();
        }
    }

    // ===== LOGIN ACTION =====
    private void loginAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String correctPassword = rolePasswords.get(role);

        if (password.equals(correctPassword)) {
            JOptionPane.showMessageDialog(this, "Welcome " + role + " : " + username);
            dispose(); // close login

            // OPEN DASHBOARD
            switch (role) {
                case "Admin":
                    new AdminDashboard();
                    break;
                case "Manager":
                    new ManagerDashboard();
                    break;
                case "Cashier":
                    new CashierDashboard();
                    break;
                case "Customer":
                    new CustomerDashboard();
                    break;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Wrong password for " + role + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== CLEAR ACTION =====
    private void clearAction() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    // ===== REGISTER ACTION =====
    private void registerAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if(username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if user already exists for role
        if(rolePasswords.containsKey(role) && rolePasswords.get(role).equals(password)) {
            JOptionPane.showMessageDialog(this, "User already exists for this role!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            rolePasswords.put(role, password);
            JOptionPane.showMessageDialog(this, "Registered successfully!\nRole: " + role + "\nUsername: " + username);
            clearAction();
        }
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginForm login = new LoginForm();
                login.setVisible(true);
            }
        });
    }
}
