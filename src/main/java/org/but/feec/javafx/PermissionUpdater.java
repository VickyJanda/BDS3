package org.but.feec.javafx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.but.feec.javafx.config.DataSourceConfig;

public class PermissionUpdater {

    public static void updatePermissions() {
        // SQL commands to grant permissions
        String grantAllOnSchema = "GRANT ALL PRIVILEGES ON SCHEMA mydb TO test;";
        String grantAllOnTable = "GRANT ALL PRIVILEGES ON TABLE mydb.user TO test;";
        String grantUsageOnSequence = "GRANT USAGE, SELECT ON SEQUENCE mydb.user_user_id_seq TO test;";

        // Get the database connection from DataSourceConfig
        try (Connection connection = DataSourceConfig.getConnection()) {
            // Execute each SQL command
            executeGrantCommand(connection, grantAllOnSchema);
            executeGrantCommand(connection, grantAllOnTable);
            executeGrantCommand(connection, grantUsageOnSequence);

            System.out.println("Permissions granted successfully.");
        } catch (SQLException e) {
            System.out.println("Error while executing SQL commands: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeGrantCommand(Connection connection, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate(); // Execute the GRANT command
        }
    }
}

