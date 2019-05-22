package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Manage the graphical window of the Info.
 */
public class InfoController {

    @FXML
    private TextArea textArea;

    void initializeData(String info) {
        textArea.setEditable(false);
        textArea.setText(info);
    }

}
