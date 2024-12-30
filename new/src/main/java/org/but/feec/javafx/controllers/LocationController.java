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
import org.but.feec.javafx.api.LocationBasicView;
import org.but.feec.javafx.data.LocationRepository;
import org.but.feec.javafx.exceptions.ExceptionHandler;
import org.but.feec.javafx.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @FXML
    public Button addLocationButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button simulateButton;
    @FXML
    private TableColumn<LocationBasicView, Long> locationId;
    @FXML
    private TableColumn<LocationBasicView, String> locationCity;
    @FXML
    private TableColumn<LocationBasicView, String> locationStreet;
    @FXML
    private TableColumn<LocationBasicView, String> locationHouseNumber;
    @FXML
    private TableColumn<LocationBasicView, String> locationZipCode;
    @FXML
    private TableColumn<LocationBasicView, Long> libraryId;
    @FXML
    private TableView<LocationBasicView> systemLocationsTableView;
    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private TextField searchIdField, searchCityField, searchStreetField, searchHouseNumberField, searchZipCodeField, searchLibraryIdField;

    private LocationService locationService;
    private LocationRepository locationRepository;

    public LocationController() {
    }

    @FXML
    private void initialize() {
        locationRepository = new LocationRepository();
        locationService = new LocationService(locationRepository);


        locationId.setCellValueFactory(new PropertyValueFactory<LocationBasicView, Long>("locationId"));
        locationCity.setCellValueFactory(new PropertyValueFactory<LocationBasicView, String>("city"));
        locationStreet.setCellValueFactory(new PropertyValueFactory<LocationBasicView, String>("street"));
        locationHouseNumber.setCellValueFactory(new PropertyValueFactory<LocationBasicView, String>("houseNumber"));
        locationZipCode.setCellValueFactory(new PropertyValueFactory<LocationBasicView, String>("zipCode"));
        libraryId.setCellValueFactory(new PropertyValueFactory<LocationBasicView, Long>("libraryId"));

        ObservableList<LocationBasicView> observableLocationList = initializeLocationData();
        filteredData = new FilteredList<>(observableLocationList, p -> true);
        systemLocationsTableView.setItems(filteredData);

        systemLocationsTableView.getSortOrder().add(locationId);

        initializeTableViewSelection();
        loadIcons();


        setupSearch();

        logger.info("LocationController initialized");
    }

    private FilteredList<LocationBasicView> filteredData;

    private void setupSearch() {


        searchIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(location -> {
                if (newValue.isEmpty()) return true;
                return location.getLocationId().toString().contains(newValue);
            });
        });

        searchCityField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(location -> {
                if (newValue.isEmpty()) return true;
                return location.getCity().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchStreetField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(location -> {
                if (newValue.isEmpty()) return true;
                return location.getStreet().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchHouseNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(location -> {
                if (newValue.isEmpty()) return true;
                return location.getHouseNumber().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchZipCodeField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(location -> {
                if (newValue.isEmpty()) return true;
                return location.getZipCode().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchLibraryIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(location -> {
                if (newValue.isEmpty()) return true;
                return location.getLibraryId().toString().contains(newValue);
            });
        });


        SortedList<LocationBasicView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(systemLocationsTableView.comparatorProperty());
        systemLocationsTableView.setItems(sortedData);
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
    private void handleSwitchLocationButton() {
        loadNewContent("LocationTable.fxml");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit location");
        MenuItem delete = new MenuItem("Delete");

        edit.setOnAction((ActionEvent event) -> {
            LocationBasicView locationView = systemLocationsTableView.getSelectionModel().getSelectedItem();
            if (locationView != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(App.class.getResource("fxml/LocationEdit.fxml"));


                    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                    LocationEditController controller = fxmlLoader.getController();

                    Stage stage = new Stage();
                    stage.setTitle("Edit Location");

                    stage.setUserData(locationView);

                    controller.setStage(stage);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ExceptionHandler.handleException(ex);
                }
            } else {
                showAlert("No Selection", "Please select a location to edit.");
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            LocationBasicView selectedLocation = systemLocationsTableView.getSelectionModel().getSelectedItem();
            if (selectedLocation != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this location?");
                alert.setContentText("Location ID: " + selectedLocation.getLocationId() + "\nCity: " + selectedLocation.getCity());

                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    locationService.deleteLocationById(selectedLocation.getLocationId());
                    refresh();
                    showAlert("Deletion Successful", "The location has been deleted successfully.");
                }
            } else {
                showAlert("No Selection", "Please select a location to delete.");
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(edit, delete);
        systemLocationsTableView.setContextMenu(menu);
    }

    private ObservableList<LocationBasicView> initializeLocationData() {
        List<LocationBasicView> locations = locationService.getLocationsBasicView();
        return FXCollections.observableArrayList(locations);
    }

    private void loadIcons() {

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

    public void handleAddLocationButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/LocationCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("Create Location");

            LocationCreateController controller = fxmlLoader.getController();
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
        ObservableList<LocationBasicView> observableLocationsList = initializeLocationData();
        systemLocationsTableView.setItems(observableLocationsList);
        systemLocationsTableView.refresh();
        systemLocationsTableView.sort();
        filteredData = new FilteredList<>(observableLocationsList, p -> true);
        systemLocationsTableView.setItems(filteredData);
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
