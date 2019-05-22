package controllers;

import client.Client;
import interfaces.Conference;
import interfaces.Participant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Manage the graphical window of the Client application.
 */
public class ClientController implements Initializable {

    @FXML
    public TextField hostField;

    @FXML
    public TextField portField;

    @FXML
    public TextField participantsField;

    @FXML
    public TextField nameField;

    @FXML
    public TextField surnameField;

    @FXML
    public TextField orgField;

    @FXML
    public TextField reportField;

    @FXML
    public TextField emailField;

    @FXML
    public Button regButton;

    @FXML
    public Button clearButton;

    @FXML
    public Button infoButton;

    @FXML
    public Button exitButton;

    private Conference stub;
    private ArrayList<Participant> participants = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostField.setText("127.0.0.1");
        portField.setText("10100");
        participantsField.setText("0");
        participantsField.setEditable(false);
        infoButton.setDisable(true);
    }

    @FXML
    public void actionRegistration() throws RemoteException {
        hostField.setEditable(false);
        portField.setEditable(false);
        infoButton.setDisable(false);
        registerParticipant();
    }

    /**
     * Registration of a new conference participant.
     */
    private void registerParticipant() throws RemoteException {
        getLocateRegistry();
        updateInfo();
        showInfoDialog("Thank you for registering!");
    }

    /**
     * Returns a reference to the remote object Registry on the specified host and port.
     */
    private void getLocateRegistry() {
        try {
            Registry registry = LocateRegistry.getRegistry(hostField.getText(), Integer.parseInt(portField.getText()));
            String findName = "Registrable";
            stub = (Conference) registry.lookup(findName); // Binds a remote reference to the specified name in this registry
        } catch (RemoteException | NotBoundException e) {
            showErrorDialog("Failed to register\n" + e.getMessage());
            hostField.setEditable(true);
            portField.setEditable(true);
            infoButton.setDisable(true);
            e.printStackTrace();
        }
    }

    /**
     * Method for correct updating of the list of participants and the number of participants field.
     */
    private void updateInfo() throws RemoteException {
        participants.add(getParticipantInfo());
        participantsField.setText(String.valueOf(stub.register(getParticipantInfo())));
    }

    private Participant getParticipantInfo() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String organization = orgField.getText();
        String report = reportField.getText();
        String email = emailField.getText();
        return new Participant(name, surname, organization, report, email);
    }

    @FXML
    public void actionClear() {
        nameField.clear();
        surnameField.clear();
        orgField.clear();
        reportField.clear();
        emailField.clear();
    }

    @FXML
    public void getInfo() {
        try {
            if (stub == null) getLocateRegistry();
            participantsField.setText(String.valueOf(stub.getSize()));
            getInfoWindow(stub.getInfo());
        } catch (RemoteException | NullPointerException e) {
            showErrorDialog("Failed to get information\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * A window of information about all conference participants is created.
     */
    private void getInfoWindow(String info) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/info.fxml"));

            Parent infoWindow = loader.load();
            infoWindow.getStylesheets().add(getClass().getResource("/decor/styles.css").toExternalForm());

            InfoController infoController = loader.getController();
            infoController.initializeData(info);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(infoWindow));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(getClass().getResource("/decor/icon.png").toString()));
            stage.setTitle("Full information");
            stage.setX(1290);
            stage.setY(10);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            showErrorDialog("Could not open window with additional information\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void actionExit() {
        Platform.exit();
        System.exit(0);
    }

    private void showInfoDialog(String info) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("INFO: " + info);
            alert.setContentText(null);
            alert.setResizable(false);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Client.class.getResource("/decor/icon.png").toString()));

            Stage mainStage = (Stage) infoButton.getScene().getWindow();
            alert.initOwner(mainStage);
            alert.show();
        });
    }

    private void showErrorDialog(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("ERROR: " + error);
            alert.setContentText(null);
            alert.setResizable(false);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Client.class.getResource("/decor/icon.png").toString()));

            Stage mainStage = (Stage) regButton.getScene().getWindow();
            alert.initOwner(mainStage);
            alert.show();
        });
    }

}
