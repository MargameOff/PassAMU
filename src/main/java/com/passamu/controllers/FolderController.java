package com.passamu.controllers;

import com.passamu.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * controller des petit FolderList
 */
public class FolderController {

    @FXML
    private Button buttontext;

    @FXML
    private ImageView logo;
    /** 
     * @param texte est le texte de type String prise en paramètre pour le setter des dossiers.
     */
    public void setInfo(String texte) {
        setInfo(texte, String.valueOf(App.class.getResource("icons/folder.png")));
    }
    /** 
     * @param texte est le texte de type String prise en paramètre pour le setter des dossiers, avec bonus modifier le logo.
     */
    public void setInfo(String texte, String URLLogo) {
        buttontext.setText(texte);
        logo.setImage(new Image(URLLogo));
    }
}
