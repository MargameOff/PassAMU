package com.passamu.utils;

import com.passamu.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.*;
import java.io.IOException;

/**
 * Classe qui gere le Password Generator
 */
public class PasswordGenerator {
    private double x = 0, y = 0;


    public PasswordGenerator() throws IOException {
        Stage passwordGenerator = new Stage();
        passwordGenerator.setTitle("test");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/passwordGenerator.fxml"));
        passwordGenerator.initStyle(StageStyle.TRANSPARENT);
        Scene scenePass = new Scene(loader.load());
        passwordGenerator.setScene(scenePass);

        passwordGenerator.show();
        scenePass.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        scenePass.setOnMouseDragged(mouseEvent -> {
            passwordGenerator.setX(mouseEvent.getScreenX() - x);
            passwordGenerator.setY(mouseEvent.getScreenY() - y);
        });
        scenePass.setFill(Color.TRANSPARENT);
    }
}
