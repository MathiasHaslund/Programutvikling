package gameoflife;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.7
 * @since 0.1 (5/3/2017)
 */

/**
 * The "Model" file that displays all the GUI elements.
 */
public class GameBoardModel { 
    /**
     * Tick time in miliseconds.
     */
    private int minTickTime = 100;
    private int maxTickTime = 1000;
    private int currentTickTime;
      /**
     * Function for setting the speed with the slider.
     * @see initSlider
     * @param gameSpeed 
     */
    protected void setGameSpeed(double gameSpeed){
        currentTickTime= (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    }
    
    private ArrayList<GameBoardCell> cellChangeList = new ArrayList<GameBoardCell>();
    private boolean cellIsAliveArray[][];
    private int xmax = 10;
    private int ymax = 15;
    
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
     * Returns the tick time in miliseconds.
     */
    protected int getCurrentTickTime(){
        return currentTickTime;
    }
    /**
     * Checks for the cell states adjacent to the current cell. 
     */
    protected void initCellStates (){
        cellIsAliveArray = new boolean[xmax][ymax];
        initCellStatesFromArray(cellIsAliveArray);
    }
    /**
     * 
     * @param cellIsAliveArray 
     */
    protected void initCellStatesFromArray(boolean[][] cellIsAliveArray){
        for (int i=0; i<xmax; i++){
            for (int j=0; j<ymax; j++){
                cellIsAliveArray[i][j] = false;
            }
        }
    }
    /**
     * Changes the state of the cell.
     * @param x
     * @param y 
     */
    protected void toggleCellState (int x, int y){
        cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        addToCellChangeList(x, y);
    }
     /**
     * @param x
     * @param y
     * @return The cell to an alive or dead state. 
     */
    protected boolean getCellIsAlive(int x, int y){
        return cellIsAliveArray [x][y];
    }
     /**
     * Adds/Checks a new cell and its state.
     * @param x
     * @param y 
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
     * @param x
     * @param y
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
     * @param x
     * @param y
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
        long start = System.nanoTime();
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
        long afterCount = System.nanoTime();
        for (int i = 0; i<cellChangeList.size(); i++){
            GameBoardCell gameBoardCell = cellChangeList.get(i);
            int x = gameBoardCell.getX();
            int y = gameBoardCell.getY();
            cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        }
        long afterCellChangeList = System.nanoTime();
        System.out.println("after count: "+((afterCount-start)/1000000.0));
        System.out.println("after cell change list: "+((afterCellChangeList-start)/1000000.0));
    }
}
