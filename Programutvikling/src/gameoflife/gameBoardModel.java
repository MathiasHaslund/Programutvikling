/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Espen
 */
public class gameBoardModel {
    
    private float gameSpeed = 0f;
    
    private ArrayList<int[]> cellChangeList = new ArrayList<int[]>();
    private boolean cellStates[][] = new boolean[1000][1000];
    protected int xmax = 10;
    protected int ymax = 10;
    
    protected void initCellStates (){
        for (int i=0; i<xmax; i++){
            for (int j=0; j<ymax; j++){
                cellStates[i][j] = false;
            }
        }
    }
    
    protected void toggleCellState (int x, int y){
        cellStates[x][y] = !cellStates [x][y];
        addToCellChangeList(x, y);
    }
    
    protected boolean getCellState(int x, int y){
        return cellStates [x][y];
    }
    
    protected void addToCellChangeList (int x, int y){
        int[] coordinates = new int[2];
        coordinates[0]=x;
        coordinates[1]=y;
        cellChangeList.add(coordinates);
    }
    
    protected int[] takeNextCellChange(){
        int listLength = cellChangeList.size();
        int[] coordinates = cellChangeList.get(0);
        cellChangeList.remove(0);
        return coordinates;
    }
    
    protected void setGameSpeed(float speed){
        if (speed >=0 && speed <=1){
            gameSpeed = speed;
        }
        
    } 

    protected void dummyPlayGame(){
        for (int i=0; i<3; i++){
            System.out.println("linje"+i);
             try {
                // Wait for 1 second.
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {}
            }
    }
}
