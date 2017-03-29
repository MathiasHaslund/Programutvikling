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
    /*always between 0 and 1. lower value is slower*/
    private float gameSpeed = 0.5f;
    /*tick time in miliseconds*/
    private int minTickTime = 100;
    private int maxTickTime = 1000;
    private int currentTickTime = (int)(1.0/(1.0/maxTickTime + gameSpeed*(1.0/minTickTime - 1.0/maxTickTime)));
    //private int currentTickTime = (int)(maxTickTime - gameSpeed*(maxTickTime - minTickTime));
    
    private ArrayList<gameBoardCell> cellChangeList = new ArrayList<gameBoardCell>();
    //private ArrayList<int[]> cellChangeList = new ArrayList<int[]>();
    private boolean cellIsAliveArray[][]; //new boolean[1000][1000];
    protected int xmax = 40;
    protected int ymax = 30;
    
    /*tick time in miliseconds*/
    protected int getCurrentTickTime(){
        return currentTickTime;
    }
    
    protected void initCellStates (){
        cellIsAliveArray = new boolean[xmax][ymax];
        for (int i=0; i<xmax; i++){
            for (int j=0; j<ymax; j++){
                cellIsAliveArray[i][j] = false;
            }
        }
    }
    
    protected void toggleCellState (int x, int y){
        cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        addToCellChangeList(x, y);
    }
    
    protected boolean getCellIsAlive(int x, int y){
        return cellIsAliveArray [x][y];
    }
    
    protected void addToCellChangeList (int x, int y){        
        cellChangeList.add(new gameBoardCell(x, y, cellIsAliveArray));
    }
    
    protected gameBoardCell takeNextCellChange(){
        int listLength = cellChangeList.size();
        if (listLength==0){
            return null;
        }
        gameBoardCell gameBoardCell = cellChangeList.get(0);
        cellChangeList.remove(0);
        return gameBoardCell;
    }
    
    protected void setGameSpeed(float speed){
        if (speed >=0 && speed <=1){
            gameSpeed = speed;
        }
        
    } 


    private boolean isOutsideBoard(int x, int y){
        if (x<0 || x>=xmax || y<0 || y>=ymax){
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
                        if (getCellIsAlive(xn,yn) == true){
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
		if (getCellIsAlive(i, j) == true){
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
            gameBoardCell gameBoardCell = cellChangeList.get(i);
            int x = gameBoardCell.getX();
            int y = gameBoardCell.getY();
            cellIsAliveArray[x][y] = !cellIsAliveArray [x][y];
        }
    }
}
