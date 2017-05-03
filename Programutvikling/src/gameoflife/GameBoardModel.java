package gameoflife;

import java.util.ArrayList;


/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.7
 * @since 0.1 (5/3/2017)
 */

/**
 * The "Model" file that displays all the GUI elements.
 * The game board is created using X and Y dimensions.
 */
public class GameBoardModel { 
    
    private ArrayList<GameBoardCell> cellChangeList = new ArrayList<GameBoardCell>();
    private boolean cellIsAliveArray[][];
    private int xmax = 50;
    private int ymax = 30;
    
    protected int getXmax(){
        return xmax;
    }
    
    protected int getYmax(){
        return ymax;
    }
    protected void setXmax(int xmax){
        this.xmax= xmax;
    }
    
    protected void setYmax(int ymax){
        this.ymax = ymax;
    }
    
    /**
     * Checks for the cell states adjacent to the current cell.
     */
    protected void initCellStates (){
        /**
         * Relies on the fact that boolean arrays are initialized with false as the default value
         */
        this.cellIsAliveArray = new boolean[xmax][ymax];
    }
    /**
     * Initializes the cell states from the current array.
     * @param cellIsAliveArray todo
     */
    protected void initCellStatesFromArray(boolean[][] cellIsAliveArray){
        this.cellIsAliveArray = new boolean[xmax][ymax];
        for (int i=0; i<xmax; i++){
            for (int j=0; j<ymax; j++){
                this.cellIsAliveArray[i][j] = cellIsAliveArray[i][j];
            }
        }
    }
    /**
     * Changes the state of the cell.
     * @param x todo
     * @param y todo
     */
    protected void toggleCellState (int x, int y){
        cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        addToCellChangeList(x, y);
    }
     /**
     * @param x todo
     * @param y todo
     * @return The cell to an alive or dead state. 
     */
    protected boolean getCellIsAlive(int x, int y){
        return cellIsAliveArray [x][y];
    }
     /**
     * Adds/Checks a new cell and its state.
     * @param x todo
     * @param y  todo
     */
    protected void addToCellChangeList (int x, int y){        
        cellChangeList.add(new GameBoardCell(x, y, cellIsAliveArray));
    }
    /**
     * @return Nothing if Length is Zero. 
     */
    protected GameBoardCell takeNextCellChange(){
        int listLength = cellChangeList.size();
        if (listLength==0){
            return null;
        }
        GameBoardCell gameBoardCell = cellChangeList.get(0);
        cellChangeList.remove(0);
        return gameBoardCell;
    }
      /**
     * @param x todo
     * @param y todo
     * @return True if cell is inside the game board, false if not.
     */
    private boolean isOutsideBoard(int x, int y){
        if (x<0 || x>=xmax || y<0 || y>=ymax){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Checks if current cell is a cell on the board.
     * @param x todo
     * @param y todo
     * @return A value based on the number of living cells around one specific cell.
     */
    private int countLiveNeighbours(int x, int y){
        int counter = 0;
		for (int i =-1; i<=1; i++){
                    for (int j = -1; j<=1; j++){
                        int xn = x+i;
                        int yn = y+j;
                        /*checks if current cell is a cell on the board and is a neighbour of the cell we are checking, and not the cell itself*/
                        if ((xn == x && yn == y) || isOutsideBoard(xn, yn)){
                            continue;
                        }
                        if (getCellIsAlive(xn,yn) == true){
                            counter ++;
                        }
                    }
                    
                }               
                return counter;
    }
    /**
     * The process of the game (according to the game of life rules/logic).
     * It counts the cells based on boolean values for alive and dead cells around.
     * Determines whether certain cells will be dead or alive.
     * Repeats the calculations for every cell on the game board.
     */
    protected void gameLogic(){
        for (int i = 0; i<xmax; i++){
            for (int j = 0; j<ymax; j++){
		int counter = countLiveNeighbours(i, j);
		if (getCellIsAlive(i, j) == true){
                    if (counter<2 || counter>3){
                        addToCellChangeList(i, j);
                    }
		}
                else {
                    if (counter == 3){
			addToCellChangeList(i, j);
                    }
		}					
            }
        }
        for (int i = 0; i<cellChangeList.size(); i++){
            GameBoardCell gameBoardCell = cellChangeList.get(i);
            int x = gameBoardCell.getX();
            int y = gameBoardCell.getY();
            cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        }
    }
}
