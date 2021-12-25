package com.passamu.utils;

import com.passamu.App;
import com.passamu.controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.stage.*;

import java.io.File;
import java.io.IOException;

/**
 * Classe de différents outils de UI pour le programme tel que des fenêtres modal ou d'erreurs
 */
public class UIFunction {
    public static boolean passwordValidate, windowOpen, folderValidate;
    public static String password;
    public static String folder;

    
    /**
     * Ouvre le selecteur de fichier
     * @param stage Stage de l'application
     * @param taskName String nom de la tache qui sera affiché en haut
     * @param extension String de l'extension du fichier a chercher
     * @param description String de la description
     * @return File que l'on cherchait
     */
    public static File showFileChooser(Stage stage, String taskName, String extension, String description) {
        File ret = null;
        final FileChooser fc = new FileChooser();
        fc.setTitle(taskName);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(description, extension),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fc.showOpenDialog(stage);
            return file;
    }

    
    /**
     * Ouvre le selecteur d'où sauvegarder le fichier
     * @param stage Stage de l'application
     * @param taskName String nom de la tache qui sera affiché en haut
     * @param extension String de l'extension du fichier a chercher
     * @param description String de la description
     * @return File que l'on veux réécrire
     */
    public static File showSaveChooser(Stage stage, String taskName, String extension, String description) {
        File ret = null;
        final FileChooser fc = new FileChooser();
        fc.setTitle(taskName);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(description, extension),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fc.showSaveDialog(stage);
        return file;
    }
    
    /**
     * Fenêtre d'entrée de mot de passe
     * @return byte[] du password
     */
    public static byte[] openDialog(){
        final double x = 0;
        double y = 0;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/password.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            windowOpen=true;
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!passwordValidate && windowOpen){}
        if (password == null){
            return null;
        }
        byte[] passwordHash = null;
        try {
            passwordHash = CryptUtils.getPKCS5Sha256Hash(password.toCharArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return passwordHash;

    }

    /**
     * Ouvre une fenêtre modal du nom d'un dossier
     * @return le nom du fichier
     */
    public static String openFolderName(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/folderName.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            windowOpen=true;
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!folderValidate && windowOpen){}
        if (folder == null){
            return null;
        }
        return folder;

    }

    
    /**
     * Ouvre une fenêtre d'erreur
     * @param errormsg String du message d'erreur
     */
    public static void openErrorDialog(String errormsg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/error.fxml"));
                Scene scene = null;
                try {
                    Parent test =fxmlLoader.load();
                    scene = new Scene(test);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    ErrorController controlleri = fxmlLoader.getController();
                    controlleri.setErrorMessage(errormsg);
                    windowOpen = true;
                    stage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    
    /**
     * Ouvre la fenêtre de question
     * @param question String de la question
     * @return String retour reponse (erreur si -1)
     */
    public static String openYesNoDialog(String question) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/yesNo.fxml"));
        Scene scene = null;
        boolean output;
        try {
            Parent test =fxmlLoader.load();
            scene = new Scene(test);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            YesNoController controlleri = fxmlLoader.getController();
            controlleri.setYesNoMessage(question);
            windowOpen = true;
            stage.showAndWait();
            String str = controlleri.isOutput() ? "1"  : "0";
            return str;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "-1";
    }
}
