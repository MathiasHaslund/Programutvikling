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
 * The main .java file that runs all the other java,fxml and css files.
 */

public class GameOfLife extends Application {
    
    @Override
    /**
     * The main method that runs the whole scene (Game board and the gui).
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
        
        Scene scene = new Scene(root);
        /**
         * Importing the CSS into the main stage.
         */
        scene.getStylesheets().add("gameoflife/css/GameOfLife.css");
        stage.setScene(scene);
        /**
         * Giving the window a title.
         */
        stage.setTitle("The Game of Life");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
