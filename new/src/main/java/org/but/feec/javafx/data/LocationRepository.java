package org.but.feec.javafx.data;

import org.but.feec.javafx.api.*;
import org.but.feec.javafx.config.DataSourceConfig;
import org.but.feec.javafx.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationRepository {

    // Method to delete a location by its ID
    public void deleteLocationById(Long id) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM mydb.location WHERE location_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting location with ID: " + id, e);
        }
    }

    // Method to get a list of locations in basic view format
    public List<LocationBasicView> getLocationsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT location_id, city, street, house_number, zip_code, library_id " +
                             "FROM mydb.location ORDER BY location_id")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<LocationBasicView> locationBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                locationBasicViews.add(mapToLocationBasicView(resultSet));
            }
            return locationBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Locations basic view could not be loaded.", e);
        }
    }

    // Method to create a new location
    public void createLocation(LocationCreateView locationCreateView) {
        String insertLocationSQL = "INSERT INTO mydb.location (city, street, house_number, zip_code, library_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertLocationSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, locationCreateView.getCity());
            preparedStatement.setString(2, locationCreateView.getStreet());
            preparedStatement.setString(3, locationCreateView.getHouseNumber());
            preparedStatement.setString(4, locationCreateView.getZipCode());
            preparedStatement.setLong(5, locationCreateView.getLibraryId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating location failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error: " + e.getMessage(), e);
        }
    }

    // Method to edit an existing location
    public void editLocation(LocationEditView locationEditView) {
        String updateLocationSQL = "UPDATE mydb.location SET city = ?, street = ?, house_number = ?, zip_code = ?, library_id = ? WHERE location_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateLocationSQL)) {
            preparedStatement.setString(1, locationEditView.getCity());
            preparedStatement.setString(2, locationEditView.getStreet());
            preparedStatement.setString(3, locationEditView.getHouseNumber());
            preparedStatement.setString(4, locationEditView.getZipCode());
            preparedStatement.setLong(5, locationEditView.getLibraryId());
            preparedStatement.setLong(6, locationEditView.getLocationId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Editing location failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Editing location operation on the database failed.", e);
        }
    }

    // Helper method to map a ResultSet to LocationBasicView
    private LocationBasicView mapToLocationBasicView(ResultSet rs) throws SQLException {
        LocationBasicView locationBasicView = new LocationBasicView();
        locationBasicView.setLocationId(rs.getLong("location_id"));
        locationBasicView.setCity(rs.getString("city"));
        locationBasicView.setStreet(rs.getString("street"));
        locationBasicView.setHouseNumber(rs.getString("house_number"));
        locationBasicView.setZipCode(rs.getString("zip_code"));
        locationBasicView.setLibraryId(rs.getLong("library_id"));
        return locationBasicView;
    }
}
