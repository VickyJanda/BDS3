package org.but.feec.javafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.but.feec.javafx.App;
import org.but.feec.javafx.api.BookBasicView;
import org.but.feec.javafx.data.BookRepository;
import org.but.feec.javafx.exceptions.ExceptionHandler;
import org.but.feec.javafx.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @FXML
    public Button addBookButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button simulateButton;
    @FXML
    private TableColumn<BookBasicView, Long> bookId;
    @FXML
    private TableColumn<BookBasicView, String> bookName;
    @FXML
    private TableColumn<BookBasicView, String> bookIsbn;
    @FXML
    private TableColumn<BookBasicView, String> bookStatus;
    @FXML
    private TableColumn<BookBasicView, Integer> bookPrice;
    @FXML
    private TableColumn<BookBasicView, Integer> bookQuantity;
    @FXML
    private TableView<BookBasicView> systemBooksTableView;
    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private TextField searchIdField, searchNameField, searchIsbnField, searchStatusField, searchPriceField, searchQuantityField;

    private BookService bookService;
    private BookRepository bookRepository;

    public BookController() {
    }

    @FXML
    private void initialize() {
        bookRepository = new BookRepository();
        bookService = new BookService(bookRepository);


        bookId.setCellValueFactory(new PropertyValueFactory<BookBasicView, Long>("id"));
        bookName.setCellValueFactory(new PropertyValueFactory<BookBasicView, String>("name"));
        bookIsbn.setCellValueFactory(new PropertyValueFactory<BookBasicView, String>("isbn"));
        bookStatus.setCellValueFactory(new PropertyValueFactory<BookBasicView, String>("status"));
        bookPrice.setCellValueFactory(new PropertyValueFactory<BookBasicView, Integer>("price"));
        bookQuantity.setCellValueFactory(new PropertyValueFactory<BookBasicView, Integer>("quantity"));

        ObservableList<BookBasicView> observablePersonsList = initializeBooksData();
        filteredData = new FilteredList<>(observablePersonsList, p -> true);
        systemBooksTableView.setItems(filteredData);


        systemBooksTableView.getSortOrder().add(bookId);

        initializeTableViewSelection();
        loadIcons();


        setupSearch();

        logger.info("BookController initialized");
    }

    private FilteredList<BookBasicView> filteredData;

    private void setupSearch() {


        searchIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue.isEmpty()) return true;
                return book.getId().toString().contains(newValue);
            });
        });

        searchNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue.isEmpty()) return true;
                return book.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchIsbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue.isEmpty()) return true;
                return book.getIsbn().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchStatusField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue.isEmpty()) return true;
                return book.getStatus().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue.isEmpty()) return true;
                try {
                    int price = Integer.parseInt(newValue);
                    return book.getPrice() == price;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
        });

        searchQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue.isEmpty()) return true;
                try {
                    int quantity = Integer.parseInt(newValue);
                    return book.getQuantity() == quantity;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
        });


        SortedList<BookBasicView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(systemBooksTableView.comparatorProperty());
        systemBooksTableView.setItems(sortedData);
    }

    public void loadNewContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/but/feec/javafx/fxml/" + fxml));
            AnchorPane newContent = loader.load();
            contentAnchorPane.getChildren().setAll(newContent);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error loading FXML: " + fxml, e);
        }
    }

    @FXML
    private void handleSwitchUserButton() {
        loadNewContent("UserTable.fxml");
    }

    @FXML
    private void handleSwitchBookButton() {
        loadNewContent("BookTable.fxml");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit book");
        MenuItem delete = new MenuItem("Delete");

        edit.setOnAction((ActionEvent event) -> {
            BookBasicView bookView = systemBooksTableView.getSelectionModel().getSelectedItem();
            if (bookView != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(App.class.getResource("fxml/BookEdit.fxml"));


                    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                    BookEditController controller = fxmlLoader.getController();

                    Stage stage = new Stage();
                    stage.setTitle("Edit Book");

                    stage.setUserData(bookView);

                    controller.setStage(stage);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ExceptionHandler.handleException(ex);
                }
            } else {
                showAlert("No Selection", "Please select a book to edit.");
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            BookBasicView selectedBook = systemBooksTableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this book?");
                alert.setContentText("ID: " + selectedBook.getId() + "\nName: " + selectedBook.getName());

                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    bookService.deleteBookById(selectedBook.getId());
                    refresh();
                    showAlert("Deletion Successful", "The book has been deleted successfully.");
                }
            } else {
                showAlert("No Selection", "Please select a book to delete.");
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(edit, delete);
        systemBooksTableView.setContextMenu(menu);
    }

    private ObservableList<BookBasicView> initializeBooksData() {
        List<BookBasicView> books = bookService.getBooksBasicView();
        return FXCollections.observableArrayList(books);
    }

    private void loadIcons() {
        Image vutLogoImage = new Image(App.class.getResourceAsStream("logos/vut-logo-eng.png"));
        ImageView vutLogo = new ImageView(vutLogoImage);
        vutLogo.setFitWidth(150);
        vutLogo.setFitHeight(50);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);


        Stage stage = (Stage) contentAnchorPane.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    public void handleAddBookButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/BookCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("Create Book");

            BookCreateController controller = fxmlLoader.getController();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        refresh();
    }

    public void refresh() {
        ObservableList<BookBasicView> observableBooksList = initializeBooksData();
        systemBooksTableView.setItems(observableBooksList);
        systemBooksTableView.refresh();
        systemBooksTableView.sort();
        filteredData = new FilteredList<>(observableBooksList, p -> true);
        systemBooksTableView.setItems(filteredData);
    }

    @FXML
    public void handleSimulateButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/SimulateAttack.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("BDS JavaFX Simulate Attack");


            SimulateAttackController controller = fxmlLoader.getController();


            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }
}