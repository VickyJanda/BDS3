package org.but.feec.javafx.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.javafx.api.FineEditView;
import org.but.feec.javafx.services.FineService;
import org.but.feec.javafx.data.FineRepository;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class FineEditController {

    private static final Logger logger = LoggerFactory.getLogger(FineEditController.class);

    @FXML
    public Button editFineButton;
    @FXML
    public TextField fineIdTextField;
    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField rentIdTextField;
    @FXML
    private TextField fineTotalTextField;
    @FXML
    private DatePicker fineDueDatePicker;

    private FineService fineService;
    private FineRepository fineRepository;
    private ValidationSupport validation;

    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage == null) {
            logger.error("Stage is still null after setStage() method.");
        } else {
            logger.info("Stage successfully set in FineEditController.");
        }
    }

    @FXML
    public void initialize() {
        fineRepository = new FineRepository();
        fineService = new FineService(fineRepository);

        validation = new ValidationSupport();
        fineIdTextField.setEditable(false);


        validation.registerValidator(userIdTextField, Validator.createEmptyValidator("The user ID must not be empty."));
        validation.registerValidator(rentIdTextField, Validator.createEmptyValidator("The rent ID must not be empty."));
        validation.registerValidator(fineTotalTextField, Validator.createEmptyValidator("The fine total must not be empty."));
        validation.registerValidator(fineDueDatePicker, Validator.createEmptyValidator("The fine due date must not be empty."));


        editFineButton.disableProperty().bind(validation.invalidProperty());


        Platform.runLater(this::loadFineData);

        logger.info("FineEditController initialized.");
    }

    private void loadFineData() {
        if (this.stage == null) {
            logger.error("Stage is null. Cannot load fine data.");
            return;
        }

        if (stage.getUserData() instanceof FineEditView) {
            FineEditView fineEditView = (FineEditView) stage.getUserData();


            logger.info("Loaded FineEditView: {}", fineEditView);


            fineIdTextField.setText(String.valueOf(fineEditView.getId()));
            userIdTextField.setText(String.valueOf(fineEditView.getUserId()));
            rentIdTextField.setText(String.valueOf(fineEditView.getRentId()));
            fineTotalTextField.setText(String.valueOf(fineEditView.getFineTotal()));


            if (fineEditView.getFineDueDate() != null) {
                try {
                    LocalDate fineDueDate = LocalDate.parse(fineEditView.getFineDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    fineDueDatePicker.setValue(fineDueDate);
                } catch (DateTimeParseException e) {
                    logger.error("Invalid date format for fine due date: " + fineEditView.getFineDueDate());
                }
            } else {
                logger.warn("Fine due date is null.");
            }
        } else {
            logger.error("Stage user data is not an instance of FineEditView.");
        }
    }

    @FXML
    public void handleEditFineButton(ActionEvent event) {
        try {

            Long fineId = Long.valueOf(fineIdTextField.getText());
            Long userId = Long.valueOf(userIdTextField.getText());
            Long rentId = Long.valueOf(rentIdTextField.getText());
            Double fineTotal = Double.valueOf(fineTotalTextField.getText());


            String fineDueDate = null;
            if (fineDueDatePicker.getValue() != null) {
                fineDueDate = fineDueDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }


            FineEditView fineEditView = new FineEditView();
            fineEditView.setId(fineId);
            fineEditView.setUserId(userId);
            fineEditView.setRentId(rentId);
            fineEditView.setFineTotal(fineTotal);
            fineEditView.setFineDueDate(fineDueDate);


            fineService.editFine(fineEditView);


            fineEditedConfirmationDialog();
        } catch (NumberFormatException e) {
            logger.error("Invalid number format while editing fine: ", e);
        }
    }

    private void fineEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Fine Edited Confirmation");
        alert.setHeaderText("The fine was successfully edited.");


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


        Stage stage = (Stage) editFineButton.getScene().getWindow();
        stage.close();
    }
}
