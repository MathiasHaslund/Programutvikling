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
    protected int xmax = 20;//10
    protected int ymax = 20;//10
    
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
        if (listLength==0){
            return null;
        }
        int[] coordinates = cellChangeList.get(0);
        cellChangeList.remove(0);
        return coordinates;
    }
    
    protected void setGameSpeed(float speed){
        if (speed >=0 && speed <=1){
            gameSpeed = speed;
        }
        
    } 


    private boolean isOutsideBoard(int x, int y){
        if (x<0 || x>xmax || y<0 || y>ymax){
            return true;
        }
        else{
            return false;
        }
    }
    
    private int countLiveNeighbours(int x, int y){
        int counter = 0;
		for (int i =-1; i<=1; i++){
                    for (int j = -1; j<=1; j++){
                        int xn = x+i;
                        int yn = y+j;
                        /*checks if current cell is a cell on the board and is a neighbour of the cell we are checking, and not the cell itself*/
                        if ((xn == x && yn == y) || isOutsideBoard(xn, yn)){
                            continue;
                        }
                        if (getCellState(xn,yn) == true){
                            counter ++;
                        }
                    }
                    
                }               
                return counter;
    }
    protected void gameLogic(){
        for (int i = 0; i<xmax; i++){
            for (int j = 0; j<ymax; j++){
		int counter = countLiveNeighbours(i, j);
		if (getCellState(i, j) == true){
                    if (counter<2 || counter>3){
                        addToCellChangeList(i, j);
                    }
		}
                else {
                    if (counter == 3){
			addToCellChangeList(i, j);
                    }
		}					
            }
        }
        for (int i = 0; i<cellChangeList.size(); i++){
            int[] coordinates = cellChangeList.get(i);
            int x = coordinates[0];
            int y = coordinates[1];
            cellStates[x][y] = !cellStates [x][y];
        }
    }
}
