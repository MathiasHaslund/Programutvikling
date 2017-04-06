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


public class gameBoardController implements Initializable{
    private static final PseudoClass LIVE_PSEUDO_CLASS =
    PseudoClass.getPseudoClass("live");
    
    /*References the button with the text "start/stop" in the gameBoard.fxml*/
    @FXML 
    private Button startButton;
    
    /*References the grid pane that contains the squares in the main bord in gameBoard.fxml*/
    @FXML
    private GridPane gameGrid;
    
    /**/
    @FXML
    private Slider speedSlider;
    
    /**/
    @FXML
    private Label roundCounterLabel;
    
    /**/
    private int roundCounter;
    
    /**/
    private boolean gameRunning = false;
    
    /**/
    MediaPlayer mediaPlayer;
    
    /**/
    private Button cellViewArray[][];
    
    /**/
    gameBoardModel gameBoardModel = new gameBoardModel();
            
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //playSound("sneaky.mp3");
        initSlider ();
        initBoard();
    }
      
    private void playSound(String soundName){
        String soundString = soundName;
        Media sound = new Media(new File(soundString).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
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
        cellViewArray = new Button[gameBoardModel.xmax][gameBoardModel.ymax];
        for (int i=0; i<gameBoardModel.xmax; i++)
        {
            for (int j=0; j<gameBoardModel.ymax; j++)
            {
                String buttonId = "cell_"+i+"_"+j;
                Button button = new Button();
                cellViewArray[i][j] = button;
                
                /*Does not use the game board cell object for improved performance*/
                refreshButton(button, gameBoardModel.getCellIsAlive(i, j));
                
                button.setMinSize(5, 5);
                button.setMaxSize(5, 5);
                button.setId(buttonId);
                button.setOnAction(new EventHandler<ActionEvent>(){
                
                    @Override
                    public void handle(ActionEvent event) {
                        writeCellClickToModel(buttonId);
                        refreshBoard();
                    };
                });                       
                gameGrid.add(button, i, j);
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
                            Thread.sleep(gameBoardModel.getCurrentTickTime());
                        }
                        catch (InterruptedException ex) {}
                }
            }
        }).start();
    }
    
    @FXML
    private void step (){
        //playSound("ffs.wav");
        gameBoardModel.gameLogic();
        roundCounter++;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        long start = System.nanoTime();
        while(true) {           
            gameBoardCell gameBoardCell = gameBoardModel.takeNextCellChange();
            if (gameBoardCell == null){
                break;
            }
            refreshButtonAtCoordinates(gameBoardCell);            
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
        for (int i = 0; i<gameBoardModel.xmax; i++){
            for (int j = 0; j<gameBoardModel.ymax; j++){
                Button button = cellViewArray[i][j];
                refreshButton(button, gameBoardModel.getCellIsAlive(i, j));
            }
        }
    }
    
    private void writeCellClickToModel(String buttonId){
        //playSound("hello2.mp3");
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
      
    /**/
    private void refreshButtonAtCoordinates(gameBoardCell gameBoardCell){        
        Button button = cellViewArray[gameBoardCell.getX()][gameBoardCell.getY()];
        refreshButton(button, gameBoardCell.isAlive());
    }
    
    /*refreshes the view of a single cell*/
    private void refreshButton(Button button, boolean cellState){
            if (cellState){
                button.pseudoClassStateChanged(LIVE_PSEUDO_CLASS, true);
            }
            else{
                button.pseudoClassStateChanged(LIVE_PSEUDO_CLASS, false);  
            }
    }
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
