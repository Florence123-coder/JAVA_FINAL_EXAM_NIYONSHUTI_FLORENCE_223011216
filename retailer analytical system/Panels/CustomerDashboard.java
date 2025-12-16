package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.retailanalyticalsystem.LoginForm;

public class CustomerDashboard extends JFrame {

    public CustomerDashboard() {

        setTitle("Customer Dashboard - Retail Analytical System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ================= MAIN PANEL =================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 204)); // Blue header
        header.setPreferredSize(new Dimension(1000, 70));

        JLabel titleLabel = new JLabel("  CUSTOMER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // SEARCH BAR
        final JTextField searchField = new JTextField(20);
        final JButton searchBtn = new JButton("Search");

        // LOGOUT BUTTON
        final JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(searchField);
        rightPanel.add(searchBtn);
        rightPanel.add(logoutBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        // ================= TABBED PANE =================
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Products", new ProductPanel());
        tabs.addTab("Orders", new OrdersPanel());

        tabs.setBackground(new Color(230, 240, 255)); // same as Admin
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ================= ADD TO MAIN PANEL =================
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);

        // ================= LOGOUT ACTION =================
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(logoutBtn);
                    frame.dispose();
                    new LoginForm().setVisible(true);
                }
            }
        });

        // ================= SEARCH ACTION =================
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchField.getText().trim();

                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter something to search",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // Currently simple dialog; can be extended to search tables later
                    JOptionPane.showMessageDialog(null,
                            "You searched for: " + text,
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerDashboard();
            }
        });
    }
}
