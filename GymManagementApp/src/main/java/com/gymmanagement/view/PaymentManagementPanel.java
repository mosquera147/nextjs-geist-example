package com.gymmanagement.view;

import com.gymmanagement.dao.ClientDAO;
import com.gymmanagement.dao.PaymentDAO;
import com.gymmanagement.model.Client;
import com.gymmanagement.model.Payment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PaymentManagementPanel extends JPanel {
    private ClientDAO clientDAO;
    private PaymentDAO paymentDAO;

    private JComboBox<Client> comboClients;
    private JTextField txtFechaPago;
    private JTextField txtMonto;
    private JComboBox<String> comboTipoPago;

    private JButton btnAddPayment;
    private JTable paymentTable;
    private DefaultTableModel paymentTableModel;

    public PaymentManagementPanel() {
        clientDAO = new ClientDAO();
        paymentDAO = new PaymentDAO();

        setLayout(new BorderLayout());

        // Top panel for payment form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Pago"));

        formPanel.add(new JLabel("Cliente:"));
        comboClients = new JComboBox<>();
        loadClients();
        formPanel.add(comboClients);

        formPanel.add(new JLabel("Fecha de Pago (YYYY-MM-DD):"));
        txtFechaPago = new JTextField();
        formPanel.add(txtFechaPago);

        formPanel.add(new JLabel("Monto:"));
        txtMonto = new JTextField();
        formPanel.add(txtMonto);

        formPanel.add(new JLabel("Tipo de Pago:"));
        comboTipoPago = new JComboBox<>(new String[]{"Mensual", "Por Clase"});
        formPanel.add(comboTipoPago);

        btnAddPayment = new JButton("Registrar Pago");
        btnAddPayment.addActionListener(e -> addPayment());

        // Table for payment history
        paymentTableModel = new DefaultTableModel(new Object[]{"ID", "Fecha Pago", "Monto", "Tipo Pago"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentTable = new JTable(paymentTableModel);
        JScrollPane tableScrollPane = new JScrollPane(paymentTable);

        add(formPanel, BorderLayout.NORTH);
        add(btnAddPayment, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        comboClients.addActionListener(e -> loadPaymentsForSelectedClient());
    }

    private void loadClients() {
        comboClients.removeAllItems();
        List<Client> clients = clientDAO.getAllClients();
        for (Client client : clients) {
            comboClients.addItem(client);
        }
    }

    private void addPayment() {
        Client selectedClient = (Client) comboClients.getSelectedItem();
        if (selectedClient == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        String fechaPago = txtFechaPago.getText().trim();
        String montoStr = txtMonto.getText().trim();
        String tipoPago = (String) comboTipoPago.getSelectedItem();

        if (fechaPago.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.");
            return;
        }

        Payment payment = new Payment(selectedClient.getId(), fechaPago, monto, tipoPago);
        if (paymentDAO.addPayment(payment)) {
            JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.");
            loadPaymentsForSelectedClient();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el pago.");
        }
    }

    private void loadPaymentsForSelectedClient() {
        paymentTableModel.setRowCount(0);
        Client selectedClient = (Client) comboClients.getSelectedItem();
        if (selectedClient == null) return;

        List<Payment> payments = paymentDAO.getPaymentsByClientId(selectedClient.getId());
        for (Payment payment : payments) {
            paymentTableModel.addRow(new Object[]{
                    payment.getId(),
                    payment.getFechaPago(),
                    payment.getMonto(),
                    payment.getTipoPago()
            });
        }
    }

    private void clearForm() {
        txtFechaPago.setText("");
        txtMonto.setText("");
        comboTipoPago.setSelectedIndex(0);
    }
}
