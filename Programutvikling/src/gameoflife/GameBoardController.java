/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
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
    /*private static final PseudoClass LIVE_PSEUDO_CLASS =
    PseudoClass.getPseudoClass("live");
    */
    
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
    
   
    
    /*
    private Button cellViewArray[][];
    */
    private GameBoardTile cellViewArray[][];
    
    /**/
    GameBoardModel GameBoardModel = new GameBoardModel();
    
    
    Sound Sound = new Sound();
            
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
        Sound.playSound("BACKGROUND");
        initSlider ();
        initBoard();
    }
      

    
    @FXML
    private void initSlider (){
        GameBoardModel.setGameSpeed(speedSlider.valueProperty().doubleValue());        
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    System.out.println(new_val.doubleValue());
                    GameBoardModel.setGameSpeed(new_val.doubleValue());                    
            }
        });
    }
                
    @FXML
    private void initBoard() {
        GameBoardModel.initCellStates();
        cellViewArray = new GameBoardTile[GameBoardModel.xmax][GameBoardModel.ymax];
        for (int i=0; i<GameBoardModel.xmax; i++)
        {
            for (int j=0; j<GameBoardModel.ymax; j++)
            {
                String tileId = "cell_"+i+"_"+j;
                int tileSize = 15;
                GameBoardTile gameBoardTile = new GameBoardTile(tileId, tileSize);
                cellViewArray[i][j] = gameBoardTile;
                
                /*Does not use the game board cell object for improved performance*/
                gameBoardTile.refreshTile(GameBoardModel.getCellIsAlive(i, j));
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
        
        
        //playSound("ffs.wav");
        if(gameRunning == true){
            gameRunning = false;
            startButton.setText("Start");
            //playSound("what.wav");
            //playSound("failed.mp3");
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
                            Thread.sleep(GameBoardModel.getCurrentTickTime());
                        }
                        catch (InterruptedException ex) {}
                }
            }
        }).start();        
    }
    
    @FXML
    private void step (){
        //playSound("ffs.wav");
        GameBoardModel.gameLogic();
        roundCounter++;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        long start = System.nanoTime();
        while(true) {           
            GameBoardCell GameBoardCell = GameBoardModel.takeNextCellChange();
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
        GameBoardModel.initCellStates();
        rePaintBoard();        
    }
        
    private void rePaintBoard(){
        for (int i = 0; i<GameBoardModel.xmax; i++){
            for (int j = 0; j<GameBoardModel.ymax; j++){
                GameBoardTile gameBoardTile = cellViewArray[i][j];
                gameBoardTile.refreshTile(GameBoardModel.getCellIsAlive(i, j));
            }
        }
    }
    
    private void writeCellClickToModel(String buttonId){
        //playSound("hello2.mp3");
        int p1 = buttonId.indexOf("_",0);
        int p2 = buttonId.indexOf("_",p1+1);
        int x = Integer.parseInt(buttonId.substring(p1+1, p2));
        int y = Integer.parseInt(buttonId.substring(p2+1));
        GameBoardModel.toggleCellState(x, y);        
    }
    
    /*Updates the view of the whole board from list*/
    private void refreshBoard(){
        GameBoardCell GameBoardCell = GameBoardModel.takeNextCellChange();
        refreshButtonAtCoordinates(GameBoardCell);
    }    

    /**/
    protected void refreshButtonAtCoordinates(GameBoardCell gameBoardCell){        
        GameBoardTile gameBoardTile = cellViewArray[gameBoardCell.getX()][gameBoardCell.getY()];
        gameBoardTile.refreshTile(gameBoardCell.isAlive());
    }
    
    /*refreshes the view of a single cell*/
    /*
    private void refreshButton(Button button, boolean cellState){
            if (cellState){
                button.pseudoClassStateChanged(LIVE_PSEUDO_CLASS, true);
            }
            else{
                button.pseudoClassStateChanged(LIVE_PSEUDO_CLASS, false);  
            }
    }
*/
    @FXML
    private void debug(){
        long start = System.nanoTime();
        step();
        step();
        step();
        long stop = System.nanoTime();
        System.out.println("after 3 steps: "+((stop-start)/1000000.0));
    }    
}
