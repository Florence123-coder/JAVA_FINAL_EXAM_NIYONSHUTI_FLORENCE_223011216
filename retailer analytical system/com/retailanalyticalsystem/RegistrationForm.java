package com.retailanalyticalsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

@SuppressWarnings("serial")
public class RegistrationForm extends JFrame implements ActionListener {

    private JTextField usernameField, emailField, fullNameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationForm() {
        setTitle("Retail Analytics System - Registration");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        // Add form labels and fields
        panel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        panel.add(fullNameField);

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        panel.add(new JLabel()); // empty cell
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Basic validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Connection conn = Databaseconnection.getConnection();
            String sql = "INSERT INTO customer (FullName, Username, Email, PasswordHash) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, fullName);
            pst.setString(2, username);
            pst.setString(3, email);
            pst.setString(4, password); // Ideally hash passwords

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                this.dispose(); // close registration form
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Try again.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database.");
        }
    }

    // Main method to run the registration form independently
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    	    @Override
    	    public void run() {
    	        new RegistrationForm();
    	    }
    	});

    }
}
