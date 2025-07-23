package com.gymmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Gym Management Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenuBar();
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        showDashboard();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuClients = new JMenu("Clientes");
        JMenuItem menuItemManageClients = new JMenuItem("Gestionar Clientes");
        menuItemManageClients.addActionListener(e -> showClientManagement());
        menuClients.add(menuItemManageClients);

        JMenu menuPayments = new JMenu("Pagos");
        JMenuItem menuItemManagePayments = new JMenuItem("Gestionar Pagos");
        menuItemManagePayments.addActionListener(e -> showPaymentManagement());
        menuPayments.add(menuItemManagePayments);

        JMenu menuReports = new JMenu("Reportes");
        JMenuItem menuItemDashboard = new JMenuItem("Dashboard");
        menuItemDashboard.addActionListener(e -> showDashboard());
        menuReports.add(menuItemDashboard);

        menuBar.add(menuClients);
        menuBar.add(menuPayments);
        menuBar.add(menuReports);

        setJMenuBar(menuBar);
    }

    private void showDashboard() {
        contentPanel.removeAll();
        contentPanel.add(new DashboardPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showClientManagement() {
        contentPanel.removeAll();
        contentPanel.add(new ClientManagementPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPaymentManagement() {
        contentPanel.removeAll();
        contentPanel.add(new PaymentManagementPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
