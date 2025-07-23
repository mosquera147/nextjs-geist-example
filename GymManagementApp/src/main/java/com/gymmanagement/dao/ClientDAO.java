package com.gymmanagement.dao;

import com.gymmanagement.model.Client;
import com.gymmanagement.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private Connection connection;

    public ClientDAO() {
        connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addClient(Client client) {
        String sql = "INSERT INTO clients (nombre, apellido, dni, telefono, correo, fecha_inscripcion) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getNombre());
            pstmt.setString(2, client.getApellido());
            pstmt.setString(3, client.getDni());
            pstmt.setString(4, client.getTelefono());
            pstmt.setString(5, client.getCorreo());
            pstmt.setString(6, client.getFechaInscripcion());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateClient(Client client) {
        String sql = "UPDATE clients SET nombre = ?, apellido = ?, dni = ?, telefono = ?, correo = ?, fecha_inscripcion = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getNombre());
            pstmt.setString(2, client.getApellido());
            pstmt.setString(3, client.getDni());
            pstmt.setString(4, client.getTelefono());
            pstmt.setString(5, client.getCorreo());
            pstmt.setString(6, client.getFechaInscripcion());
            pstmt.setInt(7, client.getId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClient(int clientId) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractClientFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> searchClientsByNameOrDni(String keyword) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nombre LIKE ? OR apellido LIKE ? OR dni LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            pstmt.setString(3, likeKeyword);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                clients.add(extractClientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                clients.add(extractClientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private Client extractClientFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNombre(rs.getString("nombre"));
        client.setApellido(rs.getString("apellido"));
        client.setDni(rs.getString("dni"));
        client.setTelefono(rs.getString("telefono"));
        client.setCorreo(rs.getString("correo"));
        client.setFechaInscripcion(rs.getString("fecha_inscripcion"));
        return client;
    }
}
