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
    /*tick time in miliseconds*/
    private int minTickTime = 100;
    private int maxTickTime = 1000;
    private int currentTickTime;
    
    public GameSpeedControl(int min, int max){
                
    }
/**
 * Method for the calculation of the current tick time (adjusted with slider).
 * @see GameBoard.fxml
 * @see GameBoardController (initSlider)
 * @param gameSpeed 
 */
    protected void setGameSpeed(double gameSpeed){
        currentTickTime= (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    }
    
}
