package com.gymmanagement.dao;

import com.gymmanagement.model.Payment;
import com.gymmanagement.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection connection;

    public PaymentDAO() {
        connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (client_id, fecha_pago, monto, tipo_pago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getClientId());
            pstmt.setString(2, payment.getFechaPago());
            pstmt.setDouble(3, payment.getMonto());
            pstmt.setString(4, payment.getTipoPago());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Payment> getPaymentsByClientId(int clientId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE client_id = ? ORDER BY fecha_pago DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public boolean isClientUpToDate(int clientId) {
        // For simplicity, assume monthly payments and check if last payment is within last 30 days
        String sql = "SELECT fecha_pago FROM payments WHERE client_id = ? ORDER BY fecha_pago DESC LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String lastPaymentDate = rs.getString("fecha_pago");
                // Compare lastPaymentDate with current date (simple string compare or parse date)
                // For now, just return true as placeholder
                return true;
            } else {
                return false; // no payments found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setClientId(rs.getInt("client_id"));
        payment.setFechaPago(rs.getString("fecha_pago"));
        payment.setMonto(rs.getDouble("monto"));
        payment.setTipoPago(rs.getString("tipo_pago"));
        return payment;
    }
}
