package com.passamu.controllers;

import com.passamu.utils.UIFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller de la fenetre modal Password
 */
public class PasswordController {

    @FXML
    private Button closePasswordButton;

    @FXML
    private Label crdNoteText;

    @FXML
    private PasswordField filePassword;
    @FXML
    private AnchorPane scene2;
    @FXML
    private Button validPasswordButton;
    Stage stage;

    
    /** 
     * @param event est la variable de type ActionEvent prise en paramètre afin de fermer la fenêtre concernée, optimisée pour JavaFX.
     */
    @FXML
    void close(ActionEvent event) {

        stage = (Stage) scene2.getScene().getWindow();
        System.out.println("fermeture");
        UIFunction.windowOpen = false;
        stage.close();
    }

    
    /** 
     * @param event est la variable de type ActionEvent prise en paramètre afin de fermer la fenêtre concernée, cette fonction est nécessaire au bon fonctionnement de JavaFX.
     */
    @FXML
    void validate(ActionEvent event) {
        if (filePassword.getText() == "") {
            close(event);

        } else {
            UIFunction.password = filePassword.getText();
            UIFunction.passwordValidate = true;
            close(event);
        }

    }
}
