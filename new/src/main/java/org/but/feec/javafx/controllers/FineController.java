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
import org.but.feec.javafx.api.FineBasicView;
import org.but.feec.javafx.data.FineRepository;
import org.but.feec.javafx.exceptions.ExceptionHandler;
import org.but.feec.javafx.services.FineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class FineController {

    private static final Logger logger = LoggerFactory.getLogger(FineController.class);

    @FXML
    public Button addFineButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button simulateButton;
    @FXML
    private TableColumn<FineBasicView, Long> fineId;
    @FXML
    private TableColumn<FineBasicView, String> userId;
    @FXML
    private TableColumn<FineBasicView, String> rentId;
    @FXML
    private TableColumn<FineBasicView, String> fineDueDate;
    @FXML
    private TableColumn<FineBasicView, Integer> fineTotal;  // Changed to Integer
    @FXML
    private TableView<FineBasicView> fineTableView;
    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private TextField searchFineIdField, searchUserIdField, searchRentIdField, searchFineDueDateField, searchFineTotalField;

    private FineService FineService;
    private FineRepository FineRepository;

    public FineController() {
    }

    @FXML
    private void initialize() {
        FineRepository = new FineRepository();
        FineService = new FineService(FineRepository);

        // Initialize the table columns with Fine properties
        fineId.setCellValueFactory(new PropertyValueFactory<FineBasicView, Long>("fineId"));
        userId.setCellValueFactory(new PropertyValueFactory<FineBasicView, String>("userId"));
        rentId.setCellValueFactory(new PropertyValueFactory<FineBasicView, String>("rentId"));
        fineDueDate.setCellValueFactory(new PropertyValueFactory<FineBasicView, String>("fineDueDate"));
        fineTotal.setCellValueFactory(new PropertyValueFactory<FineBasicView, Integer>("fineTotal"));

        ObservableList<FineBasicView> observablePersonsList = initializeFinesData();
        filteredData = new FilteredList<>(observablePersonsList, p -> true);
        fineTableView.setItems(filteredData);


        fineTableView.getSortOrder().add(fineId);

        initializeTableViewSelection();
        loadIcons();

        // Initialize search functionality
        setupSearch();

        logger.info("FineController initialized");
    }

    private FilteredList<FineBasicView> filteredData;

    private void setupSearch() {
        // Create a FilteredList to hold filtered data

        // Add listeners to search fields to filter data
        searchFineIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Fine -> {
                if (newValue.isEmpty()) return true; // If the field is empty, show all results
                return Fine.getId().toString().contains(newValue); // Filter by ID
            });
        });

        searchUserIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Fine -> {
                if (newValue.isEmpty()) return true;
                return Fine.getUserId().toString().contains(newValue); // Filter by name
            });
        });

        searchRentIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Fine -> {
                if (newValue.isEmpty()) return true;
                return Fine.getRentId().toString().contains(newValue); // Filter by ISBN
            });
        });

        searchFineDueDateField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Fine -> {
                if (newValue.isEmpty()) return true;
                return Fine.getFineDueDate().toString().contains(newValue); // Filter by status
            });
        });

        searchFineTotalField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Fine -> {
                if (newValue.isEmpty()) return true;
                try {
                    int price = Integer.parseInt(newValue); // Parse the price as an integer
                    return Fine.getFineTotal() == price; // Filter by exact price match
                } catch (NumberFormatException e) {
                    return false; // If the value is not a valid number, return false
                }
            });
        });

        // Bind the filtered data to the table's items property
        SortedList<FineBasicView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(fineTableView.comparatorProperty());
        fineTableView.setItems(sortedData);
    }

    public void loadNewContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/but/feec/javafx/fxml/" + fxml));
            AnchorPane newContent = loader.load();
            contentAnchorPane.getChildren().setAll(newContent); // Replace the current content
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error loading FXML: " + fxml, e);
        }
    }

    @FXML
    private void handleSwitchUserButton() {
        loadNewContent("UserTable.fxml"); // Load user table content
    }

    @FXML
    private void handleSwitchFineButton() {
        loadNewContent("FineTable.fxml"); // Load library table content
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit Fine");
        MenuItem delete = new MenuItem("Delete");

        edit.setOnAction((ActionEvent event) -> {
            FineBasicView FineView = fineTableView.getSelectionModel().getSelectedItem();
            if (FineView != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(App.class.getResource("fxml/FineEdit.fxml"));

                    // Load the FXML and get the controller
                    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                    FineEditController controller = fxmlLoader.getController();

                    Stage stage = new Stage();
                    stage.setTitle("Edit Fine");

                    stage.setUserData(FineView);

                    controller.setStage(stage);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ExceptionHandler.handleException(ex);
                }
            } else {
                showAlert("No Selection", "Please select a Fine to edit.");
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            FineBasicView selectedFine = fineTableView.getSelectionModel().getSelectedItem();
            if (selectedFine != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this Fine?");
                alert.setContentText("ID: " + selectedFine.getId());

                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    FineService.deleteFineById(selectedFine.getId());
                    refresh();
                    showAlert("Deletion Successful", "The Fine has been deleted successfully.");
                }
            } else {
                showAlert("No Selection", "Please select a Fine to delete.");
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(edit, delete);
        fineTableView.setContextMenu(menu);
    }

    private ObservableList<FineBasicView> initializeFinesData() {
        List<FineBasicView> Fines = FineService.getFineBasicView();
        return FXCollections.observableArrayList(Fines);
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

        // Set the owner to center the alert
        Stage stage = (Stage) contentAnchorPane.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    public void handleAddFineButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/FineCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("Create Fine");

            FineCreateController controller = fxmlLoader.getController();
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
        ObservableList<FineBasicView> observableFinesList = initializeFinesData();
        fineTableView.setItems(observableFinesList);
        fineTableView.refresh();
        fineTableView.sort();
        filteredData = new FilteredList<>(observableFinesList, p -> true);
        fineTableView.setItems(filteredData);
    }
    @FXML
    public void handleSimulateButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/SimulateAttack.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("BDS JavaFX Simulate Attack");

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