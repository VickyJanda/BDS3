package org.but.feec.javafx.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.javafx.api.FineCreateView;
import org.but.feec.javafx.services.FineService;
import org.but.feec.javafx.data.FineRepository;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

public class FineCreateController {

    private static final Logger logger = LoggerFactory.getLogger(FineCreateController.class);

    @FXML
    private Button createFineButton;
    @FXML
    private TextField newUserId;
    @FXML
    private TextField newRentId;
    @FXML
    private TextField newFineTotal;
    @FXML
    private DatePicker newFineDueDate;

    private FineService fineService;
    private ValidationSupport validation;

    private FineController fineController;


    public void setFineController(FineController fineController) {
        this.fineController = fineController;
    }

    @FXML
    public void initialize() {

        fineService = new FineService(new FineRepository());


        validation = new ValidationSupport();


        if (newUserId != null) {
            validation.registerValidator(newUserId, Validator.createEmptyValidator("The user ID must not be empty."));
        }
        if (newRentId != null) {
            validation.registerValidator(newRentId, Validator.createEmptyValidator("The rent ID must not be empty."));
        }
        if (newFineTotal != null) {
            validation.registerValidator(newFineTotal, Validator.createEmptyValidator("The fine total must not be empty."));
        }
        if (newFineDueDate != null) {
            validation.registerValidator(newFineDueDate, Validator.createEmptyValidator("The fine due date must not be empty."));
        }


        if (createFineButton != null) {
            createFineButton.disableProperty().bind(validation.invalidProperty());
        }

        logger.info("FineCreateController initialized");
    }

    @FXML
    void handleCreateFine(ActionEvent event) {
        try {

            Long userId = Long.valueOf(newUserId.getText());
            Long rentId = Long.valueOf(newRentId.getText());
            Double fineTotal = Double.valueOf(newFineTotal.getText());


            LocalDate fineDueDate = null;
            if (newFineDueDate.getValue() != null) {
                fineDueDate = newFineDueDate.getValue();
            }


            FineCreateView fineCreateView = new FineCreateView();
            fineCreateView.setUserId(userId);
            fineCreateView.setRentId(rentId);
            fineCreateView.setFineTotal(fineTotal);


            fineCreateView.setFineDueDate(fineDueDate);


            fineService.createFine(fineCreateView);

            fineCreatedConfirmationDialog();
        } catch (NumberFormatException e) {
            logger.error("Error parsing fine ID, user ID, rent ID, fine total: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for fine ID, user ID, rent ID, and fine total.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void fineCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Fine Created Confirmation");
        alert.setHeaderText("Your fine was successfully created.");


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


        Stage stage = (Stage) createFineButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }


        if (fineController != null) {
            fineController.refresh();
        }
    }
}
