

package gameoflife;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;


/**
* @author Mathias Haslund
* @author Josef Krivan
* @version 0.7
* @since 0.1 (5/3/2017)
*/

/**
 * The functions and inner workings of the GUI.
 * The center for most of the functions of the Java program such as the sounds, slider, button controlling and the board itself.
 * For more detailed explanations and code: 
 * @see Sound
 * @see GameSpeedControl
 * @see GameBoardModel
 */

public class GameBoardController implements Initializable{
    
    /**
    * References the button with the text "start/stop" in the gameBoard
    */
    @FXML
    private Button saveButton;
    
    /**
    * References the button with the text "Load" in the gameBoard
    */
    @FXML
    private Button loadButton;
    
    /**
    * References the button with the text "start/stop" in the gameBoard
    */
    @FXML 
    protected Button startButton;
    
     /**
    * References the grid pane that contains the squares in the main board in gameBoard
    */
    @FXML
    private GridPane gameGrid;
    
    /**
     * References the slider inside the GUI.
     */
    @FXML
    private Slider speedSlider;
    
    /**
     * Counts the ticks of the game board (GUI).
     */
    @FXML
    protected Label roundCounterLabel;
    
    @FXML
    /**
     * Class for the "X" value in the text-field.
     */
    private TextField inputX;
    
    @FXML
    /**
     * Class for the "Y" value in the text-field.
     */
    private TextField inputY;
    
    @FXML
    /**
     * Class for the "Resize" button on the GUI.
     */
    protected Button resize;

    /**
    * Class for the SaveChooser ChoiceBox in the GUI.
    * Used for selecting what save file the load button will load to the board.
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
     * @see GameBoardCell
     * @see GameBoardTile
     */    
    private GameBoardTile cellViewArray[][];
    
    /**
     * Creating the object to display the "Game Board".
     */
    GameBoardModel gameBoardModel = new GameBoardModel();
    
    /**
     * Creating the object to control the game speed.
     */
    GameSpeedControl gameSpeedControl = new GameSpeedControl();
    

    
    /**
     * Creating the new object that will play the sound file associated with the enum element "BACKGROUND".
     * @see Sound
     */
    Sound backgroundSound = new Sound(Sound.SoundTypes.BACKGROUND);
    
    /**
     * Creating an object to import/export boards and the cell states in the boards.
     * @see FileIO
     */
    FileIO fileIO = new FileIO();
            
    @Override
    /**
     * @since 0.6
     * Initialization method for Background Music, Board and Slider functions.
     * Calls the slider and board initializers.
     * @see playSound
     * @see initSlider
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        //sound.playSound(Sound.SoundTypes.BACKGROUND);
        initSlider ();
        initEmptyBoard();
        initSaveChooser();
    }
      

    @FXML
    /**
     * Method that allows the user to select any preset or saved game boards and cell states.
     */
    private void initSaveChooser(){
        saveChooser.setItems(FXCollections.observableArrayList("UserSave", "Gliders", "Boring", "Propeller","Flower"));
        saveChooser.getSelectionModel().selectFirst();
    }
    @FXML
     /**
     * Slider
     * @since 0.7 
     * The method that initiates the slider function.
     * The functions of the slider based on the game speed.
     * @see gameBoardModel (setGameSpeed)
     * @see GameSpeedControl
     */
    private void initSlider (){
        gameSpeedControl.setGameSpeed(speedSlider.valueProperty().doubleValue());        
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    gameSpeedControl.setGameSpeed(new_val.doubleValue());                    
            }
        });
    }
                
    @FXML
    /**
     * Method that sets up the game board & cells.
     * The main initializer that creates a board based on the properties set in the code.
     * @see GameBoardCell
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
    /**
     * Method that initializes an "empty" board.
     * An empty board contains exclusively dead cells.
     * The board starts the same way as like in 'initBoard'
     * @see initBoard
     */
    private void initEmptyBoard(){
        gameBoardModel.initCellStates();
        initBoard();
    }
    
    @FXML
     /**
     * Method that controls if the game is running or not.
     * If the game is running the stopGame method will execute.
     * If the game isnt running, the startGame method will execute.
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
    /**
     * Method that controls the actions when the game is commenced with the 'Start' button.
     */
    private void startGame(){
        if(gameRunning){
            return;
        }
        /**
         * Music plays when the button is pressed.
         * @see Sound
         */
        (new Sound(Sound.SoundTypes.START)).playSound();
        backgroundSound.playSound();
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
                            Thread.sleep(gameSpeedControl.getTickTime());
                        }
                        catch (InterruptedException ex) {}
                }
            }
        }).start();     
    }
    
    @FXML
    /**
     * The method that is executed when the game is commanded to stop running.
     */
    private void stopGame(){
        if(!gameRunning){
            return;
        }
        /**
         * A different sound effect is played when the game is stopped.
         * @see Sound
         */
        (new Sound(Sound.SoundTypes.STOP)).playSound();
        backgroundSound.pauseSound();
        gameRunning = false;
        startButton.setText("Start");
    }
    
    @FXML
    /**
     * Method for executing a step.
     * Tells the counter to add one to the integer value for each step.
     */
    private void step (){        
        gameBoardModel.gameLogic();
        roundCounter++;
        roundCounterLabel.setText(Integer.toString(roundCounter));
        while(true) {           
            GameBoardCell GameBoardCell = gameBoardModel.takeNextCellChange();
            if (GameBoardCell == null){
                break;
            }
            refreshButtonAtCoordinates(GameBoardCell);            
        }
    }

    @FXML
     /**
     * Method for executing the sound from the step button, and calling the step method.
     */
    private void singleStep(){
        (new Sound(Sound.SoundTypes.CLICK)).playSound();
        step();
    }
    
    @FXML
    /**
    * Method for clearing the board as well as the round counter.
    * The CLICK sound is played when the associated button is pressed
    * @see Sound
    */
    private void clearBoard(){
        (new Sound(Sound.SoundTypes.CLICK)).playSound();
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
    /**
     * Class... 
     * @param buttonId todo
     */
    private void writeCellClickToModel(String buttonId){
        int p1 = buttonId.indexOf("_",0);
        int p2 = buttonId.indexOf("_",p1+1);
        int x = Integer.parseInt(buttonId.substring(p1+1, p2));
        int y = Integer.parseInt(buttonId.substring(p2+1));
        gameBoardModel.toggleCellState(x, y);        
    }
    
    /**
     * Method that updates the view of the whole board from list.
     */
    private void refreshBoard(){
        GameBoardCell GameBoardCell = gameBoardModel.takeNextCellChange();
        refreshButtonAtCoordinates(GameBoardCell);
    }    

    /**
     * Method that gets the X and Y coordinates from the game board.
     * Refreshes a certain button based on its status (checks if its alive).
     * @see GameBoardModel
     * @param gameBoardCell todo
     */
    protected void refreshButtonAtCoordinates(GameBoardCell gameBoardCell){        
        GameBoardTile gameBoardTile = cellViewArray[gameBoardCell.getX()][gameBoardCell.getY()];
        gameBoardTile.refreshTile(gameBoardCell.isAlive());
    }
    
    @FXML
    /**
     * Method that resizes the board based on integer values typed into TextField (X and Y).
     */
    private void setBoardSize(){
        (new Sound(Sound.SoundTypes.CLICK)).playSound();
        gameBoardModel.setXmax(Integer.parseInt(inputX.getText()));
        gameBoardModel.setYmax(Integer.parseInt(inputY.getText()));
        gameGrid.getChildren().get(0);
        gameGrid.getChildren().clear();
        initEmptyBoard();
    }
    /**
     * Loading a saved .dat file.
     * The exception handling might not be necessary as the user should not manipulate the savegame folder.
     * @see FileIO
     * @see stopGame
     */
    @FXML
    private void loadGameFromFile(){
        stopGame();
        FileIO fileIO = new FileIO();
        String savename = saveChooser.getValue().toString()+".dat";
        try{
            boolean[][] cellIsAliveArrayFromFile = fileIO.readBoardFromFile(gameBoardModel, savename);
            int xmax = cellIsAliveArrayFromFile.length;
            int ymax = cellIsAliveArrayFromFile[0].length;
            (new Sound(Sound.SoundTypes.CLICK)).playSound();
            roundCounter = 0;
            roundCounterLabel.setText(Integer.toString(roundCounter));
            gameBoardModel.setXmax(xmax);
            gameBoardModel.setYmax(ymax);
            gameBoardModel.initCellStatesFromArray(cellIsAliveArrayFromFile);
            gameGrid.getChildren().get(0);
            gameGrid.getChildren().clear();
            initBoard();
        }
        /**
         * An alert that triggers if a certain file being loaded doesnt exist or cant be found.
         */
        catch(IOException ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Missing file");
            alert.setHeaderText("The file you tried to load could not be found");
            alert.setContentText("could not find the file "+savename+" in the savegame folder");
            alert.showAndWait();

        }
    }
    
    /**
     * Saving the current board as a .dat file.
     * the exception handling should only trigger if the user chooses to rename or delete the savegame folder.
     * @see stopGame
     */
    @FXML
    private void saveGameToFile(){
        try{
            stopGame();
            (new Sound(Sound.SoundTypes.CLICK)).playSound();
            FileIO fileIO = new FileIO();
            fileIO.writeBoardToFile(gameBoardModel);
        }
        /**
         * Alerts and text incase something goes wrong when saving the file.
         */
        catch(IOException ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Unable to save file");
            alert.setHeaderText("The file you tried to save could not be saved");
            alert.setContentText("could not write the file UserSave.dat to the savegame folder");
            alert.showAndWait();
        }
    }
}