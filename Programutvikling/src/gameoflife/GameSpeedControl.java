/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

/**
 *
 * @author Espen
 */
public class GameSpeedControl {
    /*tick time in miliseconds*/
    private int minTickTime = 100;
    private int maxTickTime = 1000;
    private int currentTickTime;
    
    public GameSpeedControl(int min, int max){
                
    }
    
    protected void setGameSpeed(double gameSpeed){
        currentTickTime= (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    }
    
}
