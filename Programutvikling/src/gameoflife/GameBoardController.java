/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.css.PseudoClass;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.media.*;


/**
 *
 * @author Mathias
 */


public class GameBoardController implements Initializable{
    
    /*References the button with the text "start/stop" in the gameBoard.fxml*/
    @FXML 
    protected Button startButton;
    
    /*References the grid pane that contains the squares in the main bord in gameBoard.fxml*/
    @FXML
    private GridPane gameGrid;
    
    /**/
    @FXML
    private Slider speedSlider;
    
    /**/
    @FXML
    protected Label roundCounterLabel;
    
    /**/
    private int roundCounter;
        
    protected boolean gameRunning = false;
        
    private GameBoardTile cellViewArray[][];
    
    /**/
    GameBoardModel gameBoardModel = new GameBoardModel();
    
    Sound sound = new Sound();
    
    FileIO fileIO = new FileIO();
            
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sound.playSound(Sound.SoundTypes.BACKGROUND);
        initSlider ();
        initBoard();
    }
      

    
    @FXML
    private void initSlider (){
        gameBoardModel.setGameSpeed(speedSlider.valueProperty().doubleValue());        
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    System.out.println(new_val.doubleValue());
                    gameBoardModel.setGameSpeed(new_val.doubleValue());                    
            }
        });
    }
                
    @FXML
    private void initBoard() {
        gameBoardModel.initCellStates();
        cellViewArray = new GameBoardTile[gameBoardModel.getXmax()][gameBoardModel.getYmax()];
        for (int i=0; i<gameBoardModel.getXmax(); i++)
        {
            for (int j=0; j<gameBoardModel.getYmax(); j++)
            {
                String tileId = "cell_"+i+"_"+j;
                int tileSize = 15;
                GameBoardTile gameBoardTile = new GameBoardTile(tileId, tileSize);
                cellViewArray[i][j] = gameBoardTile;
                
                /*Does not use the game board cell object for improved performance*/
                gameBoardTile.refreshTile(gameBoardModel.getCellIsAlive(i, j));
                gameBoardTile.getTile().setOnAction(new EventHandler<ActionEvent>(){
                
                    @Override
                    public void handle(ActionEvent event) {
                        writeCellClickToModel(tileId);
                        refreshBoard();
                    };
                });                       
                gameGrid.add(gameBoardTile.getTile(), i, j);
            }
        }
    }
    
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
    
    @FXML
    private void step (){
        gameBoardModel.gameLogic();
        roundCounter++;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        long start = System.nanoTime();
        while(true) {           
            GameBoardCell GameBoardCell = gameBoardModel.takeNextCellChange();
            if (GameBoardCell == null){
                break;
            }
            refreshButtonAtCoordinates(GameBoardCell);            
        }
        long stop = System.nanoTime();
        System.out.println("after view update: "+((stop-start)/1000000.0));
    }
    
    @FXML
    private void clearBoard(){
        roundCounter = 0;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        if (gameRunning == true){
            startStopGame();            
        }
        gameBoardModel.initCellStates();
        rePaintBoard();        
    }
        
    private void rePaintBoard(){
        for (int i = 0; i<gameBoardModel.getXmax(); i++){
            for (int j = 0; j<gameBoardModel.getYmax(); j++){
                GameBoardTile gameBoardTile = cellViewArray[i][j];
                gameBoardTile.refreshTile(gameBoardModel.getCellIsAlive(i, j));
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
        GameBoardCell GameBoardCell = gameBoardModel.takeNextCellChange();
        refreshButtonAtCoordinates(GameBoardCell);
    }    

    /**/
    protected void refreshButtonAtCoordinates(GameBoardCell gameBoardCell){        
        GameBoardTile gameBoardTile = cellViewArray[gameBoardCell.getX()][gameBoardCell.getY()];
        gameBoardTile.refreshTile(gameBoardCell.isAlive());
    }
    
    @FXML
    private void debug(){
        
        try {
            fileIO.writeBoardToFile(gameBoardModel);
            fileIO.readFile();
            //fileIO.readTestFile();
            /*
            long start = System.nanoTime();
            step();
            step();
            step();
            long stop = System.nanoTime();
            System.out.println("after 3 steps: "+((stop-start)/1000000.0));
            */
        } catch (IOException ex) {
            Logger.getLogger(GameBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
