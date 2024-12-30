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
import org.but.feec.javafx.api.BookBasicView;
import org.but.feec.javafx.api.BookEditView;
import org.but.feec.javafx.data.BookRepository;
import org.but.feec.javafx.services.BookService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BookEditController {

    private static final Logger logger = LoggerFactory.getLogger(BookEditController.class);

    @FXML
    public Button editBookButton;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField isbnTextField;
    @FXML
    private TextField statusTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField quantityTextField;

    private BookService bookService;
    private BookRepository bookRepository;
    private ValidationSupport validation;


    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage == null) {
            logger.error("Stage is still null after setStage()");
        } else {
            logger.info("Stage successfully set in BooksEditController.");
        }
    }

    @FXML
    public void initialize() {
        bookRepository = new BookRepository();
        bookService = new BookService(bookRepository);
        if (stage == null) {
            logger.error("Stage is not set in BooksEditController.");
        } else {
            logger.info("Stage is set in BooksEditController.");
        }

        validation = new ValidationSupport();
        idTextField.setEditable(false);
        validation.registerValidator(nameTextField, Validator.createEmptyValidator("The name must not be empty."));
        validation.registerValidator(isbnTextField, Validator.createEmptyValidator("The ISBN must not be empty."));
        validation.registerValidator(statusTextField, Validator.createEmptyValidator("The status must not be empty."));
        validation.registerValidator(priceTextField, Validator.createEmptyValidator("The price must not be empty."));
        validation.registerValidator(quantityTextField, Validator.createEmptyValidator("The quantity must not be empty."));

        editBookButton.disableProperty().bind(validation.invalidProperty());

        Platform.runLater(this::loadBookData);

        logger.info("BooksEditController initialized");
    }

    private void loadBookData() {
        if (this.stage == null) {
            logger.error("Stage is null. Cannot load book data.");
            return;
        }
        if (stage.getUserData() instanceof BookBasicView) {
            BookBasicView bookBasicView = (BookBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(bookBasicView.getId()));
            nameTextField.setText(bookBasicView.getName());
            isbnTextField.setText(bookBasicView.getIsbn());
            statusTextField.setText(bookBasicView.getStatus());
            priceTextField.setText(String.valueOf(bookBasicView.getPrice()));
            quantityTextField.setText(String.valueOf(bookBasicView.getQuantity()));
        } else {
            logger.error("Stage user data is not an instance of BookBasicView.");
        }
    }


    @FXML
    public void handleEditBookButton(ActionEvent event) {
        Long id = Long.valueOf(idTextField.getText());
        String name = nameTextField.getText();
        String isbn = isbnTextField.getText();
        String status = statusTextField.getText();
        Integer price = Integer.valueOf(priceTextField.getText());
        Integer quantity = Integer.valueOf(quantityTextField.getText());

        BookEditView bookEditView = new BookEditView();
        bookEditView.setId(id);
        bookEditView.setName(name);
        bookEditView.setIsbn(isbn);
        bookEditView.setStatus(status);
        bookEditView.setPrice(price);
        bookEditView.setQuantity(quantity);

        bookService.editBook(bookEditView);

        bookEditedConfirmationDialog();
    }

    private void bookEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Edited Confirmation");
        alert.setHeaderText("Your book was successfully edited.");

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

        Stage stage = (Stage) editBookButton.getScene().getWindow();
        stage.close();
    }

}
