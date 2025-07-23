package com.gymmanagement.view;

import com.gymmanagement.dao.ClientDAO;
import com.gymmanagement.dao.PaymentDAO;
import com.gymmanagement.model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private JLabel totalClientsLabel;
    private JLabel clientsUpToDateLabel;
    private JLabel clientsPendingPaymentsLabel;

    private ClientDAO clientDAO;
    private PaymentDAO paymentDAO;

    public DashboardPanel() {
        clientDAO = new ClientDAO();
        paymentDAO = new PaymentDAO();

        setLayout(new GridLayout(3, 1, 10, 10));
        totalClientsLabel = new JLabel();
        clientsUpToDateLabel = new JLabel();
        clientsPendingPaymentsLabel = new JLabel();

        totalClientsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        clientsUpToDateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        clientsPendingPaymentsLabel.setFont(new Font("Arial", Font.BOLD, 18));

        add(totalClientsLabel);
        add(clientsUpToDateLabel);
        add(clientsPendingPaymentsLabel);

        loadData();
    }

    private void loadData() {
        List<Client> clients = clientDAO.getAllClients();
        int totalClients = clients.size();
        int upToDateCount = 0;
        int pendingCount = 0;

        for (Client client : clients) {
            if (paymentDAO.isClientUpToDate(client.getId())) {
                upToDateCount++;
            } else {
                pendingCount++;
            }
        }

        totalClientsLabel.setText("Total de Clientes: " + totalClients);
        clientsUpToDateLabel.setText("Clientes al Día: " + upToDateCount);
        clientsPendingPaymentsLabel.setText("Clientes con Pagos Pendientes: " + pendingCount);
    }
}
