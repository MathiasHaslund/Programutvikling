package gameoflife;

import java.util.ArrayList;


/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.7
 * @since 0.1 (5/3/2017)
 */

/**
 * The model contains the data for the state of the cells, size of the model, and the game logic itself.
 * 
 */
public class GameBoardModel { 
    
    /**
     * A list ontaining the coordinates of cells that has changed status from alive to dead and vice versa.
     */
    private ArrayList<GameBoardCell> cellChangeList = new ArrayList<GameBoardCell>();
    /**
     * The array containing the information of whether each cell is alive or dead.
     */
    private boolean cellIsAliveArray[][];
    /**
     * The length of the models x-axis.
     */
    private int xmax = 50;
    /**
     * The length of the models y-axis.
     */
    private int ymax = 30;
    
    /**
     * Returns the length of the board's x-axis.
     * @return int containing the length of the game boards x-axis
     */
    protected int getXmax(){
        return xmax;
    }
    
    /**
     * Returns the length of the board's y-axis
     * @return int containing the length of the game boards y-axis
     */
    protected int getYmax(){
        return ymax;
    }
    
    /**
     * Sets the length of the board's x-axis.
     * @param xmax this contains the new length of the x-axis.
     */
    protected void setXmax(int xmax){
        this.xmax= xmax;
    }
    
    /**
     * Sets the length of the board's y-axis.
     * @param ymax this contains the new length of the y-axis.
     */
    protected void setYmax(int ymax){
        this.ymax = ymax;
    }
    
    /**
     * Populates the cellIsAliveArray with dead cells, using xmax and ymax to set the dimentions of the array.
     */
    protected void initCellStates (){
        /**
         * Relies on the fact that boolean arrays are initialized with false as the default value
         */
        this.cellIsAliveArray = new boolean[xmax][ymax];
    }
    
    /**
     * Populates the cellIsAliveArray with cells whose status comes from the input parameter, using xmax and ymax to set the dimentions of the array.
     * @param cellIsAliveArray contains the cell statuses to populate the array with.
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
     * Changes the cells life state to the opposite.
     * @param x The cells x-axis coordinate in the cellIsAliveArray.
     * @param y The cells y-axis coordinate in the cellIsAliveArray.
     */
    protected void toggleCellState (int x, int y){
        cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        addToCellChangeList(x, y);
    }
    
     /**
     * Checks if the cell is alive
     * Is an alternative to the isAlive() method in gameBoardCell.java.
     * It's used to increase performance by not creating gameBoardCell objects for every single position in the array when the board is drawn or re-drawn,
     * @param x The cells x-axis coordinate in the cellIsAliveArray.
     * @param y The cells y-axis coordinate in the cellIsAliveArray.
     * @return A boolean value, false=dead, true=alive.
     */
    protected boolean getCellIsAlive(int x, int y){
        return cellIsAliveArray [x][y];
    }
    
     /**
     * Adds a cell to cellChangeList.
     * This is used to store information on which cells have changed their life/death state.
     * @param x The cells x-axis coordinate in the cellIsAliveArray.
     * @param y The cells y-axis coordinate in the cellIsAliveArray.
     */
    private void addToCellChangeList (int x, int y){        
        cellChangeList.add(new GameBoardCell(x, y, cellIsAliveArray));
    }
    
    /**
     * Saves the first object in cellChange list to a local variable, deletes it from the list, and returns the local variable.
     * @return The first GameBoardCell element in the cellChangeList. Returns null if there are no objects in the cell change list.
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
     * Checks whether or not the given coordinates are outside the bounds of the model.
     * @param x the x-axis coordinates to check.
     * @param y the y-axis coordinates to check.
     * @return True if coordinates are outside the model, returns false if they are inside.
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
     * Counts the amount of live cells among the 8 closest neighbours according to the rules of GoL.
     * @param x the center cell's position on the x-axis.
     * @param y the center cell's position on the y-axis.
     * @return An integer number equal to the number of live neighbours.
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
     * Determines whether certain cells will be dead or alive in the next iteration.
     * Repeats the calculations for every cell position in the model.
     * @see <a href="https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules">The rules for Game of Life</a>
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
