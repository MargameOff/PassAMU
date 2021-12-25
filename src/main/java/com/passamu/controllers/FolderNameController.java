package com.passamu.controllers;

import com.passamu.utils.UIFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller du widget FolderName
 */
public class FolderNameController {

    @FXML
    private Button closeFolderButton;

    @FXML
    private Label crdNoteText;

    @FXML
    private TextField folderName;

    @FXML
    private AnchorPane scene2;

    @FXML
    private Button validFolderButton;

    Stage stage;
    /** 
     * @param event est la variable de type ActionEvent prise en paramètre afin de fermer la fenêtre concernée.
     */
    @FXML
    void close(ActionEvent event) {
        stage = (Stage) scene2.getScene().getWindow();
        System.out.println("fermeture");
        UIFunction.windowOpen = false;
        stage.close();
    }
    /** 
     * @param event est la variable de type ActionEvent prise en paramètre afin de fermer la fenêtre concernée.
     */
    @FXML
    void validate(ActionEvent event) {
            UIFunction.folder = folderName.getText();
            UIFunction.folderValidate = true;
            close(event);
    }

}
