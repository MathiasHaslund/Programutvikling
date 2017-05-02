package gameoflife;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.7
 * @since 0.1 (5/3/2017)
 * The methods to display the grid.
 */

/**
 * The "Grid" of click-able buttons on the visual program.
 */

public class GameBoardCell {
    private final int x;
    private final int y;
    private final boolean[][] cellIsAliveArray;
    
    public GameBoardCell(int x, int y, boolean[][] cellIsAliveArray){
        this.x=x;
        this.y=y;
        this.cellIsAliveArray = cellIsAliveArray;
        
    }
    
/**
 * @return Gets the horizontal value for the grid.
 */
    
    public int getX(){
        return x;
    }
/**
 * @return Gets the vertical value for the grid.
 */      
    public int getY(){
        return y;
    }
/**
 * @return Gets the state of the cell (alive or dead).
 */    
    public boolean isAlive(){
        return cellIsAliveArray[x][y];
    }
    
    
}
