package gameoflife;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.7
 * @since 0.1 (5/3/2017)
 */

/**
 * Represents the non visual data for a cell. 
 */

public class GameBoardCell {
    private final int x;
    private final int y;
    private final boolean[][] cellIsAliveArray;
    
    /**
     * Sets the cell's x and y position and whether it's alive or dead.
     * @param x is the cell's position on the x-axis
     * @param y is the cell's position on the y-axis
     * @param cellIsAliveArray is whether or not the cell is dead.
     */
    public GameBoardCell(int x, int y, boolean[][] cellIsAliveArray){
        this.x=x;
        this.y=y;
        this.cellIsAliveArray = cellIsAliveArray;
        
    }
    
    /**
    * @return Gets the cell's position on the boards x-axis.
    */    
    public int getX(){
        return x;
    }
    
/**
 * @return Gets the cell's position on the boards y-axis.
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
