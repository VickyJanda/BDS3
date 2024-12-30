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
import org.but.feec.javafx.api.PersonCreateView;
import org.but.feec.javafx.data.PersonRepository;
import org.but.feec.javafx.services.PersonService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PersonCreateController {

    private static final Logger logger = LoggerFactory.getLogger(PersonCreateController.class);

    @FXML
    public Button newPersonCreatePerson;
    @FXML
    private TextField newPersonEmail;

    @FXML
    private TextField newPersonGivenName;

    @FXML
    private TextField newPersonFamilyName;

    @FXML
    private TextField newPersonPwd;

    @FXML
    private TextField newPersonAge;

    @FXML
    private TextField newPersonPhoneNumber;

    private PersonService personService;
    private ValidationSupport validation;

    private PersonsController personsController;


    public void setPersonsController(PersonsController personsController) {
        this.personsController = personsController;
    }

    @FXML
    public void initialize() {

        personService = new PersonService(new PersonRepository());

        validation = new ValidationSupport();
        validation.registerValidator(newPersonEmail, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(newPersonGivenName, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(newPersonFamilyName, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(newPersonPwd, Validator.createEmptyValidator("The password must not be empty."));
        validation.registerValidator(newPersonAge, Validator.createEmptyValidator("The age must not be empty."));
        validation.registerValidator(newPersonPhoneNumber, Validator.createEmptyValidator("The phone number must not be empty."));

        newPersonCreatePerson.disableProperty().bind(validation.invalidProperty());

        logger.info("PersonCreateController initialized");
    }

    @FXML
    void handleCreateNewPerson(ActionEvent event) {
        String email = newPersonEmail.getText();
        String firstName = newPersonGivenName.getText();
        String lastName = newPersonFamilyName.getText();
        String password = newPersonPwd.getText();
        Integer age = Integer.valueOf(newPersonAge.getText());
        String phoneNumber = newPersonPhoneNumber.getText();

        PersonCreateView personCreateView = new PersonCreateView();
        personCreateView.setPwd(password.toCharArray());
        personCreateView.setEmail(email);
        personCreateView.setGivenName(firstName);
        personCreateView.setFamilyName(lastName);
        personCreateView.setAge(age);
        personCreateView.setPhoneNumber(phoneNumber);

        personService.createPerson(personCreateView);

        personCreatedConfirmationDialog();
    }

    private void personCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Person Created Confirmation");
        alert.setHeaderText("Your person was successfully created.");

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

        Stage stage = (Stage) newPersonCreatePerson.getScene().getWindow();
        stage.close();


        if (personsController != null) {
            personsController.refresh();
        }
    }
}
