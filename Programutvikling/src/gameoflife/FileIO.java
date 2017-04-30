package gameoflife;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mathias Haslund
 * @author Josef Krivan
 * @version 0.9
 * @since 0.9 (29/04/2017)
 */
public class FileIO {
 
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
                    System.out.println("tekst"+i+j);
                }
            }
        }
        writeCompressedBlock(os, counter, last);
        
        os.close();
    }
    
    protected boolean[][] readBoardFromFile(GameBoardModel gameBoardModel, String file) throws IOException{
        File inputFile = new File ("savegame/"+file+".dat");
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
    
    private void writeCompressedBlock(DataOutputStream os, int amount, boolean alive) throws IOException{        
        byte codedByte = (byte) amount;
        if(!alive){
            codedByte = (byte) (-1*codedByte);
        }
        os.writeByte(codedByte);
    }
    
}
