package com.retailanalyticalsystem;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Start the Retail Analytics System safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Starting Retail Analytics System...");
                new LoginForm(); // Open the login form
            }
        });
    }
}
