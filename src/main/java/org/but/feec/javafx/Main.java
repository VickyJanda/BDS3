package org.but.feec.javafx;

import org.but.feec.javafx.config.DataSourceConfig;

public class Main {
    public static void main(String[] args) {
        // Initialize the data source
        DataSourceConfig.initializeDataSource(args);

        try {
            PermissionUpdater.updatePermissions();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur
        }

        // Call the PasswordUpdater class to update passwords
        try {
            PasswordUpdater.updatePasswords();  // Replace with actual method
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur
        }

        // Now run the JavaFX app
        App.main(args);
    }
}
