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
    private TextArea queryArea;
    @FXML
    private TextArea resultArea;

    @FXML
    public void handleVulnerableButton(ActionEvent event) {
        try {

            createDummyTable();


            insertFakeData();


            String query = queryArea.getText();


            String result = executeVulnerableQuery(query);


            resultArea.setText(result);

        } catch (SQLException e) {
            logger.error("An error occurred during attack simulation (vulnerable).", e);
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleSafeButton(ActionEvent event) {
        try {

            createDummyTable();


            insertFakeData();


            String query = queryArea.getText();


            String result = executeSafeQuery(query);


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
        String checkDataSQL = "SELECT COUNT(*) FROM mydb.dummy_table";
        String insertDataSQL = "INSERT INTO mydb.dummy_table (name, email, age) VALUES (?, ?, ?)";

        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {


            ResultSet rs = stmt.executeQuery(checkDataSQL);
            if (rs.next() && rs.getInt(1) == 0) {

                try (PreparedStatement pstmt = conn.prepareStatement(insertDataSQL)) {

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

            stmt.execute("SET search_path TO mydb");

            if (query.trim().startsWith("INSERT") || query.trim().startsWith("UPDATE") || query.trim().startsWith("DELETE")) {

                int rowsAffected = stmt.executeUpdate(query);
                result.append(rowsAffected).append(" row(s) affected.\n");
            } else {

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
        }
        return result.toString();
    }



    private String executeSafeQuery(String query) throws SQLException {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DataSourceConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SET search_path TO mydb");

            if (query.trim().startsWith("INSERT") || query.trim().startsWith("UPDATE") || query.trim().startsWith("DELETE")) {
                int rowsAffected = stmt.executeUpdate(query);
                result.append(rowsAffected).append(" row(s) affected.\n");
            } else {
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
