package gameoflife;

import javafx.css.PseudoClass;
import javafx.scene.control.Button;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */
 
/**
 * Represents the visual characteristics of a tile on the game board
 */
public class GameBoardTile {
   
   private static final PseudoClass LIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("live");
   
   private Button tile;
   
   /**
    * Sets the tile's ID and size
    * @param tileId unique id for referring to the tile object
    * @param tileSize the size of the visual representation of the tile object
    */
   public GameBoardTile(String tileId, int tileSize){
       tile = new Button();
       tile.setId(tileId);
       setTileSize(tileSize);
    }
   
    /**
     * Changes the tile's pseudo class based on it's life state,
     * so that we can change it's visual look.
     * @param cellState boolean deciding whether the cell alive or dead. 
     */
    protected void refreshTile(boolean cellState){
            if (cellState){
                tile.pseudoClassStateChanged(LIVE_PSEUDO_CLASS, true);
            }
            else{
                tile.pseudoClassStateChanged(LIVE_PSEUDO_CLASS, false);  
            }
    }
    /**
     * Sets the size of the tile.
     * @param size int deciding the width and height of the tile in pixels
     */
    private void setTileSize(int size){
        tile.setMinSize(size, size);
        tile.setMaxSize(size, size);
    }
    /**
     * @return The tile object.
     */
    protected Button getTile(){
        return tile;
    }
    
}
