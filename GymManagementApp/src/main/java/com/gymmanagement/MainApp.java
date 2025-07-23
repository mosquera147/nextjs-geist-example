package com.gymmanagement;

import javax.swing.SwingUtilities;
import com.gymmanagement.view.MainFrame;
import com.gymmanagement.util.DatabaseManager;

public class MainApp {
    public static void main(String[] args) {
        // Initialize database
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.connect();
        dbManager.createTables();

        // Launch GUI in Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
