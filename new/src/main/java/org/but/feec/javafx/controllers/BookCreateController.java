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
import org.but.feec.javafx.api.BookCreateView;
import org.but.feec.javafx.data.BookRepository;
import org.but.feec.javafx.services.BookService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BookCreateController {

    private static final Logger logger = LoggerFactory.getLogger(BookCreateController.class);

    @FXML
    private Button newBookCreateButton;
    @FXML
    private TextField newBookName;
    @FXML
    private TextField newBookIsbn;
    @FXML
    private TextField newBookStatus;
    @FXML
    private TextField newBookPrice;
    @FXML
    private TextField newBookQuantity;

    private BookService bookService;
    private ValidationSupport validation;

    private BookController bookController;


    public void setBookController(BookController bookController) {
        this.bookController = bookController;
    }

    @FXML
    public void initialize() {

        bookService = new BookService(new BookRepository());


        validation = new ValidationSupport();


        if (newBookName != null) {
            validation.registerValidator(newBookName, Validator.createEmptyValidator("The book name must not be empty."));
        }
        if (newBookIsbn != null) {
            validation.registerValidator(newBookIsbn, Validator.createEmptyValidator("The ISBN must not be empty."));
        }
        if (newBookStatus != null) {
            validation.registerValidator(newBookStatus, Validator.createEmptyValidator("The status must not be empty."));
        }
        if (newBookPrice != null) {
            validation.registerValidator(newBookPrice, Validator.createEmptyValidator("The price must not be empty."));
        }
        if (newBookQuantity != null) {
            validation.registerValidator(newBookQuantity, Validator.createEmptyValidator("The quantity must not be empty."));
        }


        if (newBookCreateButton != null) {
            newBookCreateButton.disableProperty().bind(validation.invalidProperty());
        }

        logger.info("BookCreateController initialized");
    }

    @FXML
    void handleCreateNewBook(ActionEvent event) {
        try {

            String name = newBookName.getText();
            String isbn = newBookIsbn.getText();
            String status = "available";
            Double price = Double.valueOf(newBookPrice.getText());
            Integer quantity = Integer.valueOf(newBookQuantity.getText());


            BookCreateView bookCreateView = new BookCreateView();
            bookCreateView.setName(name);
            bookCreateView.setIsbn(isbn);
            bookCreateView.setStatus(status);
            bookCreateView.setPrice(price);
            bookCreateView.setQuantity(quantity);


            bookService.createBook(bookCreateView);

            bookCreatedConfirmationDialog();
        } catch (NumberFormatException e) {
            logger.error("Error parsing price or quantity: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for price and quantity.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void bookCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Created Confirmation");
        alert.setHeaderText("Your book was successfully created.");


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


        Stage stage = (Stage) newBookCreateButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }


        if (bookController != null) {
            bookController.refresh();
        }
    }
}
