package org.but.feec.javafx.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.javafx.api.LocationCreateView;
import org.but.feec.javafx.data.LocationRepository;
import org.but.feec.javafx.services.LocationService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LocationCreateController {

    private static final Logger logger = LoggerFactory.getLogger(LocationCreateController.class);

    @FXML
    private Button newLocationCreateButton;
    @FXML
    private TextField newLibraryId;
    @FXML
    private TextField newLocationCity;
    @FXML
    private TextField newLocationStreet;
    @FXML
    private TextField newLocationHouseNumber;
    @FXML
    private TextField newLocationZipCode;

    private LocationService locationService;
    private ValidationSupport validation;

    private LocationController locationController;  // Store the reference to LocationController

    // Setter for LocationController
    public void setLocationController(LocationController locationController) {
        this.locationController = locationController;
    }

    @FXML
    public void initialize() {
        // Initialize LocationService with LocationRepository
        locationService = new LocationService(new LocationRepository());

        // Initialize ValidationSupport
        validation = new ValidationSupport();

        // Check if the controls are not null before applying validation
        if (newLocationCity != null) {
            validation.registerValidator(newLocationCity, Validator.createEmptyValidator("The city must not be empty."));
        }
        if (newLocationStreet != null) {
            validation.registerValidator(newLocationStreet, Validator.createEmptyValidator("The street must not be empty."));
        }
        if (newLocationHouseNumber != null) {
            validation.registerValidator(newLocationHouseNumber, Validator.createEmptyValidator("The house number must not be empty."));
        }
        if (newLocationZipCode != null) {
            validation.registerValidator(newLocationZipCode, Validator.createEmptyValidator("The zip code must not be empty."));
        }
        if (newLibraryId != null) {
            validation.registerValidator(newLibraryId, Validator.createEmptyValidator("The library ID must not be empty."));
        }

        // Disable the "Create Location" button if validation fails
        if (newLocationCreateButton != null) {
            newLocationCreateButton.disableProperty().bind(validation.invalidProperty());
        }

        logger.info("LocationCreateController initialized");
    }

    @FXML
    void handleCreateNewLocation(ActionEvent event) {
        try {
            // Get the values from the text fields
            String city = newLocationCity.getText();
            String street = newLocationStreet.getText();
            String houseNumber = newLocationHouseNumber.getText();
            String zipCode = newLocationZipCode.getText();
            Long libraryId = Long.valueOf(newLibraryId.getText());

            // Create the location view object
            LocationCreateView locationCreateView = new LocationCreateView();
            locationCreateView.setCity(city);
            locationCreateView.setStreet(street);
            locationCreateView.setHouseNumber(houseNumber);
            locationCreateView.setZipCode(zipCode);
            locationCreateView.setLibraryId(libraryId);

            // Call the service to create the location
            locationService.createLocation(locationCreateView);

            locationCreatedConfirmationDialog();
        } catch (NumberFormatException e) {
            logger.error("Error parsing libraryId: " + e.getMessage());
            // Show error alert for invalid inputs
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for library ID.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void locationCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Location Created Confirmation");
        alert.setHeaderText("Your location was successfully created.");

        // Close the dialog after 3 seconds
        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.setResult(ButtonType.CANCEL);
                alert.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
        Optional<ButtonType> result = alert.showAndWait();

        // Close the window after the alert
        Stage stage = (Stage) newLocationCreateButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }

        // Call refresh() in LocationController after closing the window
        if (locationController != null) {
            locationController.refresh();
        }
    }
}