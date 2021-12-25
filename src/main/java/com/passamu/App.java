package com.passamu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    private double x = 0, y = 0;

    
    /** 
     * @param stage est le paramètre utilisé dans la fonction start() qui permet l'affichage graphique de l'application et l'utilisation de la souris/du curseur.
     * @throws IOException Traitement des exceptions.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PassAMU - Marius D");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        scene.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

    }

    
    /** 
     * @param args arguments nécessaires au fonctionnement de la fonction Main qui exécute l'application.
     */
    public static void main(String[] args) {
        launch();
    }
}