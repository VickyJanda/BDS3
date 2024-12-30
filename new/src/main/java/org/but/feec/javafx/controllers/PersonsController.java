package org.but.feec.javafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.but.feec.javafx.App;
import org.but.feec.javafx.api.PersonBasicView;
import org.but.feec.javafx.api.PersonDetailView;
import org.but.feec.javafx.data.PersonRepository;
import org.but.feec.javafx.exceptions.ExceptionHandler;
import org.but.feec.javafx.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class PersonsController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsController.class);

    @FXML
    public Button addPersonButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button simulateButton;
    @FXML
    private TableColumn<PersonBasicView, Long> personsId;
    @FXML
    private TableColumn<PersonBasicView, String> personsEmail;
    @FXML
    private TableColumn<PersonBasicView, String> personsFamilyName;
    @FXML
    private TableColumn<PersonBasicView, String> personsGivenName;
    @FXML
    private TableColumn<PersonBasicView, String> personsAge;
    @FXML
    private TableColumn<PersonBasicView, String> personsPhoneNumber;
    @FXML
    private TableView<PersonBasicView> systemPersonsTableView;
    @FXML
    private ScrollPane contentScrollPane; // Reference to the ScrollPane that holds the content
    @FXML
    private AnchorPane contentAnchorPane;


    @FXML
    private TextField searchIdField, searchEmailField, searchGivenNameField, searchFamilyNameField, searchAgeField, searchPhoneField;


    private PersonService personService;
    private PersonRepository personRepository;

    public PersonsController() {
    }

    @FXML
    private void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);

        personsId.setCellValueFactory(new PropertyValueFactory<PersonBasicView, Long>("id"));
        personsEmail.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("email"));
        personsFamilyName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("familyName"));
        personsGivenName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("givenName"));
        personsAge.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("age"));
        personsPhoneNumber.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("phoneNumber"));

        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        filteredData = new FilteredList<>(observablePersonsList, p -> true);
        systemPersonsTableView.setItems(filteredData);


        systemPersonsTableView.getSortOrder().add(personsId);

        initializeTableViewSelection();
        loadIcons();

        // Initialize search functionality
        setupSearch();

        logger.info("PersonsController initialized");
    }
    private FilteredList<PersonBasicView> filteredData;
    private void setupSearch() {
        // Create a FilteredList to hold filtered data

        // Add listeners to search fields to filter data
        searchIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true; // If the field is empty, show all results
                return person.getId().toString().contains(newValue); // Filter by ID
            });
        });

        searchEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getEmail().toLowerCase().contains(newValue.toLowerCase()); // Filter by email
            });
        });

        searchGivenNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getGivenName().toLowerCase().contains(newValue.toLowerCase()); // Filter by given name
            });
        });

        searchFamilyNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getFamilyName().toLowerCase().contains(newValue.toLowerCase()); // Filter by family name
            });
        });

        searchAgeField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getAge().contains(newValue); // Filter by age
            });
        });

        searchPhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getPhoneNumber().contains(newValue); // Filter by phone number
            });
        });

        // Bind the filtered data to the table's items property
        SortedList<PersonBasicView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(systemPersonsTableView.comparatorProperty());
        systemPersonsTableView.setItems(sortedData);
    }

    public void loadNewContent(String fxml) {
        try {
            // Correctly load the FXML with the absolute path from the resources directory
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/but/feec/javafx/fxml/" + fxml));
            AnchorPane newContent = loader.load();
            contentAnchorPane.getChildren().setAll(newContent); // Replace the current content
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error loading FXML: " + fxml, e);
        }
    }


    @FXML
    private void handleSwitchUserButton() {
        loadNewContent("UserTable.fxml"); // Load user table content
    }

    @FXML
    private void handleSwitchBookButton() {
        loadNewContent("BookTable.fxml"); // Load library table content
    }

    @FXML
    private void handleSwitchAuthorButton() {
        loadNewContent("AuthorTable.fxml"); // Load library table content
    }

    @FXML
    private void handleSwitchLocationButton() {
        loadNewContent("LocationTable.fxml"); // Load library table content
    }

    @FXML
    private void handleSwitchFineButton() {
        loadNewContent("FineTable.fxml"); // Load library table content
    }



    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit person");
        MenuItem detailedView = new MenuItem("Detailed person view");
        MenuItem delete = new MenuItem("Delete");
        edit.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            if (personView != null) { // Ensure a person is selected
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(App.class.getResource("fxml/PersonEdit.fxml"));

                    // Load the FXML and get the controller
                    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                    PersonsEditController controller = fxmlLoader.getController();

                    // Create and configure the stage
                    Stage stage = new Stage();
                    stage.setTitle("BDS JavaFX Edit Person");

                    // Set the user data
                    stage.setUserData(personView);

                    // Ensure the controller receives the stage
                    controller.setStage(stage); // Pass the stage to the controller

                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ExceptionHandler.handleException(ex);
                }
            } else {
                // Show an alert if no person is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No Selection");
                alert.setContentText("Please select a person to edit.");
                alert.showAndWait();
            }
        });


        detailedView.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonsDetailView.fxml"));
                Stage stage = new Stage();

                Long personId = personView.getId();
                PersonDetailView personDetailView = personService.getPersonDetailView(personId);

                stage.setUserData(personDetailView);
                stage.setTitle("BDS JavaFX Persons Detailed View");

                PersonsDetailViewController controller = new PersonsDetailViewController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });
        delete.setOnAction((ActionEvent event) -> {
            PersonBasicView selectedPerson = systemPersonsTableView.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Show confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this person?");
                alert.setContentText("ID: " + selectedPerson.getId() + "\nName: "
                        + selectedPerson.getGivenName() + " "
                        + selectedPerson.getFamilyName());

                // Wait for user response
                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    // Delete the person from the database
                    personService.deletePersonById(selectedPerson.getId());
                    refresh(); // Refresh the table view
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Deletion Successful");
                    info.setHeaderText(null);
                    info.setContentText("The person has been deleted successfully.");
                    info.showAndWait();
                }
            } else {
                // Show an alert if no person is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No Selection");
                alert.setContentText("Please select a person to delete.");
                alert.showAndWait();
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(edit, detailedView, delete);
        systemPersonsTableView.setContextMenu(menu);
    }

    private ObservableList<PersonBasicView> initializePersonsData() {
        if (personService == null) {
            logger.error("PersonService is null. Initialization failed.");
            throw new IllegalStateException("PersonService not initialized.");
        }
        List<PersonBasicView> persons = personService.getPersonsBasicView();
        return FXCollections.observableArrayList(persons);
    }


    private void loadIcons() {
        Image vutLogoImage = new Image(App.class.getResourceAsStream("logos/vut-logo-eng.png"));
        ImageView vutLogo = new ImageView(vutLogoImage);
        vutLogo.setFitWidth(150);
        vutLogo.setFitHeight(50);
    }

    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddPersonButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/PersonsCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS JavaFX Create Person");

            // Get the controller instance and pass this instance of PersonsController
            PersonCreateController controller = fxmlLoader.getController();
            controller.setPersonsController(this);  // Pass the current instance of PersonsController

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        refresh();
    }

    public void refresh() {
        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);
        systemPersonsTableView.refresh();
        systemPersonsTableView.sort();
        filteredData = new FilteredList<>(observablePersonsList, p -> true);
        systemPersonsTableView.setItems(filteredData);
    }
    public void handleSimulateButton(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/SimulateAttack.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("BDS JavaFX Create Person");

            // Get the controller instance and pass this instance of PersonsController
            SimulateAttackController controller = fxmlLoader.getController();
            controller.setPersonsController(this);  // Pass the current instance of PersonsController

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }
}
