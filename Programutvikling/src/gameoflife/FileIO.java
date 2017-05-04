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
 * FileIO is used for writing and reading game data from files.
 * This allows the game board size and the status of each cell to be saved to a compressed lossless format using run length encoding. 
 * The data is saved in binary, with the first 4 bytes containing the length of the x-axis as an integer, and the next 4 bytes containing the length of the y-axis.
 * After the first 8 bytes data is saved in "blocks" of one byte, with positive numbers representing live cells, and negative numbers representing dead cells.
 * 
 * FileIO contains methods for {@link #writeBoardToFile saving} the existing game board to a file, and to {@link #readBoardFromFile create} a game board by reading data from a file
 * @author Mathias
 */
public class FileIO {
    /**
     * Saves the current state of the board to a .dat file.
     * 
     * @param gameBoardModel contains information about the size of the board i.e. the number of columns/rows and whether the cells are alive or dead.
     * @throws IOException if unable to write to file.
     * @see readBoardFromFile
     */
    protected void writeBoardToFile(GameBoardModel gameBoardModel) throws IOException{
        File outputFile = new File ("savegame/UserSave.dat");
        DataOutputStream os;
        os = new DataOutputStream(new FileOutputStream(outputFile));
        int xmax = gameBoardModel.getXmax();
        int ymax = gameBoardModel.getYmax();        
        os.writeInt(xmax);
        os.writeInt(ymax);
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
    * Reads the game board from a saved .dat file.
    * @param gameBoardModel overwrites existing data to create a new board
    * @param file name of the file to load
    * @throws IOException if unable to find the correct file
    * @return cellIsAliveArray for building the new board with the correct cell statuses
    * @see writeBoardToFile
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
    /*reads the data from fillnumber and adds them to the cellIsAliveArray*/
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
    /*writes runs to the data stream*/
    private void writeCompressedBlock(DataOutputStream os, int amount, boolean alive) throws IOException{        
        byte codedByte = (byte) amount;
        if(!alive){
            codedByte = (byte) (-1*codedByte);
        }
        os.writeByte(codedByte);
    }
    
}
