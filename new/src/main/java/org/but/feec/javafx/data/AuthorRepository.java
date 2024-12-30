package org.but.feec.javafx.data;

import org.but.feec.javafx.api.*;
import org.but.feec.javafx.config.DataSourceConfig;
import org.but.feec.javafx.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {

    public void deleteAuthorById(Long id) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM mydb.author WHERE author_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting author with ID: " + id, e);
        }
    }

    public List<AuthorBasicView> getAuthorsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT author_id, first_name, last_name, country, main_language, born, death " +
                             "FROM mydb.author ORDER BY author_id")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<AuthorBasicView> authorBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                authorBasicViews.add(mapToAuthorBasicView(resultSet));
            }
            return authorBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Authors basic view could not be loaded.", e);
        }
    }

    public void createAuthor(AuthorCreateView authorCreateView) {
        String insertAuthorSQL = "INSERT INTO mydb.author (first_name, last_name, country, main_language, born, death) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertAuthorSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, authorCreateView.getFirstName());
            preparedStatement.setString(2, authorCreateView.getLastName());
            preparedStatement.setString(3, authorCreateView.getCountry());
            preparedStatement.setString(4, authorCreateView.getMainLanguage());

            // Convert LocalDate to java.sql.Date
            LocalDate bornDate = authorCreateView.getBorn();
            if (bornDate != null) {
                preparedStatement.setDate(5, Date.valueOf(bornDate));  // Convert LocalDate to java.sql.Date
            } else {
                preparedStatement.setNull(5, Types.DATE);  // If bornDate is null, set it as SQL NULL
            }

            // Convert LocalDate to java.sql.Date for death as well
            LocalDate deathDate = authorCreateView.getDeath();
            if (deathDate != null) {
                preparedStatement.setDate(6, Date.valueOf(deathDate));  // Convert LocalDate to java.sql.Date
            } else {
                preparedStatement.setNull(6, Types.DATE);  // If deathDate is null, set it as SQL NULL
            }

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating author failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error: " + e.getMessage(), e);
        }
    }

    public void editAuthor(AuthorEditView authorEditView) {
        String updateAuthorSQL = "UPDATE mydb.author SET first_name = ?, last_name = ?, country = ?, main_language = ?, born = ?, death = ? WHERE author_id = ?";

        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateAuthorSQL)) {

            preparedStatement.setString(1, authorEditView.getFirstName());
            preparedStatement.setString(2, authorEditView.getLastName());
            preparedStatement.setString(3, authorEditView.getCountry());
            preparedStatement.setString(4, authorEditView.getMainLanguage());

            // Convert LocalDate to java.sql.Date for 'born'
            LocalDate bornDate = authorEditView.getBorn();
            if (bornDate != null) {
                preparedStatement.setDate(5, Date.valueOf(bornDate));  // Convert LocalDate to java.sql.Date
            } else {
                preparedStatement.setNull(5, Types.DATE);  // If bornDate is null, set it as SQL NULL
            }

            // Convert LocalDate to java.sql.Date for 'death'
            LocalDate deathDate = authorEditView.getDeath();
            if (deathDate != null) {
                preparedStatement.setDate(6, Date.valueOf(deathDate));  // Convert LocalDate to java.sql.Date
            } else {
                preparedStatement.setNull(6, Types.DATE);  // If deathDate is null, set it as SQL NULL
            }

            preparedStatement.setLong(7, authorEditView.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Editing author failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Editing author operation on the database failed.", e);
        }
    }

    private AuthorBasicView mapToAuthorBasicView(ResultSet rs) throws SQLException {
        AuthorBasicView authorBasicView = new AuthorBasicView();
        authorBasicView.setId(rs.getLong("author_id"));
        authorBasicView.setFirstName(rs.getString("first_name"));
        authorBasicView.setLastName(rs.getString("last_name"));
        authorBasicView.setCountry(rs.getString("country"));
        authorBasicView.setMainLanguage(rs.getString("main_language"));

        // Convert java.sql.Date to java.time.LocalDate
        Date bornDate = rs.getDate("born");
        if (bornDate != null) {
            authorBasicView.setBorn(bornDate.toLocalDate());  // Convert to LocalDate
        } else {
            authorBasicView.setBorn(null);  // Handle case where born date is null
        }

        Date deathDate = rs.getDate("death");
        if (deathDate != null) {
            authorBasicView.setDeath(deathDate.toLocalDate());  // Convert to LocalDate
        } else {
            authorBasicView.setDeath(null);  // Handle case where death date is null
        }

        return authorBasicView;
    }

}
