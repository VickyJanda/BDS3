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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.javafx.api.AuthorBasicView;
import org.but.feec.javafx.api.AuthorEditView;
import org.but.feec.javafx.data.AuthorRepository;
import org.but.feec.javafx.services.AuthorService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

public class AuthorEditController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorEditController.class);

    @FXML
    public Button editAuthorButton;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField mainLanguageTextField;
    @FXML
    private DatePicker authorBornDatePicker;  // Change to DatePicker
    @FXML
    private DatePicker authorDeathDatePicker;  // Change to DatePicker

    private AuthorService authorService;
    private AuthorRepository authorRepository;
    private ValidationSupport validation;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage == null) {
            logger.error("Stage is still null after setStage()");
        } else {
            logger.info("Stage successfully set in AuthorEditController.");
        }
    }

    @FXML
    public void initialize() {
        authorRepository = new AuthorRepository();
        authorService = new AuthorService(authorRepository);
        if (stage == null) {
            logger.error("Stage is not set in AuthorEditController.");
        } else {
            logger.info("Stage is set in AuthorEditController.");
        }

        validation = new ValidationSupport();
        idTextField.setEditable(false);
        validation.registerValidator(firstNameTextField, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(lastNameTextField, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(countryTextField, Validator.createEmptyValidator("The country must not be empty."));
        validation.registerValidator(mainLanguageTextField, Validator.createEmptyValidator("The main language must not be empty."));

        editAuthorButton.disableProperty().bind(validation.invalidProperty());

        Platform.runLater(this::loadAuthorData);

        logger.info("AuthorEditController initialized");
    }

    private void loadAuthorData() {
        if (this.stage == null) {
            logger.error("Stage is null. Cannot load author data.");
            return;
        }
        if (stage.getUserData() instanceof AuthorBasicView) {
            AuthorBasicView authorBasicView = (AuthorBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(authorBasicView.getId()));
            firstNameTextField.setText(authorBasicView.getFirstName());
            lastNameTextField.setText(authorBasicView.getLastName());
            countryTextField.setText(authorBasicView.getCountry());
            mainLanguageTextField.setText(authorBasicView.getMainLanguage());
            // Set DatePicker values
            authorBornDatePicker.setValue(authorBasicView.getBorn() != null ? authorBasicView.getBorn() : null);
            authorDeathDatePicker.setValue(authorBasicView.getDeath() != null ? authorBasicView.getDeath() : null);
        } else {
            logger.error("Stage user data is not an instance of AuthorBasicView.");
        }
    }

    @FXML
    public void handleEditAuthorButton(ActionEvent event) {
        Long id = Long.valueOf(idTextField.getText());
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String country = countryTextField.getText();
        String mainLanguage = mainLanguageTextField.getText();

        // Get dates from DatePicker
        LocalDate born = authorBornDatePicker.getValue();
        LocalDate death = authorDeathDatePicker.getValue();

        AuthorEditView authorEditView = new AuthorEditView();
        authorEditView.setId(id);
        authorEditView.setFirstName(firstName);
        authorEditView.setLastName(lastName);
        authorEditView.setCountry(country);
        authorEditView.setMainLanguage(mainLanguage);
        authorEditView.setBorn(born);  // Directly assign LocalDate
        authorEditView.setDeath(death);  // Directly assign LocalDate

        authorService.editAuthor(authorEditView);

        authorEditedConfirmationDialog();
    }


    private void authorEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Author Edited Confirmation");
        alert.setHeaderText("Your author was successfully edited.");

        // Set the owner to the current stage
        alert.initOwner(stage);

        // Center the dialog on the screen
        alert.initModality(Modality.APPLICATION_MODAL);

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
        stage.close();
    }

}
