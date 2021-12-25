package com.passamu.controllers;

import java.net.MalformedURLException;
import java.net.URL;

import com.passamu.utils.UIFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;


public class ItemController{

    @FXML
    private Label listAppName;

    @FXML
    private ImageView listLogo;

    @FXML
    private Label listUser;

    
    /**
     * Fonction qui se lance au démarage du controller
     * @param url
     * @param resourceBundle
     * @Descritpion Initialisation.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Fonction qui définit le contenu d'un item
     * @param _listName est le nom de type String prit en paramètre pour le fonctionnement de ce setter.
     * @param _listUser est l'utilisateur de type String prit en paramètre pour le fonctionnement de ce setter.
     * @param _listWebsite est le site web de type String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setInfoItem(String _listName, String _listUser, String _listWebsite) {
        listAppName.setText(_listName);
        listUser.setText(_listUser);
        try {
            URL website = new URL(_listWebsite);
            String path = "https://www.google.com/s2/favicons?sz=128&domain_url="+website.getHost();
            listLogo.setImage(new Image(path));

        } catch (MalformedURLException e) {
            UIFunction.openErrorDialog(e.getMessage());
        }
    }
}
