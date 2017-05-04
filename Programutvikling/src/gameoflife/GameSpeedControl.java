package gameoflife;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */

/**
 * GameSpeedControl is used for {@link #setGameSpeed setting} the speed at which the game runs, and {@link #getTickTime returning} that value as needed.
 */
public class GameSpeedControl {

    private int minTickTime;
    private int maxTickTime;
    private int currentTickTime;
    
    /**
     * Sets minimum and maximum tick time.
     */
    public void GameSpeedControl(){
        minTickTime=100;
        maxTickTime = 1000;
    }

    /**
     * Calculates the tick time in milliseconds.
     * @param gameSpeed double ranging from 0-1, 0 being slow, 1 being the fastest.
     */
    protected void setGameSpeed(double gameSpeed){
        currentTickTime= (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    }

    /**
    * Gives the current tick time of the game
    * @return currentTickTime which controls the delay in milliseconds between steps.
    */    
    protected int getTickTime(){
        return currentTickTime;
    }
    
}
