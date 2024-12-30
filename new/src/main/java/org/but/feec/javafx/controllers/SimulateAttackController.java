package org.but.feec.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.but.feec.javafx.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SimulateAttackController {

    private static final Logger logger = LoggerFactory.getLogger(SimulateAttackController.class);

    @FXML
    private TextArea queryArea;  // Where the user enters queries
    @FXML
    private TextArea resultArea; // Where query results are displayed

    @FXML
    public void handleVulnerableButton(ActionEvent event) {
        try {
            // Create the dummy table
            createDummyTable();

            // Insert fake data
            insertFakeData();

            // Get the query entered by the user
            String query = queryArea.getText();

            // Execute the vulnerable query (direct SQL)
            String result = executeVulnerableQuery(query);

            // Display the result in the result area
            resultArea.setText(result);

        } catch (SQLException e) {
            logger.error("An error occurred during attack simulation (vulnerable).", e);
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleSafeButton(ActionEvent event) {
        try {
            // Create the dummy table
            createDummyTable();

            // Insert fake data
            insertFakeData();

            // Get the query entered by the user
            String query = queryArea.getText();

            // Execute the safe query (using prepared statement)
            String result = executeSafeQuery(query);

            // Display the result in the result area
            resultArea.setText(result);

        } catch (SQLException e) {
            logger.error("An error occurred during attack simulation (safe).", e);
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private TextArea suggestedQueriesArea;

    @FXML
    public void initialize() {
        String suggestedQueries = """
            Suggested queries:
            
            SELECT * FROM dummy_table;
            INSERT INTO dummy_table (name, email, age) VALUES ('Alice', 'alice@example.com', 28);
            DELETE FROM dummy_table WHERE id = 1;
            """;
        suggestedQueriesArea.setText(suggestedQueries);
    }


    private void createDummyTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS mydb.dummy_table (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "email VARCHAR(100), " +
                "age INT" +
                ");";

        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            logger.info("Dummy table created successfully.");
        }
    }

    private void insertFakeData() throws SQLException {
        String checkDataSQL = "SELECT COUNT(*) FROM mydb.dummy_table"; // Check if data exists
        String insertDataSQL = "INSERT INTO mydb.dummy_table (name, email, age) VALUES (?, ?, ?)";

        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // Check if data already exists in the table
            ResultSet rs = stmt.executeQuery(checkDataSQL);
            if (rs.next() && rs.getInt(1) == 0) { // No data exists
                // Insert fake data only if the table is empty
                try (PreparedStatement pstmt = conn.prepareStatement(insertDataSQL)) {
                    // Insert fake data
                    pstmt.setString(1, "John Doe");
                    pstmt.setString(2, "john.doe@example.com");
                    pstmt.setInt(3, 25);
                    pstmt.executeUpdate();

                    pstmt.setString(1, "Jane Smith");
                    pstmt.setString(2, "jane.smith@example.com");
                    pstmt.setInt(3, 30);
                    pstmt.executeUpdate();

                    logger.info("Fake data inserted into dummy table.");
                }
            } else {
                logger.info("Data already exists in the dummy table.");
            }
        }
    }

    private String executeVulnerableQuery(String query) throws SQLException {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // Set the schema search path to your schema (e.g., 'mydb')
            stmt.execute("SET search_path TO mydb");

            // Execute the query
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    result.append("ID: ").append(rs.getInt("id"))
                            .append(", Name: ").append(rs.getString("name"))
                            .append(", Email: ").append(rs.getString("email"))
                            .append(", Age: ").append(rs.getInt("age"))
                            .append("\n");
                }
            }
        }
        return result.toString();
    }


    private String executeSafeQuery(String query) throws SQLException {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // Set the schema search path to your schema (e.g., 'mydb')
            stmt.execute("SET search_path TO mydb");

            // Use prepared statement for safe query execution
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Execute the query
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        result.append("ID: ").append(rs.getInt("id"))
                                .append(", Name: ").append(rs.getString("name"))
                                .append(", Email: ").append(rs.getString("email"))
                                .append(", Age: ").append(rs.getInt("age"))
                                .append("\n");
                    }
                }
            }
        }
        return result.toString();
    }
    public static void dropDummyTable() throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS mydb.dummy_table;";

        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropTableSQL);
            LoggerFactory.getLogger(SimulateAttackController.class).info("Dummy table dropped successfully.");
        }
    }

}
