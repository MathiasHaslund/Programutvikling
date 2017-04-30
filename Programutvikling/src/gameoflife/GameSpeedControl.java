package gameoflife;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */

/**
 * Class for setting the maximum, minimum and current tick time.
 */
public class GameSpeedControl {
    /**
     * Tick time in milliseconds.
     */
    private int minTickTime = 100;
    private int maxTickTime = 1000;
    private int currentTickTime;
    
/**
 * Method for the calculation of the current tick time (adjusted with slider).
 * @see GameBoard.fxml
 * @see GameBoardController (initSlider)
 * @param gameSpeed 
 */
    protected void setGameSpeed(double gameSpeed){
        currentTickTime= (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    }

/**
 * Method for getting the current speed the game runs at.
 * @see step()
 * @return currentTickTime (in milliseconds), which controls the delay betweens steps.
 */    
    protected int getTickTime(){
        return currentTickTime;
    }
    
}
