package org.but.feec.javafx;

import org.but.feec.javafx.config.DataSourceConfig;
import org.but.feec.javafx.controllers.SimulateAttackController;
import org.but.feec.javafx.exceptions.ExceptionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.sql.SQLException;

/**
 * @author Viktor Janda
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
            VBox mainStage = loader.load();

            primaryStage.setTitle("BDS Database");
            Scene scene = new Scene(mainStage);
            setUserAgentStylesheet(STYLESHEET_MODENA);
            String myStyle = getClass().getResource("css/myStyle.css").toExternalForm();
            scene.getStylesheets().add(myStyle);

            primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("logos/vut.jpg")));
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> {
                try {

                    SimulateAttackController.dropDummyTable();
                } catch (SQLException e) {
                    System.err.println("Error dropping the table: " + e.getMessage());
                }
            });

            primaryStage.setOnShown(event -> primaryStage.centerOnScreen());
            primaryStage.show();

        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

}