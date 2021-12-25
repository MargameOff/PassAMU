package com.passamu.controllers;

import com.passamu.utils.UIFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller du widget modal ErrorController
 */
public class ErrorController {

    @FXML
    private Label errorName;

    @FXML
    private Text errorText;

    @FXML
    private AnchorPane scene2;

    @FXML
    private Button validButton;

    Stage stage;

    
    /** 
     * @param event est la variable de type ActionEvent prise en paramètre afin de fermer la fenêtre concernée.
     */
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
        close(event);
    }


    
    /** 
     * @param _errorText est le message d'erreur de type String en paramètre pour le fonctionnement de ce setter.
     */
    public void setErrorMessage(String _errorText) {
        errorText.setText(_errorText);
    }
}
