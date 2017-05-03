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
 * PseudoClass is used for the Pseudo properties of the buttons.
 * PseudoClass has different properties compared to the standard class. 
 */
public class GameBoardTile {
   
   private static final PseudoClass LIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("live");
   
   /**
    * Class for creating a new tile object.
    */
   private Button tile;
   /**
    * Class for the pseudo buttons associated with the Class.
    * @see GameBoardTile
    * @param tileId todo
    * @param tileSize  todo
    */
   public GameBoardTile(String tileId, int tileSize){
       tile = new Button();
       tile.setId(tileId);
       setTileSize(tileSize);
    }
   
    /**
     * Refreshes the view of a single cell based on the condition of the cell.
     * @param cellState todo
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
     * Allows the size-adjustment of the tiles (X and Y properties).
     * @param size todo
     */
    private void setTileSize(int size){
        tile.setMinSize(size, size);
        tile.setMaxSize(size, size);
    }
    /**
     * Method for the visual display of a tile.
     * @return A viewable tile.
     */
    protected Button getTile(){
        return tile;
    }
    
}
