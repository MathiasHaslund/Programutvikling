

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
 * The GameBoardController sends commands to update the data in the model (GameBoardmodel.java) and to update objects in view (GameBoard.fxml)
 * It accepts actions through user input from the interactive objects in the view, and uses the user interaction to update and change data and states in the model.
 * When the data and states in the model changes, the GameBoardController updates the view to reflect the changes.
 */

public class GameBoardController implements Initializable{
    
    /**
    * References the button with the text "start/stop" in the gameBoard.
    */
    @FXML
    private Button saveButton;
    
    /**
    * References the button with the text "Load" in the gameBoard.
    */
    @FXML
    private Button loadButton;
    
    /**
    * References the button with the text "start/stop" in the gameBoard.
    */
    @FXML 
    private Button startButton;
    
     /**
    * References the grid pane that contains the squares in the main board.
    */
    @FXML
    private GridPane gameGrid;
    
    /**
     * References the slider used to change the game speed.
     */
    @FXML
    private Slider speedSlider;
    
    /**
     * Counts the ticks of the game board.
     */
    @FXML
    private Label roundCounterLabel;
    
    @FXML
    /**
     * References the TextField for setting the length og the x-axis.
     */
    private TextField inputX;
    
    @FXML
    /**
     * References the TextField for setting the length og the y-axis.
     */
    private TextField inputY;
    
    @FXML
    /**
     * References the Resize button used to set custom board sizes set by inputX and inputY.
     */
    private Button resize;

    /**
    * References the CoiceBox used for selecting saved games to load.
    */
    @FXML
    private ChoiceBox saveChooser;
    

     /**
     * Integer value for counting each step.
     */
    private int roundCounter;
    
    /**
     * Flag for determining wether the game is running or not.
     */    
    protected boolean gameRunning = false;
    
    /**
     * Array for containing the visual tiles that build the game board.
     */    
    private GameBoardTile cellViewArray[][];
    
    /**
     * Creating an instance of the GameBoardModel.
     */
    private GameBoardModel gameBoardModel = new GameBoardModel();
    
    /**
     * Creating an instance of the GameSpeedControl.
     */
    private GameSpeedControl gameSpeedControl = new GameSpeedControl();
    

    
    /**
     * Creating an instance of the Sound object that will play and pause the audio file associated with the enum element "BACKGROUND".
     */
    Sound backgroundSound = new Sound(Sound.SoundTypes.BACKGROUND);
    
    /**
     * Creating an instance of the object to import/export boards and the cell states in the boards.
     */
    FileIO fileIO = new FileIO();
            
    /**
     * Initialization method for the Board, Slider functions and the save game file chooser.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initSlider ();
        initEmptyBoard();
        initSaveChooser();
    }
      

    @FXML
    /**
     * Populates the ChoiceBox with options referencing existing save file names.
     */
    private void initSaveChooser(){
        saveChooser.setItems(FXCollections.observableArrayList("UserSave", "Gliders", "Boring", "Propeller","Flower"));
        saveChooser.getSelectionModel().selectFirst();
    }
    @FXML
     /**
     * Adds a listener to the slider that reports the slider value to gamespeedControl.setGameSpeed()
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
     * Sets up the game board & cells based on the board size and cell states set in the model.
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
     * Initializes an empty board.
     * it first calls gameBoardModel.initCellStates() to set all cell states in the model to dead,
     * then it updates the view with an empty game board using initBoard()
     */
    private void initEmptyBoard(){
        gameBoardModel.initCellStates();
        initBoard();
    }
    
    @FXML
     /**
     * Checks the gameRunning flag to determine if it's going to pause the game, or resume it.
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
     * Starts a thread that runs an iteration according to the game rules.
     * It then pauses for a length of time set by the user, and repeats untill the game is paused by another process setting the gameRunning flag to false.
     * Background audio is started, and the text on the start/stop button is changed to "stop" to reflect what the next action of the button will be.
     */
    private void startGame(){
        if(gameRunning){
            return;
        }
        (new Sound(Sound.SoundTypes.START)).playSound();
        backgroundSound.playSound();
        gameRunning = true;
        startButton.setText("Stop");
                
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (gameRunning == true) {
                    /**
                     * Using runLater ensures the View is updated continiously.
                     */
                    Platform.runLater(new Runnable() {
                    @Override
                        public void run() {
                            step();                            
                        }
                        });
                        try {
                            Thread.sleep(gameSpeedControl.getTickTime());
                        }
                        catch (InterruptedException ex) {
                        }
                }
            }
        }).start();     
    }
    
    @FXML
    /**
     * Sets the gameRunning flag to false to stop the game from playing.
     * The change in the game running flag is observed by the process iterating the changes to the board, and makes it stop.
     * Pauses the background audio and the text on the start/stop button is changed to "start" to reflect what the next action of the button will be.
     */
    private void stopGame(){
        if(!gameRunning){
            return;
        }
        (new Sound(Sound.SoundTypes.STOP)).playSound();
        backgroundSound.pauseSound();
        gameRunning = false;
        startButton.setText("Start");
    }
    
    @FXML
    /**
     * Method for executing a step.
     * Calls the model to run gameLogic, advancing the state of the model with one iteration.
     * Tells the counter to add one to the integer value for each step.
     * Adds +1 to the Round counter label in the view.
     * Runs through the changes in the model one cell at a time, given by gameBoardModel.takeNextCellChange(), and updates the view of the board.
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
            refreshButtonFromGameBoardCell(GameBoardCell);            
        }
    }

    @FXML
     /**
     * Executes a single iteration of the game board.
     */
    private void singleStep(){
        (new Sound(Sound.SoundTypes.CLICK)).playSound();
        step();
    }
    
    @FXML
    /**
    * Used for wiping the board and reset the round counter.
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
     * Resets the view of the board according to the model.
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
     * Changes the state of a cell in the model when it's clicked in the view.
     * @param buttonId is used to determine the cell's x and y coordinates in cellIsAliveArray in the model.
     */
    private void writeCellClickToModel(String buttonId){
        int p1 = buttonId.indexOf("_",0);
        int p2 = buttonId.indexOf("_",p1+1);
        int x = Integer.parseInt(buttonId.substring(p1+1, p2));
        int y = Integer.parseInt(buttonId.substring(p2+1));
        gameBoardModel.toggleCellState(x, y);        
    }
    
    /**
     * Updates the view with a single cell change.
     * Should only be called when there's exactly one cell change to handle.
     */
    private void refreshBoard(){
        GameBoardCell gameBoardCell = gameBoardModel.takeNextCellChange();
        refreshButtonFromGameBoardCell(gameBoardCell);
    }    

    /**
     * Sets the view state of a game board tile based on the corresponding status of the game board cell in the model.
     * @param gameBoardCell the game board cell object that contains x and y coordinates used for updating the live/dead status the view of the tile
     */
    protected void refreshButtonFromGameBoardCell(GameBoardCell gameBoardCell){        
        GameBoardTile gameBoardTile = cellViewArray[gameBoardCell.getX()][gameBoardCell.getY()];
        gameBoardTile.refreshTile(gameBoardCell.isAlive());
    }
    
    @FXML
    /**
     * Method that resizes the board based on values typed into TextField (X and Y).
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
    
    @FXML
    /**
     * Saving the current board as a .dat file.
     * the exception handling should only trigger if the user chooses to rename or delete the savegame folder.
     */
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