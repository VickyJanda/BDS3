package org.but.feec.javafx.data;

import org.but.feec.javafx.api.*;
import org.but.feec.javafx.config.DataSourceConfig;
import org.but.feec.javafx.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public void deleteBookById(Long id) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM mydb.book WHERE book_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book with ID: " + id, e);
        }
    }

    public List<BookBasicView> getBooksBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT book_id, name, isbn, status, price, quantity " +
                             "FROM mydb.book ORDER BY book_id")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BookBasicView> bookBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                bookBasicViews.add(mapToBookBasicView(resultSet));
            }
            return bookBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Books basic view could not be loaded.", e);
        }
    }

    public void createBook(BookCreateView bookCreateView) {
        String insertBookSQL = "INSERT INTO mydb.book (name, isbn, status, price, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertBookSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, bookCreateView.getName());
            preparedStatement.setString(2, bookCreateView.getIsbn());
            preparedStatement.setString(3, bookCreateView.getStatus());
            preparedStatement.setDouble(4, bookCreateView.getPrice());
            preparedStatement.setInt(5, bookCreateView.getQuantity());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating book failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database error: " + e.getMessage(), e);
        }
    }

    public void editBook(BookEditView bookEditView) {
        String updateBookSQL = "UPDATE mydb.book SET name = ?, isbn = ?, status = ?, price = ?, quantity = ? WHERE book_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateBookSQL)) {
            preparedStatement.setString(1, bookEditView.getName());
            preparedStatement.setString(2, bookEditView.getIsbn());
            preparedStatement.setString(3, bookEditView.getStatus());
            preparedStatement.setInt(4, bookEditView.getPrice());
            preparedStatement.setInt(5, bookEditView.getQuantity());
            preparedStatement.setLong(6, bookEditView.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Editing book failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Editing book operation on the database failed.", e);
        }
    }

    private BookBasicView mapToBookBasicView(ResultSet rs) throws SQLException {
        BookBasicView bookBasicView = new BookBasicView();
        bookBasicView.setId(rs.getLong("book_id"));
        bookBasicView.setName(rs.getString("name"));
        bookBasicView.setIsbn(rs.getString("isbn"));
        bookBasicView.setStatus(rs.getString("status"));
        bookBasicView.setPrice(rs.getInt("price"));
        bookBasicView.setQuantity(rs.getInt("quantity"));
        return bookBasicView;
    }
}
