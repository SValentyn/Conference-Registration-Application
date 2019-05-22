package controllers;

import client.Client;
import interfaces.Conference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import server.DOMParser;
import server.Registration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

/**
 * Manage the graphical window of the Server application.
 */
public class ServerController implements Initializable {

    @FXML
    public TextField hostField;

    @FXML
    public TextField portField;

    @FXML
    public TextField participantsField;

    @FXML
    public TextArea textArea;

    @FXML
    public Button buttonStart;

    @FXML
    public Button buttonStop;

    @FXML
    public Button buttonSave;

    @FXML
    public Button buttonLoad;

    @FXML
    public Button buttonExit;

    private FileChooser fileChooser = new FileChooser();

    private Conference stub;
    private Registry registry;
    private Document document;
    private Registration registration = new Registration();

    public ServerController() throws RemoteException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostField.setText("127.0.0.1");
        portField.setText("10100");
        portField.requestFocus();
        participantsField.setText("0");
        participantsField.setEditable(false);
        textArea.setEditable(false);
        buttonStop.setDisable(true);
        buttonSave.setDisable(true);
        setPropertyFileChooser();

        constantDataUpdate();
        exportObjectInRegister();
    }

    private void setPropertyFileChooser() {
        fileChooser.setInitialDirectory(new File("src/xml/"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Method for correct updating of the text field data and the number of participants field.
     */
    private void constantDataUpdate() {
        registration.getData().addDataParticipantListener(event -> {
            textArea.setText(registration.getInfo());
            participantsField.setText(String.valueOf(registration.getSize()));
        });
    }

    /**
     * Exports the remote object to make it available to receive incoming calls, using the particular supplied port.
     */
    private void exportObjectInRegister() {
        try {
            stub = (Conference) UnicastRemoteObject.exportObject(registration, 0);
        } catch (RemoteException e) {
            showErrorDialog("FATAL ERROR\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void actionStart() {
        createBinding();
        hostField.setEditable(false);
        portField.setEditable(false);
        buttonStart.setDisable(true);
        buttonSave.setDisable(true);
        buttonLoad.setDisable(true);
        buttonStop.setDisable(false);
        showInfoDialog("Server started successfully!");
    }

    /**
     * Creates and exports a Registry instance on the local host that accepts requests on the specified port.
     */
    private void createBinding() {
        try {
            if (registry == null) registry = LocateRegistry.createRegistry(Integer.parseInt(portField.getText()));
            String name = "Registrable";
            registry.rebind(name, stub);
        } catch (RemoteException e) {
            showErrorDialog("Could not start server\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void actionStop() {
        removeBinding();
        hostField.setEditable(true);
        portField.setEditable(true);
        buttonStart.setDisable(false);
        buttonSave.setDisable(false);
        buttonLoad.setDisable(false);
        buttonStop.setDisable(true);
        showInfoDialog("Server successfully stopped!");
    }

    /**
     * Removes the binding for the specified name in this registry.
     */
    private void removeBinding() {
        try {
            Registry registry = LocateRegistry.getRegistry(Integer.parseInt(portField.getText()));
            String name = "Registrable";
            registry.unbind(name);
        } catch (RemoteException | NotBoundException e) {
            showErrorDialog("Could not stop server\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void loadDataFromFile() {
        File file = selectFileToLoad();
        if (file != null) {
            try {
                document = DOMParser.parse(file.getPath());
                DOMParser.transformDocumentToData(document, registration.getData());
            } catch (SAXException | ParserConfigurationException | IOException e) {
                showErrorDialog("Could not load\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private File selectFileToLoad() {
        fileChooser.setTitle("Load data");
        return fileChooser.showOpenDialog(buttonLoad.getScene().getWindow());
    }

    @FXML
    public void saveDataToFile() {
        File file = selectFileToSave();
        if (file != null) {
            try {
                document = DOMParser.transformDataToDocument(registration.getData());
                save(document, file.getPath());
                showInfoDialog("Saved successfully!");
            } catch (TransformerException | ParserConfigurationException | IllegalAccessException e) {
                showErrorDialog("Could not save\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private File selectFileToSave() {
        fileChooser.setTitle("Save data");
        fileChooser.setInitialFileName("Participants_new");
        return fileChooser.showSaveDialog(buttonSave.getScene().getWindow());
    }

    /**
     * The method allows you to save data to a xml-file.
     *
     * @param document processed document file {@link DOMParser#transformDataToDocument}.
     * @param fileName the selected document on the working machine.
     */
    private void save(Document document, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

    @FXML
    public void actionExit() {
        Platform.exit();
        System.exit(0);
    }

    private void showInfoDialog(String info) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info Dialog");
            alert.setHeaderText("INFO: " + info);
            alert.setContentText(null);
            alert.setResizable(false);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Client.class.getResource("../decor/icon.png").toString()));

            Stage mainStage = (Stage) buttonStart.getScene().getWindow();
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
            stage.getIcons().add(new Image(Client.class.getResource("../decor/icon.png").toString()));

            Stage mainStage = (Stage) buttonStart.getScene().getWindow();
            alert.initOwner(mainStage);
            alert.show();
        });
    }
}
