package org.but.feec.javafx.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.javafx.api.AuthorCreateView;
import org.but.feec.javafx.data.AuthorRepository;
import org.but.feec.javafx.services.AuthorService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

public class AuthorCreateController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorCreateController.class);

    @FXML
    private Button newAuthorCreateButton;
    @FXML
    private TextField newAuthorFirstName;
    @FXML
    private TextField newAuthorLastName;
    @FXML
    private TextField newAuthorCountry;
    @FXML
    private TextField newAuthorMainLanguage;
    @FXML
    private DatePicker newAuthorBorn;

    @FXML
    private DatePicker newAuthorDeath;

    private AuthorService authorService;
    private ValidationSupport validation;

    private AuthorController authorController;


    public void setAuthorController(AuthorController authorController) {
        this.authorController = authorController;
    }

    @FXML
    public void initialize() {

        authorService = new AuthorService(new AuthorRepository());


        validation = new ValidationSupport();


        if (newAuthorFirstName != null) {
            validation.registerValidator(newAuthorFirstName, Validator.createEmptyValidator("The first name must not be empty."));
        }
        if (newAuthorLastName != null) {
            validation.registerValidator(newAuthorLastName, Validator.createEmptyValidator("The last name must not be empty."));
        }
        if (newAuthorCountry != null) {
            validation.registerValidator(newAuthorCountry, Validator.createEmptyValidator("The country must not be empty."));
        }
        if (newAuthorMainLanguage != null) {
            validation.registerValidator(newAuthorMainLanguage, Validator.createEmptyValidator("The main language must not be empty."));
        }


        if (newAuthorCreateButton != null) {
            newAuthorCreateButton.disableProperty().bind(validation.invalidProperty());
        }

        logger.info("AuthorCreateController initialized");
    }

    @FXML
    void handleCreateNewAuthor(ActionEvent event) {
        try {

            String firstName = newAuthorFirstName.getText();
            String lastName = newAuthorLastName.getText();
            String country = newAuthorCountry.getText();
            String mainLanguage = newAuthorMainLanguage.getText();


            LocalDate born = newAuthorBorn.getValue();
            LocalDate death = newAuthorDeath.getValue();


            AuthorCreateView authorCreateView = new AuthorCreateView();
            authorCreateView.setFirstName(firstName);
            authorCreateView.setLastName(lastName);
            authorCreateView.setCountry(country);
            authorCreateView.setMainLanguage(mainLanguage);
            authorCreateView.setBorn(born);
            authorCreateView.setDeath(death);


            authorService.createAuthor(authorCreateView);

            authorCreatedConfirmationDialog();
        } catch (Exception e) {
            logger.error("Error creating author: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error creating the author. Please try again.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void authorCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Author Created Confirmation");
        alert.setHeaderText("The author was successfully created.");


        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            alert.setResult(ButtonType.CANCEL);
            alert.hide();
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
        Optional<ButtonType> result = alert.showAndWait();


        Stage stage = (Stage) newAuthorCreateButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }


        if (authorController != null) {
            authorController.refresh();
        }
    }
}
