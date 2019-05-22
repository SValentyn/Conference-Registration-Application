package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Permission;

/**
 * The application is a window for registering participants for the conference.
 * The basis is the use of RMI technology and JavaFX library.
 * An application data sharing scheme, Model-View-Controller, was used.
 *
 * @author Syniuk Valentyn
 * @version 1.0
 */
public class Server extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("java.rmi.server.codebase", "file://codebase.jar");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager() {
                @Override
                public void checkConnect(String host, int port) {
                }

                @Override
                public void checkConnect(String host, int port, Object context) {
                }

                @Override
                public void checkPermission(Permission permission) {
                }
            });
        }

        /* The fxml-file (formed by SceneBuilder) is loaded to display the window with the components */
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/server.fxml"));
        root.getStylesheets().add(getClass().getResource("/decor/styles.css").toExternalForm());

        stage.setScene(new Scene(root, 800, 525));
        stage.getIcons().add(new Image(getClass().getResource("/decor/icon.png").toString()));
        stage.setTitle("Conference");
        stage.setX(20);
        stage.setY(450);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

}


