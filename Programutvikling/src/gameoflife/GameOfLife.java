/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Espen
 */
public class GameOfLife extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gameBoard.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("gameoflife/css/GameOfLife.css");
        stage.setScene(scene);
        stage.setTitle("The Game of Life (Beta 0.3). now with more dududu");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
