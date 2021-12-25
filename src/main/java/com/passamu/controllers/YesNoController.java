package com.passamu.controllers;

import com.passamu.utils.UIFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller de la fenettre modal Yes No
 */
public class YesNoController {

    @FXML
    private Button AnnulButton;

    private boolean output;
    @FXML
    private AnchorPane scene2;

    @FXML
    private Button validButton;

    @FXML
    private Text yesNoText;

    
    /** 
     * @param event de type ActionEvent : Fermeture de la fenêtre concernée.
     */
    @FXML
    void yes(ActionEvent event) {
        output=true;
        close(event);
    }
    
    /** 
     * @param event de type ActionEvent : Fermeture de la fenêtre concernée.
     */
    @FXML
    void no(ActionEvent event) {
        output=false;
        close(event);
    }

    
    /** 
     * @return boolean
     */
    public boolean isOutput() {
        return output;
    }

    
    /** 
     * @param _errorText servant de setter pour un message d'erreur Yes/No.
     */
    public void setYesNoMessage(String _errorText) {
        yesNoText.setText(_errorText);

    }
    Stage stage;
    
    /** 
     * @param event de type ActionEvent : Fermeture de la fenêtre concernée.
     */
    @FXML
    void close(ActionEvent event) {

        stage = (Stage) scene2.getScene().getWindow();
        System.out.println("fermeture");
        UIFunction.windowOpen = false;
        stage.close();
    }
}
