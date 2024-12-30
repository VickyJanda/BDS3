package org.but.feec.javafx.data;

import org.but.feec.javafx.api.FineBasicView;
import org.but.feec.javafx.api.FineCreateView;
import org.but.feec.javafx.api.FineEditView;
import org.but.feec.javafx.config.DataSourceConfig;
import org.but.feec.javafx.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FineRepository {


    public void deleteFineById(Long id) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM mydb.fine WHERE fine_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting fine with ID: " + id, e);
        }
    }


    public List<FineBasicView> getFinesBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT fine_id, user_id, rent_id, fine_due_date, fine_total " +
                             "FROM mydb.fine ORDER BY fine_id")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<FineBasicView> fineList = new ArrayList<>();
            while (resultSet.next()) {
                fineList.add(mapToFineBasicView(resultSet));
            }
            return fineList;
        } catch (SQLException e) {
            throw new DataAccessException("Fines could not be loaded.", e);
        }
    }


    public void createFine(FineCreateView fineCreateView) {
        String insertFineSQL = "INSERT INTO mydb.fine (user_id, rent_id, fine_due_date, fine_total) VALUES (?, ?, ?, ?)";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertFineSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, fineCreateView.getUserId());
            preparedStatement.setLong(2, fineCreateView.getRentId());


            Date fineDueDateSQL = Date.valueOf(fineCreateView.getFineDueDate());
            preparedStatement.setDate(3, fineDueDateSQL);

            preparedStatement.setDouble(4, fineCreateView.getFineTotal());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating fine failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error: " + e.getMessage(), e);
        }
    }


    public void editFine(FineEditView fineEditView) {
        String updateFineSQL = "UPDATE mydb.fine SET user_id = ?, rent_id = ?, fine_due_date = ?, fine_total = ? WHERE fine_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateFineSQL)) {
            preparedStatement.setLong(1, fineEditView.getUserId());
            preparedStatement.setLong(2, fineEditView.getRentId());


            Date fineDueDateSQL = Date.valueOf(fineEditView.getFineDueDate());
            preparedStatement.setDate(3, fineDueDateSQL);

            preparedStatement.setDouble(4, fineEditView.getFineTotal());
            preparedStatement.setLong(5, fineEditView.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Editing fine failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Editing fine operation on the database failed.", e);
        }
    }


    private FineBasicView mapToFineBasicView(ResultSet rs) throws SQLException {
        FineBasicView fineBasicView = new FineBasicView();
        fineBasicView.setId(rs.getLong("fine_id"));
        fineBasicView.setUserId(rs.getLong("user_id"));
        fineBasicView.setRentId(rs.getLong("rent_id"));


        Date fineDueDateSQL = rs.getDate("fine_due_date");
        LocalDateTime fineDueDateTime = fineDueDateSQL.toLocalDate().atStartOfDay();
        if (fineDueDateSQL != null) {
            fineBasicView.setFineDueDate(fineDueDateTime);
        }

        fineBasicView.setFineTotal(rs.getDouble("fine_total"));
        return fineBasicView;
    }

}
