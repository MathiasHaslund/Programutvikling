/**
 * Main File
 */
package gameoflife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.7
 * @since 0.1 (5/3/2017)
 */

/**
 * The driver of the visual part of program.
 */
public class GameOfLife extends Application {
    
    @Override
    /**
     * Sets the stage, scene, title and css of the program.
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
    
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add("gameoflife/css/GameOfLife.css");
        stage.setScene(scene);
        stage.setTitle("The Game of Life");
        stage.show();
    }

    /**
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
