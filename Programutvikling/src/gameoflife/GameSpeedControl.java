package gameoflife;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */

/**
 * Constructor for setting the maximum, minimum and current tick time.
 * The slider in the GUI is directly tied to the values in this constructor.
 * @see GameBoardController
 * Tick time is in milliseconds.
 */
public class GameSpeedControl {
    /**
     * Minimum tick time (Value = 0 on slider).
     */
    private int minTickTime = 100;
    /**
     * Maximum tick time (Value = 1 on slider).
     */
    private int maxTickTime = 1000;
    /**
     * Tick time based on the sliders position between 0 and 1.
     * @see setGameSpeed (Mathematical calculation with relation to max and min tick time.)
     * 100 <= currentTickTime >= 1000
     */
    private int currentTickTime;
    
/**
 * Class for the calculation of the current tick time (adjusted with slider).
 * @see GameBoardController (initSlider)
 * @param gameSpeed  todo
 */
    protected void setGameSpeed(double gameSpeed){
        /**
         * How the current tick time is calculated.
         * currentTickTime is based on the slider's position between the minimum and maximum positions.
         */
        currentTickTime= (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    }

/**
 * Method for getting the current speed the game runs at.
 * @see GameBoardController (step)
 * @return currentTickTime (in milliseconds), which controls the delay between steps.
 */    
    protected int getTickTime(){
        return currentTickTime;
    }
    
}
