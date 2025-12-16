package Panels;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import com.retailanalyticalsystem.LoginForm;

/**
 * AdminDashboard with working Search (tab-title matching + JTable filtering)
 * No changes required to other panel classes.
 */
public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard - Retail Analytical System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0,102,204));
        header.setPreferredSize(new Dimension(1000, 70));

        JLabel title = new JLabel("  ADMIN DASHBOARD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

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

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        // ================= TABS =================
        final JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Users", new UsersPanel());
        tabs.addTab("Products", new ProductPanel());
        tabs.addTab("Supplier", new SupplierPanel());
        tabs.addTab("Customers", new CustomerPanel());
        tabs.addTab("Inventory", new InventoryPanel());
        tabs.addTab("Sales", new SalePanel());

        // Add color to tabs
        tabs.setBackground(new Color(230, 240, 255));
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ================= ADD TO FRAME =================
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);

        // ----------------- Search button action (method-style) -----------------
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                performSearch(tabs, searchField.getText().trim());
            }
        });

        // ----------------- Logout action (method-style) -----------------
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

                    new com.retailanalyticalsystem.LoginForm().setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    /**
     * Perform search across tab titles and tables inside tabs.
     * This method is defensive: if a table doesn't support filtering it will skip it.
     *
     * @param tabs  the JTabbedPane to search
     * @param query the user query
     */
    private void performSearch(JTabbedPane tabs, String query) {
        if (query == null || query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Type something to search.", "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String qLower = query.toLowerCase();

        // 1) Try to match a tab title
        for (int i = 0; i < tabs.getTabCount(); i++) {
            String title = tabs.getTitleAt(i);
            if (title != null && title.toLowerCase().contains(qLower)) {
                tabs.setSelectedIndex(i);
                JOptionPane.showMessageDialog(this, "Opened tab: " + title, "Search", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // 2) If no tab title match, try to find and filter JTable inside each tab
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            boolean foundMatchInTab = searchAndFilterTables(comp, query);
            if (foundMatchInTab) {
                tabs.setSelectedIndex(i);
                JOptionPane.showMessageDialog(this, "Found matching rows in tab: " + tabs.getTitleAt(i), "Search", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // 3) Nothing found
        JOptionPane.showMessageDialog(this, "No results found for: \"" + query + "\"", "Search", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Recursively search for JTable components inside the given component.
     * If a JTable is found and its model supports filtering with TableRowSorter,
     * this method applies a RowFilter searching all columns for the query.
     *
     * @param comp  the component to scan
     * @param query the search query
     * @return true if any table had at least one matching row after filtering
     */
    private boolean searchAndFilterTables(Component comp, String query) {
        if (comp == null) return false;

        // If component is a JTable, attempt to filter it
        if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            return applyFilterToTable(table, query);
        }

        // If component is a JScrollPane containing a JTable
        if (comp instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) comp;
            JViewport vp = sp.getViewport();
            Component view = vp.getView();
            if (view instanceof JTable) {
                return applyFilterToTable((JTable) view, query);
            }
        }

        // If it's a container, recurse children
        if (comp instanceof Container) {
            Component[] children = ((Container) comp).getComponents();
            for (Component child : children) {
                boolean found = searchAndFilterTables(child, query);
                if (found) return true;
            }
        }

        return false;
    }

    /**
     * Try to attach a TableRowSorter to the table and filter rows that contain the query.
     * Returns true if a match exists after filtering.
     */
    private boolean applyFilterToTable(JTable table, String query) {
        try {
            TableModel model = table.getModel();
            // If model isn't suitable, skip
            if (model == null) return false;

            // Create or reuse a TableRowSorter
            TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
            if (sorter == null) {
                sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);
            } else {
                // ensure sorter has model set
                sorter.setModel(model);
            }

            // Create a RowFilter that checks all columns for the query (case-insensitive)
            final String regex = "(?i).*" + Pattern.quote(query) + ".*";
            RowFilter<TableModel, Object> rf = RowFilter.regexFilter(regex);

            sorter.setRowFilter(rf);

            // Check if any rows match (converted row index exists)
            if (table.getRowCount() > 0) {
                return true;
            }

        } catch (Exception ex) {
            // If anything fails (custom model, security, etc) just ignore and continue
            // Do not propagate exceptions so UI stays stable
        }
        return false;
    }
}
