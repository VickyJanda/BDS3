package org.but.feec.javafx.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.javafx.api.LocationBasicView;
import org.but.feec.javafx.api.LocationEditView;
import org.but.feec.javafx.data.LocationRepository;
import org.but.feec.javafx.services.LocationService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LocationEditController {

    private static final Logger logger = LoggerFactory.getLogger(LocationEditController.class);

    @FXML
    public Button editLocationButton;
    @FXML
    public TextField locationIdTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private TextField libraryIdTextField;

    private LocationService locationService;
    private LocationRepository locationRepository;
    private ValidationSupport validation;


    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage == null) {
            logger.error("Stage is still null after setStage()");
        } else {
            logger.info("Stage successfully set in LocationEditController.");
        }
    }

    @FXML
    public void initialize() {
        locationRepository = new LocationRepository();
        locationService = new LocationService(locationRepository);
        if (stage == null) {
            logger.error("Stage is not set in LocationEditController.");
        } else {
            logger.info("Stage is set in LocationEditController.");
        }

        validation = new ValidationSupport();
        locationIdTextField.setEditable(false);
        validation.registerValidator(cityTextField, Validator.createEmptyValidator("The city must not be empty."));
        validation.registerValidator(streetTextField, Validator.createEmptyValidator("The street must not be empty."));
        validation.registerValidator(houseNumberTextField, Validator.createEmptyValidator("The house number must not be empty."));
        validation.registerValidator(zipCodeTextField, Validator.createEmptyValidator("The zip code must not be empty."));
        validation.registerValidator(libraryIdTextField, Validator.createEmptyValidator("The library ID must not be empty."));

        editLocationButton.disableProperty().bind(validation.invalidProperty());

        Platform.runLater(this::loadLocationData);

        logger.info("LocationEditController initialized");
    }

    private void loadLocationData() {
        if (this.stage == null) {
            logger.error("Stage is null. Cannot load location data.");
            return;
        }
        if (stage.getUserData() instanceof LocationBasicView) {
            LocationBasicView locationBasicView = (LocationBasicView) stage.getUserData();
            locationIdTextField.setText(String.valueOf(locationBasicView.getLocationId()));
            cityTextField.setText(locationBasicView.getCity());
            streetTextField.setText(locationBasicView.getStreet());
            houseNumberTextField.setText(locationBasicView.getHouseNumber());
            zipCodeTextField.setText(locationBasicView.getZipCode());
            libraryIdTextField.setText(String.valueOf(locationBasicView.getLibraryId()));
        } else {
            logger.error("Stage user data is not an instance of LocationBasicView.");
        }
    }

    @FXML
    public void handleEditLocationButton(ActionEvent event) {

        Long locationId = Long.valueOf(locationIdTextField.getText());
        String city = cityTextField.getText();
        String street = streetTextField.getText();
        String houseNumber = houseNumberTextField.getText();
        String zipCode = zipCodeTextField.getText();
        Long libraryId = Long.valueOf(libraryIdTextField.getText());


        LocationEditView locationEditView = new LocationEditView();
        locationEditView.setLocationId(locationId);
        locationEditView.setCity(city);
        locationEditView.setStreet(street);
        locationEditView.setHouseNumber(houseNumber);
        locationEditView.setZipCode(zipCode);
        locationEditView.setLibraryId(libraryId);


        locationService.editLocation(locationEditView);


        locationEditedConfirmationDialog();
    }

    private void locationEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Location Edited Confirmation");
        alert.setHeaderText("Your location was successfully edited.");


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


        Stage stage = (Stage) editLocationButton.getScene().getWindow();
        stage.close();
    }
}
