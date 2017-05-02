package gameoflife;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */
 /**
 * Main class that operates the importing and exporting of game board configurations.
 */
public class FileIO {
    /**
     * Method that allows game boards to written and saved as .dat files.
     * .dat files contain the information for the stored game board configuration.
     * @param gameBoardModel
     * @throws IOException 
     */
    protected void writeBoardToFile(GameBoardModel gameBoardModel) throws IOException{
        File outputFile = new File ("savegame/UserSave.dat");
        DataOutputStream os;
        /**
        * Gathers information for the size of the game board.
        * @see GameBoardModel.java
        * @see GameBoardController.java
        */
        os = new DataOutputStream(new FileOutputStream(outputFile));
        int xmax = gameBoardModel.getXmax();
        int ymax = gameBoardModel.getYmax();
        
        os.writeInt(xmax);
        os.writeInt(ymax);
        /**
         * Using previous code to be able to create a copy of the current status of the cells on the board.
         * @see GameBoardController.java (GameLogic)
         * Writes the saved code in 8-bit integers (-127 to 127)
         * The 8-bit code written is based on the position and state of each cell on the board.
         * Alive cells are saved as positive numbers, dead cells are saved as negative numbers.
         * If a cell is saved as '150', it will be written as 127 23.
         */
        int counter = 0;
        boolean last=false;
        boolean atStart=true;
        for (int i=0; i<xmax; i++){
            for (int j=0; j<ymax; j++){
                boolean current = gameBoardModel.getCellIsAlive(i, j);
                if (atStart){
                    last=current;
                    atStart=false;
                }
                
                if (current == last && counter < 127){
                    counter ++;                    
                }
                else{                                                          
                    writeCompressedBlock(os, counter, last); 
                    counter = 1;
                    last=current;
                }
            }
        }
        writeCompressedBlock(os, counter, last);
        
        os.close();
    }
    /**
    * This reads the game board from the saved .dat file.
    * @see GameBoardModel.java
    * @param gameBoardModel
    * @throws IOException 
    */
    protected boolean[][] readBoardFromFile(GameBoardModel gameBoardModel, String file) throws IOException{
        File inputFile = new File ("savegame/"+file);
        DataInputStream os;

        os = new DataInputStream(new FileInputStream(inputFile));
        int xmax = os.readInt();
        int ymax = os.readInt();
        gameBoardModel.setXmax(xmax);
        gameBoardModel.setYmax(ymax);
        int startX = 0;
        int startY = 0;
        boolean [][] cellIsAliveArray = new boolean[xmax][ymax];
        while(true){
            try{
                byte output = os.readByte();
                byte fillNumber = (byte) Math.abs(output);
                boolean isAlive = false;                
                if (output>0){
                    isAlive = true;
                }                
                    int[] startValues = fillArray(cellIsAliveArray,startX, startY, fillNumber, isAlive);
                    startX = startValues[0];
                    startY = startValues[1];
            }
            catch (EOFException e){
                break;
            }
        }
        os.close();
        return cellIsAliveArray;
    }
        
    private int[] fillArray(boolean [][] cellIsAliveArray, int startX, int startY, byte fillNumber, boolean isAlive){
        int nextX = startX;
        int nextY = startY;
        int ymax = cellIsAliveArray[0].length;
        int[] startValue;
        startValue = new int[2];
        for (int i = 0; i < fillNumber; i++){
            cellIsAliveArray[nextX][nextY] = isAlive;            
            if(nextY+1>=ymax){
                nextX+=1;
                nextY=0;
            }
            else{
                nextY+=1;
            }
            startValue[0] = nextX;
            startValue[1] = nextY;            
        }
        return startValue;
    }
    /**
     * Files are compressed in one byte (8-bit) integers.
     * Each individual cell is saved as either a positive (alive) or negative (dead) number.
     * Numbers range from -127 to 127.
     * @param os
     * @param amount
     * @param alive
     * @throws IOException 
     */
    private void writeCompressedBlock(DataOutputStream os, int amount, boolean alive) throws IOException{        
        byte codedByte = (byte) amount;
        if(!alive){
            codedByte = (byte) (-1*codedByte);
        }
        os.writeByte(codedByte);
    }
    
}
