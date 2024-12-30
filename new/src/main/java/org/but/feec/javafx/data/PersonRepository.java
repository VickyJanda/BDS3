package org.but.feec.javafx.data;

import org.but.feec.javafx.api.*;
import org.but.feec.javafx.config.DataSourceConfig;
import org.but.feec.javafx.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public PersonAuthView findPersonByEmail(String email) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, password " +
                             "FROM mydb.user p " +
                             "WHERE p.email = ?"
             )) {
            try {
                preparedStatement.setString(1, email);
                System.out.println("SQL prepared, executing query...");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Query executed.");
                    if (resultSet.next()) {
                        return mapToPersonAuth(resultSet);
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
                throw new DataAccessException("Find person by ID with addresses failed.", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }

    public PersonDetailView findPersonDetailedView(Long personId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT p.user_id, p.email, p.first_name, p.last_name, p.age, p.phone_number, r.role " +
                             "FROM mydb.user p " +
                             "JOIN mydb.user_has_role ur ON p.user_id = ur.user_id " +
                             "JOIN mydb.role r ON ur.role_id = r.role_id " +
                             "WHERE p.user_id = ?")) {
            preparedStatement.setLong(1, personId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }

    public void deletePersonById(Long id) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM mydb.user WHERE user_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting person with ID: " + id, e);
        }
    }


    public List<PersonBasicView> getPersonsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT user_id, first_name, last_name, email, age, phone_number " +
                             "FROM mydb.user ORDER BY user_id")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<PersonBasicView> personBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                personBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return personBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Persons basic view could not be loaded.", e);
        }
    }

    public void createPerson(PersonCreateView personCreateView) {
        String insertPersonSQL = "INSERT INTO mydb.user (email, first_name, password, last_name, age, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, personCreateView.getEmail());
            preparedStatement.setString(2, personCreateView.getGivenName());
            preparedStatement.setString(3, new String(personCreateView.getPwd()));
            preparedStatement.setString(4, personCreateView.getFamilyName());
            preparedStatement.setInt(5, personCreateView.getAge());
            preparedStatement.setString(6, personCreateView.getPhoneNumber());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating person failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error: " + e.getMessage(), e);
        }
    }

    public void editPerson(PersonEditView personEditView) {
        String updatePersonSQL = "UPDATE mydb.user SET email = ?, first_name = ?, last_name = ?, age = ?, phone_number = ? WHERE user_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updatePersonSQL)) {
            preparedStatement.setString(1, personEditView.getEmail());
            preparedStatement.setString(2, personEditView.getGivenName());
            preparedStatement.setString(3, personEditView.getFamilyName());
            preparedStatement.setInt(4, personEditView.getAge());
            preparedStatement.setString(5, personEditView.getPhoneNumber());
            preparedStatement.setLong(6, personEditView.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Editing person failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Editing person failed operation on the database failed.");
        }
    }

    private PersonAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        PersonAuthView person = new PersonAuthView();
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("password"));
        return person;
    }

    private PersonBasicView mapToPersonBasicView(ResultSet rs) throws SQLException {
        PersonBasicView personBasicView = new PersonBasicView();
        personBasicView.setId(rs.getLong("user_id"));
        personBasicView.setEmail(rs.getString("email"));
        personBasicView.setGivenName(rs.getString("first_name"));
        personBasicView.setFamilyName(rs.getString("last_name"));
        personBasicView.setAge(rs.getString("age"));
        personBasicView.setPhoneNumber(rs.getString("phone_number"));
        return personBasicView;
    }

    private PersonDetailView mapToPersonDetailView(ResultSet rs) throws SQLException {
        PersonDetailView personDetailView = new PersonDetailView();
        personDetailView.setId(rs.getLong("user_id"));
        personDetailView.setEmail(rs.getString("email"));
        personDetailView.setGivenName(rs.getString("first_name"));
        personDetailView.setFamilyName(rs.getString("last_name"));
        personDetailView.setAge(rs.getString("age"));
        personDetailView.setPhoneNumber(rs.getString("phone_number"));
        personDetailView.setRole(rs.getString("role"));
        return personDetailView;
    }
}
