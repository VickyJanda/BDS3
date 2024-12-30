package org.but.feec.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.javafx.api.PersonDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonsDetailViewController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsDetailViewController.class);

    @FXML
    private TextField idTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField givenNameTextField;

    @FXML
    private TextField familyNameTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField ageTextField;  // Added to display age

    @FXML
    private TextField phoneNumberTextField;  // Added to display phone number

    @FXML
    private TextField roleTextField;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTextField.setEditable(false);
        emailTextField.setEditable(false);
        givenNameTextField.setEditable(false);
        familyNameTextField.setEditable(false);
        ageTextField.setEditable(false);
        phoneNumberTextField.setEditable(false);
        roleTextField.setEditable(false);

        loadPersonsData();

        logger.info("PersonsDetailViewController initialized");
    }

    private void loadPersonsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof PersonDetailView) {
            PersonDetailView personDetailView = (PersonDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(personDetailView.getId()));
            emailTextField.setText(personDetailView.getEmail());
            givenNameTextField.setText(personDetailView.getGivenName());
            familyNameTextField.setText(personDetailView.getFamilyName());
            ageTextField.setText(personDetailView.getAge());
            phoneNumberTextField.setText(personDetailView.getPhoneNumber());
            roleTextField.setText(personDetailView.getRole());
        }
    }
}
