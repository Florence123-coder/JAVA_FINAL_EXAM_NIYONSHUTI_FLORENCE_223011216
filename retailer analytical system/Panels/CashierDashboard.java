package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.retailanalyticalsystem.LoginForm;

public class CashierDashboard extends JFrame {

    public CashierDashboard() {

        setTitle("Cashier Dashboard - Retail Analytical System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 153, 76)); // Green color for cashier
        header.setPreferredSize(new Dimension(1000, 70));

        JLabel title = new JLabel("  CASHIER DASHBOARD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        // SEARCH BAR
        final JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");

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

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        // ================= TABS =================
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Sales", new SalePanel());
        tabs.addTab("Reports", new ReportPanel());

        // Add color to tabs
        tabs.setBackground(new Color(230, 255, 240));
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ================= ADD TO FRAME =================
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
                    JOptionPane.showMessageDialog(null,
                            "You searched for: " + text,
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}
