package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The application is a window for sending the data of each new conference participant.
 * The basis is the use of RMI technology and JavaFX library.
 * An application data sharing scheme, Model-View-Controller, was used.
 *
 * @author Syniuk Valentyn
 * @version 1.0
 */
public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        /* The fxml-file (formed by SceneBuilder) is loaded to display the window with the components */
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/client.fxml"));
        root.getStylesheets().add(getClass().getResource("/decor/styles.css").toExternalForm());

        stage.setScene(new Scene(root, 800, 525));
        stage.getIcons().add(new Image(getClass().getResource("/decor/icon.png").toString()));
        stage.setTitle("Registration");
        stage.setX(1100);
        stage.setY(450);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

}