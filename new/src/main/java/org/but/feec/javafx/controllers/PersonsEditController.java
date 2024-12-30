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
import org.but.feec.javafx.api.PersonBasicView;
import org.but.feec.javafx.api.PersonEditView;
import org.but.feec.javafx.data.PersonRepository;
import org.but.feec.javafx.services.PersonService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class PersonsEditController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsEditController.class);

    @FXML
    public Button editPersonButton;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField givenNameTextField;
    @FXML
    private TextField familyNameTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField phoneNumberTextField;

    private PersonService personService;
    private PersonRepository personRepository;
    private ValidationSupport validation;


    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage == null) {
            logger.error("Stage is still null after setStage()");
        } else {
            logger.info("Stage successfully set in PersonsEditController.");
        }
    }


    @FXML
    public void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);
        if (stage == null) {
            logger.error("Stage is not set in PersonsEditController.");
        } else {
            logger.info("Stage is set in PersonsEditController.");
        }

        validation = new ValidationSupport();
        validation.registerValidator(idTextField, Validator.createEmptyValidator("The id must not be empty."));
        idTextField.setEditable(false);
        validation.registerValidator(emailTextField, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(givenNameTextField, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(familyNameTextField, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(ageTextField, Validator.createEmptyValidator("The age must not be empty."));
        validation.registerValidator(phoneNumberTextField, Validator.createEmptyValidator("The phone number must not be empty."));

        editPersonButton.disableProperty().bind(validation.invalidProperty());

        Platform.runLater(() -> loadPersonsData());

        logger.info("PersonsEditController initialized");
    }

    /**
     * Load passed data from Persons controller. Check this tutorial explaining how to pass the data between controllers: https:
     */
    private void loadPersonsData() {
        if (this.stage == null) {
            logger.error("Stage is null. Cannot load person data.");
            return;
        }
        if (stage.getUserData() instanceof PersonBasicView) {
            PersonBasicView personBasicView = (PersonBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(personBasicView.getId()));
            emailTextField.setText(personBasicView.getEmail());
            givenNameTextField.setText(personBasicView.getGivenName());
            familyNameTextField.setText(personBasicView.getFamilyName());
            ageTextField.setText(personBasicView.getAge());
            phoneNumberTextField.setText(personBasicView.getPhoneNumber());
        }
    }

    @FXML
    public void handleEditPersonButton(ActionEvent event) {

        Long id = Long.valueOf(idTextField.getText());
        String email = emailTextField.getText();
        String firstName = givenNameTextField.getText();
        String lastName = familyNameTextField.getText();
        Integer age = Integer.valueOf(ageTextField.getText());
        String phoneNumber = phoneNumberTextField.getText();

        PersonEditView personEditView = new PersonEditView();
        personEditView.setId(id);
        personEditView.setEmail(email);
        personEditView.setGivenName(firstName);
        personEditView.setFamilyName(lastName);
        personEditView.setAge(age);
        personEditView.setPhoneNumber(phoneNumber);

        personService.editPerson(personEditView);

        personEditedConfirmationDialog();
    }

    private void personEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Person Edited Confirmation");
        alert.setHeaderText("Your person was successfully edited.");

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

        Stage stage = (Stage) editPersonButton.getScene().getWindow();
        stage.close();
    }

}
