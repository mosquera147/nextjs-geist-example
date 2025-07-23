package com.gymmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private static final String DB_URL = "jdbc:sqlite:gymmanagement.db";

    private DatabaseManager() {
        // private constructor for singleton
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTables() {
        String createClientsTable = "CREATE TABLE IF NOT EXISTS clients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "dni TEXT UNIQUE NOT NULL," +
                "telefono TEXT," +
                "correo TEXT," +
                "fecha_inscripcion TEXT NOT NULL" +
                ");";

        String createPaymentsTable = "CREATE TABLE IF NOT EXISTS payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "client_id INTEGER NOT NULL," +
                "fecha_pago TEXT NOT NULL," +
                "monto REAL NOT NULL," +
                "tipo_pago TEXT NOT NULL," +
                "FOREIGN KEY(client_id) REFERENCES clients(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createClientsTable);
            stmt.execute(createPaymentsTable);
            System.out.println("Tables created or already exist.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
