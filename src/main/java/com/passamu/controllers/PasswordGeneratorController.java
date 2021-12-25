package com.passamu.controllers;

import com.passamu.utils.CopyUtils;
import com.passamu.utils.CryptUtils;
import com.passamu.utils.UIFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controller du générateur de Mot de Passe
 */
public class PasswordGeneratorController implements Initializable {

    @FXML
    private Button copyButton;

    @FXML
    private Button btnGenerate;

    @FXML
    private ToggleSwitch customSwitch;

    @FXML
    private TextField customSymbolList;

    @FXML
    private TextField generatedPass;

    @FXML
    private ToggleSwitch lowerCaseSwitch;

    @FXML
    private ToggleSwitch numberSwitch;

    @FXML
    private Spinner<Integer> passwordLength = new Spinner<>();

    @FXML
    private AnchorPane scene2;

    Stage stage;


    @FXML
    private ToggleSwitch uperCaseSwitch;

    @FXML
    private Button validButton;

    /**
     * Tableau de String contenant les symboles avancés
     */
    private static final String[] PASSWORD_OPTIONS = {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz", "0123456789"};

    private final Random random = CryptUtils.newRandomNumberGenerator();
    /**
     * @param event de type MouseEvent permettant 
     */
    @FXML
    void changeCustomSymbol(MouseEvent event) {
            customSymbolList.setEditable(customSwitch.isSelected());
    }

    /**
     * @param event de type ActionEvent. Génère un mot de passe aléatoire.
     */
    @FXML
    void GeneratePassword(ActionEvent event) {
        System.out.println(lowerCaseSwitch.isSelected());
        String characterSet = "";
        if (this.uperCaseSwitch.isSelected()) {
            characterSet += PASSWORD_OPTIONS[0];
        }
        if (this.lowerCaseSwitch.isSelected()) {
            characterSet += PASSWORD_OPTIONS[1];
        }
        if (this.numberSwitch.isSelected()) {
            characterSet += PASSWORD_OPTIONS[2];
        }

        if (this.customSwitch.isSelected()) {
            characterSet += this.customSymbolList.getText();
        }

        if (characterSet.isEmpty()) {
            UIFunction.openErrorDialog("Cannot generate password.\nPlease select a character set.");
            return;
        }

        StringBuilder generated = new StringBuilder();
        int passwordLength = Integer.parseInt(String.valueOf(this.passwordLength.getValue()));
        for (int i = 0; i < passwordLength; i++) {
            generated.append(characterSet.charAt(this.random.nextInt(characterSet.length())));
        }
        this.generatedPass.setText(generated.toString());
    }


    /**
     * @param event
     * //
     */
    @FXML
    void validate(ActionEvent event) {

    }


    /**
     * @param e de type ActionEvent. Fermeture de la fenêtre concernée.
     */
    @FXML
    public void close(ActionEvent e) {
        stage = (Stage) scene2.getScene().getWindow();
        System.out.println("fermeture");
        stage.close();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, 15);
        passwordLength.setValueFactory(valueFactory);
    }
    @FXML
    void copyPass(ActionEvent event) {
        CopyUtils.copyEntryField(generatedPass.getText());
    }
}
