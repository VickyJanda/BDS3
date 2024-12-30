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


        setupSearch();

        logger.info("PersonsController initialized");
    }

    private FilteredList<PersonBasicView> filteredData;

    private void setupSearch() {


        searchIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getId().toString().contains(newValue);
            });
        });

        searchEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getEmail().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchGivenNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getGivenName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchFamilyNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getFamilyName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        searchAgeField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getAge().contains(newValue);
            });
        });

        searchPhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue.isEmpty()) return true;
                return person.getPhoneNumber().contains(newValue);
            });
        });


        SortedList<PersonBasicView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(systemPersonsTableView.comparatorProperty());
        systemPersonsTableView.setItems(sortedData);
    }

    public void loadNewContent(String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/but/feec/javafx/fxml/" + fxml));
            AnchorPane newContent = loader.load();
            contentAnchorPane.getChildren().setAll(newContent);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error loading FXML: " + fxml, e);
        }
    }


    @FXML
    private void handleSwitchUserButton() {
        loadNewContent("UserTable.fxml");
    }

    @FXML
    private void handleSwitchBookButton() {
        loadNewContent("BookTable.fxml");
    }

    @FXML
    private void handleSwitchAuthorButton() {
        loadNewContent("AuthorTable.fxml");
    }

    @FXML
    private void handleSwitchLocationButton() {
        loadNewContent("LocationTable.fxml");
    }

    @FXML
    private void handleSwitchFineButton() {
        loadNewContent("FineTable.fxml");
    }


    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit user");
        MenuItem detailedView = new MenuItem("Detailed user view");
        MenuItem delete = new MenuItem("Delete");
        edit.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            if (personView != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(App.class.getResource("fxml/PersonEdit.fxml"));


                    Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                    PersonsEditController controller = fxmlLoader.getController();


                    Stage stage = new Stage();
                    stage.setTitle("Edit User");


                    stage.setUserData(personView);


                    controller.setStage(stage);

                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ExceptionHandler.handleException(ex);
                }
            } else {

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
                stage.setTitle("User Detailed View");

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

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this person?");
                alert.setContentText("ID: " + selectedPerson.getId() + "\nName: "
                        + selectedPerson.getGivenName() + " "
                        + selectedPerson.getFamilyName());


                if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

                    personService.deletePersonById(selectedPerson.getId());
                    refresh();
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Deletion Successful");
                    info.setHeaderText(null);
                    info.setContentText("The person has been deleted successfully.");
                    info.showAndWait();
                }
            } else {

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
            stage.setTitle("Create User");


            PersonCreateController controller = fxmlLoader.getController();
            controller.setPersonsController(this);

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

    @FXML
    public void handleSimulateButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/SimulateAttack.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("BDS Database Simulate Attack");


            SimulateAttackController controller = fxmlLoader.getController();


            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

}
