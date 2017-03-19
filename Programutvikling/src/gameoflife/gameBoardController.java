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
                
                /*Does not use the game board cell object for improved performance*/
                refreshButton(button, gameBoardModel.getCellIsAlive(i, j));

                    
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
        gameBoardCell gameBoardCell = gameBoardModel.takeNextCellChange();
        refreshButtonAtCoordinates(gameBoardCell);
    }
    
    @FXML
    private void clearBoard(){
        if (gameRunning == true){
            startStopGame();
        }
        gameBoardModel.initCellStates();
        rePaintBoard();
        
    }
    
    @FXML
    private void rePaintBoard(){
        for (int i = 0; i<gameBoardModel.xmax; i++){
            for (int j = 0; j<gameBoardModel.ymax; j++){
                String buttonId="cell_"+i+"_"+j;
                Button button = (Button) gridPane1.lookup("#"+buttonId);
                refreshButton(button, gameBoardModel.getCellIsAlive(i, j));
            }
        }
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
    private void refreshButtonAtCoordinates(gameBoardCell gameBoardCell){        
        String buttonId="cell_"+gameBoardCell.getX()+"_"+gameBoardCell.getY();
        Button button = (Button) gridPane1.lookup("#"+buttonId);
        refreshButton(button, gameBoardCell.isAlive());
    }
     
    @FXML
    private void step (){
        gameBoardModel.gameLogic();
        while(true) {
            gameBoardCell gameBoardCell = gameBoardModel.takeNextCellChange();           
            if (gameBoardCell == null){
                break;
            }
            refreshButtonAtCoordinates(gameBoardCell);            
        }
    }

    @FXML
    private Button startButton;
    
    private boolean gameRunning = false;
    
    /*@FXML
    private void startStopGame(){
        if (gameRunning == false){
            
        }       
    }*/

    @FXML
    private void startStopGame(){
        if(gameRunning == true){
            gameRunning = false;
            startButton.setText("Start");
            return;
        }        
        gameRunning = true;
        startButton.setText("Stop");            
                
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (gameRunning == true) {
                    Platform.runLater(new Runnable() {
                    @Override
                        public void run() {
                            step();
                        }
                        });
                        try {
                            // Wait for 1 second.
                            Thread.sleep(gameBoardModel.getCurrentTickTime());
                        }
                        catch (InterruptedException ex) {}
                }
            }
        }).start();
    }
    
    
    
}
