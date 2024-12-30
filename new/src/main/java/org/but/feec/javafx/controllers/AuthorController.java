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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.but.feec.javafx.App;
import org.but.feec.javafx.api.AuthorBasicView;
import org.but.feec.javafx.data.AuthorRepository;
import org.but.feec.javafx.exceptions.ExceptionHandler;
import org.but.feec.javafx.services.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @FXML
    public Button addAuthorButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button simulateButton;
    @FXML
    private TableColumn<AuthorBasicView, Long> authorId;
    @FXML
    private TableColumn<AuthorBasicView, String> firstName;
    @FXML
    private TableColumn<AuthorBasicView, String> lastName;
    @FXML
    private TableColumn<AuthorBasicView, String> country;
    @FXML
    private TableColumn<AuthorBasicView, String> mainLanguage;
    @FXML
    private TableColumn<AuthorBasicView, LocalDate> born;
    @FXML
    private TableColumn<AuthorBasicView, LocalDate> death;
    @FXML
    private TableView<AuthorBasicView> systemAuthorsTableView;
    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private TextField searchIdField, searchFirstNameField, searchLastNameField, searchCountryField, searchLanguageField, searchBornField, searchDeathField;

    private AuthorService authorService;
    private AuthorRepository authorRepository;

    public AuthorController() {
    }

    @FXML
    private void initialize() {
        authorRepository = new AuthorRepository();
        authorService = new AuthorService(authorRepository);

        // Initialize the table columns with author properties
        authorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        mainLanguage.setCellValueFactory(new PropertyValueFactory<>("mainLanguage"));
        born.setCellValueFactory(new PropertyValueFactory<>("born"));
        death.setCellValueFactory(new PropertyValueFactory<>("death"));

        ObservableList<AuthorBasicView> observableAuthorsList = initializeAuthorsData();
        filteredData = new FilteredList<>(observableAuthorsList, p -> true);
        systemAuthorsTableView.setItems(filteredData);

        systemAuthorsTableView.getSortOrder().add(authorId);

        initializeTableViewSelection();
        setupSearch();

        logger.info("AuthorController initialized");
    }

    private FilteredList<AuthorBasicView> filteredData;

    private void setupSearch() {
        searchIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                return author.getId().toString().contains(newValue);
            });
        });

        searchFirstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                return author.getFirstName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchLastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                return author.getLastName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchCountryField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                return author.getCountry().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchLanguageField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                return author.getMainLanguage().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchBornField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                try {
                    LocalDate date = LocalDate.parse(newValue);
                    return author.getBorn().equals(date);
                } catch (Exception e) {
                    return false;
                }
            });
        });

        searchDeathField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(author -> {
                if (newValue.isEmpty()) return true;
                try {
                    LocalDate date = LocalDate.parse(newValue);
                    return author.getDeath() != null && author.getDeath().equals(date);
                } catch (Exception e) {
                    return false;
                }
            });
        });

        SortedList<AuthorBasicView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(systemAuthorsTableView.comparatorProperty());
        systemAuthorsTableView.setItems(sortedData);
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit Author");
        MenuItem delete = new MenuItem("Delete Author");

        edit.setOnAction((ActionEvent event) -> {
            AuthorBasicView authorView = systemAuthorsTableView.getSelectionModel().getSelectedItem();
            if (authorView != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(App.class.getResource("fxml/AuthorEdit.fxml"));

                    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                    AuthorEditController controller = fxmlLoader.getController();

                    Stage stage = new Stage();
                    stage.setTitle("Edit Author");

                    stage.setUserData(authorView);

                    controller.setStage(stage);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ExceptionHandler.handleException(ex);
                }
            } else {
                showAlert("No Selection", "Please select an author to edit.");
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            AuthorBasicView selectedAuthor = systemAuthorsTableView.getSelectionModel().getSelectedItem();
            if (selectedAuthor != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this author?");
                alert.setContentText("ID: " + selectedAuthor.getId() + "\nName: " + selectedAuthor.getFirstName() + " " + selectedAuthor.getLastName());

                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    authorService.deleteAuthorById(selectedAuthor.getId());
                    refresh();
                    showAlert("Deletion Successful", "The author has been deleted successfully.");
                }
            } else {
                showAlert("No Selection", "Please select an author to delete.");
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(edit, delete);
        systemAuthorsTableView.setContextMenu(menu);
    }

    private ObservableList<AuthorBasicView> initializeAuthorsData() {
        List<AuthorBasicView> authors = authorService.getAuthorsBasicView();
        return FXCollections.observableArrayList(authors);
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

    public void handleAddAuthorButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/AuthorCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("Create Author");

            AuthorCreateController controller = fxmlLoader.getController();
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
        ObservableList<AuthorBasicView> observableAuthorsList = initializeAuthorsData();
        systemAuthorsTableView.setItems(observableAuthorsList);
        filteredData = new FilteredList<>(observableAuthorsList, p -> true);
        systemAuthorsTableView.refresh();
        systemAuthorsTableView.sort();
    }
    @FXML
    public void handleSimulateButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/SimulateAttack.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("BDS Database Simulate Attack");

            // Get the controller instance
            SimulateAttackController controller = fxmlLoader.getController();

            // Set the controller with any necessary data (optional)
            // controller.setPersonsController(this);  // Pass the current instance if needed

            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }
}
