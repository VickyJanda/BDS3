package org.but.feec.javafx;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.but.feec.javafx.config.DataSourceConfig;

import java.sql.*;
import static org.but.feec.javafx.services.Argon2FactoryService.ARGON2;

public class PasswordUpdater {

    public static String hashPassword(String password) {
        return ARGON2.hash(10, 65536, 1, password.toCharArray());
    }

    public static void updatePasswords() {
        // Connect to the database

        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT user_id, password FROM mydb.user WHERE password NOT LIKE '$argon2id$%' AND LENGTH(password) < 95")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String plaintextPassword = resultSet.getString("password");
                String hashedPassword = hashPassword(plaintextPassword);

                // Update the password hash in the database
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE mydb.user SET password = ? WHERE user_id = ?");
                updateStmt.setString(1, hashedPassword);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        updatePasswords();
        System.out.println("Passwords updated");
    }
}

