/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import static com.sun.deploy.trace.Trace.flush;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;


/**
 *
 * @author Mathias
 */
public class gameBoardController implements Initializable{
    
    gameBoardModel gameBoardModel = new gameBoardModel();
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initBoard();
    }
    
    @FXML
    private GridPane gridPane1;
    
    @FXML
    private void initBoard() {
        gameBoardModel.initCellStates();
        for (int i=0; i<gameBoardModel.xmax; i++)
        {
            for (int j=0; j<gameBoardModel.ymax; j++)
            {
                String buttonId = "cell_"+i+"_"+j;
                Button button = new Button();
                
                refreshButton(button, gameBoardModel.getCellState(i, j));

                    
                button.setId(buttonId);
                button.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                    writeCellClickToModel(buttonId);
                    refreshBoard();
                    };
                });
                       
                gridPane1.add(button, i, j);
            }
        }
    }
    private void writeCellClickToModel(String buttonId){   
        int p1 = buttonId.indexOf("_",0);
        int p2 = buttonId.indexOf("_",p1+1);
        int x = Integer.parseInt(buttonId.substring(p1+1, p2));
        int y = Integer.parseInt(buttonId.substring(p2+1));
        gameBoardModel.toggleCellState(x, y);
        
    }
    /*Updates the view of the whole board from list*/
    private void refreshBoard(){
        int[] coordinates = gameBoardModel.takeNextCellChange();
        refreshButtonAtCoordinates(coordinates);
    }
    /*refreshes the view of a single cell*/
    private void refreshButton(Button button, boolean cellState){
            if (cellState){
                button.setText("X");
            }
            else{
                button.setText(" ");
            }
    }
    /*gets the cell ID of view cell by deconstructing coordinates from array, and sends it for view refreshing */
    private void refreshButtonAtCoordinates(int[] coordinates){
        int x = coordinates[0];
        int y = coordinates[1];
        String buttonId="cell_"+x+"_"+y;
        Button button = (Button) gridPane1.lookup("#"+buttonId);
        refreshButton(button, gameBoardModel.getCellState(x, y));

    }
     
    @FXML
    private void step (){
        gameBoardModel.gameLogic();
        while(true) {
            int[] coordinates = gameBoardModel.takeNextCellChange();           
            if (coordinates == null){
                break;
            }
            refreshButtonAtCoordinates(coordinates);            
        }
    }

    @FXML
    private Button startButton;

    @FXML
    private void playGame(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=1; i<=10; i++) {
                    final int counter = i;

                    Platform.runLater(new Runnable() {
                    @Override
                        public void run() {
                            if(counter == 1){
                                startButton.setText("Stop");
                            }
                            step();
                        }
                        });
                        try {
                            // Wait for 1 second.
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException ex) {}
                }
            }
        }).start();
    }
}
