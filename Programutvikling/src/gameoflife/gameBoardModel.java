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
public class gameBoardModel {
    
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
    }
    
    protected boolean getCellState(int x, int y){
        return cellStates [x][y];
    }
    
}
