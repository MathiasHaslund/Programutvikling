/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*newest file?*/
package gameoflife;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import static javafx.scene.input.KeyCode.X;
import javafx.scene.layout.GridPane;
import javafx.scene.media.*;


/**
* @author Mathias Haslund
* @author Josef Krivan
* @version 0.7
* @since 0.1 (5/3/2017)
*/

/**
 * The functions and inner workings of the GUI
 */

public class GameBoardController implements Initializable{
    
    
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    /**
    * @see gameBoard.fxml
    * References the button with the text "start/stop" in the gameBoard.fxml
    */
    @FXML 
    protected Button startButton;
     /**
    * @see gameBoard.fxml
    * References the grid pane that contains the squares in the main board in gameBoard.fxml 
    */
    @FXML
    private GridPane gameGrid;
    /**
     * @see gameBoard.fxml
     * References the slider inside the GUI
     */
    @FXML
    private Slider speedSlider;
    /**
     * @see gameBoard.fxml
     * Counts the ticks of the game board (GUI).
     */
    @FXML
    protected Label roundCounterLabel;
    /**
     * @see step
     * Counts each tick of the game board
     */
    
    @FXML
    /**
     * Class for the "X" value in the text-field.
     * @see GameBoard.fxml
     */
    private TextField X;
    @FXML
    /**
     * Class for the "Y" value in the text-field.
     * @see GameBoard.fxml
     */
    private TextField Y;
    @FXML
    /**
     * Class for the "Resize" button on the GUI.
     * @see GameBoard.fxml
     */
    protected Button resize;

    /**
    * 
    */
    @FXML
    protected ChoiceBox saveChooser;
    

     /**
     * Integer value for counting each step.
     */
    private int roundCounter;
    /**
     * Default boolean value when the program starts.
     */    
    protected boolean gameRunning = false;
    /**
     * @see GameBoardCell.java
     * @see GameBoardTile.java
     */    
    private GameBoardTile cellViewArray[][];
    
    /**
     * Creating the object to display the "Game Board".
     */
    GameBoardModel gameBoardModel = new GameBoardModel();
    /**
     * Creating the "sound" object for playing sounds.
     * @see Sound.java
     */
    Sound sound = new Sound();
    /**
     * Creating an object to import/export boards.
     * @see FileIO.java
     */
    FileIO fileIO = new FileIO();
            
    @Override
    /**
     * @since 0.6
     * Initialization for Background Music and Slider function.
     * @see playSound
     * @see initSlider
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        sound.playSound(Sound.SoundTypes.BACKGROUND);
        initSlider ();
        initEmptyBoard();
    }
      

    
    @FXML
    /**
     * Slider
     * @since 0.7
     * The functions of the slider based on the game speed.
     * @see gameBoardModel.java (setGameSpeed)
     */
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
    /**
     * Class that sets up the game board & cells.
     * @see GameBoardCell.java
     */
    private void initBoard() {
        cellViewArray = new GameBoardTile[gameBoardModel.getXmax()][gameBoardModel.getYmax()];
        for (int i=0; i<gameBoardModel.getXmax(); i++)
        {
            for (int j=0; j<gameBoardModel.getYmax(); j++)
            {
                String tileId = "cell_"+i+"_"+j;
                int tileSize = 15;
                GameBoardTile gameBoardTile = new GameBoardTile(tileId, tileSize);
                cellViewArray[i][j] = gameBoardTile;          
                /**
                 * Does not use the "GameBoardCell" object for improved performance
                 */
                gameBoardTile.refreshTile(gameBoardModel.getCellIsAlive(i, j));
                gameBoardTile.getTile().setOnAction(new EventHandler<ActionEvent>(){
                
                    @Override
                    /**
                     * Method for refreshing the board.
                     */
                    public void handle(ActionEvent event) {
                        writeCellClickToModel(tileId);
                        refreshBoard();
                    };
                });                       
                gameGrid.add(gameBoardTile.getTile(), i, j);
            }
        }
    }
    
    private void initEmptyBoard(){
        gameBoardModel.initCellStates();
        initBoard();
    }
    
    @FXML
     /**
     * Class that calls methods for starting and stopping the game.
     */
    private void startStopGame(){
        
        if(gameRunning){
            stopGame();
        }
        else{        
            startGame();
        }        
    }
    
    @FXML
    private void startGame(){
        if(gameRunning){
            return;
        }
        gameRunning = true;
        startButton.setText("Stop");
                
        new Thread(new Runnable() {
            @Override
            /**
             * Runs the board with steps.
             */
            public void run() {
                while (gameRunning == true) {
                    Platform.runLater(new Runnable() {
                    @Override
                    /**
                     * Method that repeatedly runs "step".
                     */
                        public void run() {
                            step();                            
                        }
                        });
                        try {
                            /**
                             * Waits for 1 second.
                             */
                            Thread.sleep(gameBoardModel.getCurrentTickTime());
                        }
                        catch (InterruptedException ex) {}
                }
            }
        }).start();     
    }
    
    @FXML
    private void stopGame(){
        if(!gameRunning){
            return;
        }
        gameRunning = false;
        startButton.setText("Start");
    }
    
    @FXML
    /**
     * Class for executing a step.
     * Tells the counter to add one to the integer value for each step.
     */
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
    /**
    Method for clearing the board as well as the round counter.
    */
    private void clearBoard(){
        roundCounter = 0;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        /**
         * Tells the method to stop the game if its running.
         */
        if (gameRunning == true){
            startStopGame();            
        }
        /**
         * Board is reset so all the tiles become "dead".
         */
        gameBoardModel.initCellStates();
        rePaintBoard();        
    }
    /**
     * Method for resetting the board to it's default state (When the program first runs).
     */    
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
    
    /**
     * Updates the view of the whole board from list.
     */
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
    /**
     * Method that resizes the board based on integer values typed into TextField (X and Y).
     * @see GameBoard.fxml
     */
    private void setBoardSize(){
        String xString = X.getText();
        String yString = Y.getText();
        int xInt = Integer.parseInt(xString);
        int yInt = Integer.parseInt(yString);
        //int x = Integer.parseInt(xString);
        //System.out.println(xInt);
        gameBoardModel.setXmax(xInt);
        gameBoardModel.setYmax(yInt);
        gameGrid.getChildren().get(0);
        gameGrid.getChildren().clear();
        initEmptyBoard();
    }
    
    @FXML
    private void loadGameFromFile() throws IOException{
        FileIO fileIO = new FileIO();
        boolean[][] cellIsAliveArray = fileIO.readBoardFromFile(gameBoardModel);
        int xmax = cellIsAliveArray.length;
        int ymax = cellIsAliveArray[0].length;
        stopGame();
        roundCounter = 0;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        gameBoardModel.setXmax(xmax);
        gameBoardModel.setYmax(ymax);
        gameBoardModel.initCellStatesFromArray(cellIsAliveArray);
        gameGrid.getChildren().get(0);
        gameGrid.getChildren().clear();
        initBoard();
    }
    
    @FXML
    private void saveGameToFile() throws IOException{
        stopGame();
        FileIO fileIO = new FileIO();
        fileIO.writeBoardToFile(gameBoardModel);
    }
    
    @FXML
    private void debug(){
        //try {
           // fileIO.writeBoardToFile(gameBoardModel);
          //  fileIO.readFile();
            //fileIO.readTestFile();
            /*
            long start = System.nanoTime();
            step();
            step();
            step();
            long stop = System.nanoTime();
            System.out.println("after 3 steps: "+((stop-start)/1000000.0));
            */
        //} catch (IOException ex) {
          //  Logger.getLogger(GameBoardController.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
}    

