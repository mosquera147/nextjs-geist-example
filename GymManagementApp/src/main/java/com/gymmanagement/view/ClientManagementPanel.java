package com.gymmanagement.view;

import com.gymmanagement.dao.ClientDAO;
import com.gymmanagement.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientManagementPanel extends JPanel {
    private ClientDAO clientDAO;
    private JTable clientTable;
    private DefaultTableModel tableModel;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtFechaInscripcion;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearch;
    private JButton btnSearch;

    private int selectedClientId = -1;

    public ClientManagementPanel() {
        clientDAO = new ClientDAO();
        setLayout(new BorderLayout());

        // Top panel for form inputs
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        formPanel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        formPanel.add(txtApellido);

        formPanel.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        formPanel.add(txtDni);

        formPanel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        formPanel.add(txtTelefono);

        formPanel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        formPanel.add(txtCorreo);

        formPanel.add(new JLabel("Fecha Inscripción (YYYY-MM-DD):"));
        txtFechaInscripcion = new JTextField();
        formPanel.add(txtFechaInscripcion);

        btnAdd = new JButton("Agregar");
        btnUpdate = new JButton("Modificar");
        btnDelete = new JButton("Eliminar");
        btnClear = new JButton("Limpiar");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Buscar");
        searchPanel.add(new JLabel("Buscar por Nombre o DNI:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Table for clients
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Apellido", "DNI", "Teléfono", "Correo", "Fecha Inscripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable directly
            }
        };
        clientTable = new JTable(tableModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(clientTable);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
        add(tableScrollPane, BorderLayout.PAGE_END);

        loadClients();

        btnAdd.addActionListener(e -> addClient());
        btnUpdate.addActionListener(e -> updateClient());
        btnDelete.addActionListener(e -> deleteClient());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchClients());

        clientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedClientId = (int) tableModel.getValueAt(selectedRow, 0);
                    txtNombre.setText((String) tableModel.getValueAt(selectedRow, 1));
                    txtApellido.setText((String) tableModel.getValueAt(selectedRow, 2));
                    txtDni.setText((String) tableModel.getValueAt(selectedRow, 3));
                    txtTelefono.setText((String) tableModel.getValueAt(selectedRow, 4));
                    txtCorreo.setText((String) tableModel.getValueAt(selectedRow, 5));
                    txtFechaInscripcion.setText((String) tableModel.getValueAt(selectedRow, 6));
                }
            }
        });
    }

    private void loadClients() {
        tableModel.setRowCount(0);
        List<Client> clients = clientDAO.getAllClients();
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                    client.getId(),
                    client.getNombre(),
                    client.getApellido(),
                    client.getDni(),
                    client.getTelefono(),
                    client.getCorreo(),
                    client.getFechaInscripcion()
            });
        }
    }

    private void addClient() {
        if (!validateForm()) return;

        Client client = new Client(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtDni.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtFechaInscripcion.getText().trim()
        );

        if (clientDAO.addClient(client)) {
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
            loadClients();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente. Verifique los datos.");
        }
    }

    private void updateClient() {
        if (selectedClientId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para modificar.");
            return;
        }
        if (!validateForm()) return;

        Client client = new Client(
                selectedClientId,
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtDni.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtFechaInscripcion.getText().trim()
        );

        if (clientDAO.updateClient(client)) {
            JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
            loadClients();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar cliente. Verifique los datos.");
        }
    }

    private void deleteClient() {
        if (selectedClientId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el cliente seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (clientDAO.deleteClient(selectedClientId)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                loadClients();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente.");
            }
        }
    }

    private void clearForm() {
        selectedClientId = -1;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtFechaInscripcion.setText("");
        clientTable.clearSelection();
    }

    private void searchClients() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadClients();
            return;
        }
        List<Client> clients = clientDAO.searchClientsByNameOrDni(keyword);
        tableModel.setRowCount(0);
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                    client.getId(),
                    client.getNombre(),
                    client.getApellido(),
                    client.getDni(),
                    client.getTelefono(),
                    client.getCorreo(),
                    client.getFechaInscripcion()
            });
        }
    }

    private boolean validateForm() {
        if (txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() ||
                txtDni.getText().trim().isEmpty() ||
                txtFechaInscripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete los campos obligatorios: Nombre, Apellido, DNI y Fecha de Inscripción.");
            return false;
        }
        // Additional validation can be added here (e.g., email format, date format)
        return true;
    }
}
