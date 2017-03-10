/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


/**
 *
 * @author Mathias
 */
public class gameBoardController implements Initializable{
    
    gameBoardModel gameBoardModel = new gameBoardModel();
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initBoard();
    }
    
    @FXML
    private GridPane gridPane1;
    
    @FXML
    private void initBoard() {
        gameBoardModel.initCellStates();
        for (int i=0; i<gameBoardModel.xmax; i++)
        {
            for (int j=0; j<gameBoardModel.ymax; j++)
            {
                String buttonId = "cell_"+i+"_"+j;
                Button button = new Button();
                if (gameBoardModel.getCellState(i, j)){
                    button.setText("L");
                }
                else{
                    button.setText("D");
                }
                    
                button.setId(buttonId);
                button.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                    writeCellClickToModel(buttonId);
                    refreshBoard();
                    };
                });
                       
                gridPane1.add(button, i, j);
                System.out.println(button.getId());
            }
        }
    }
    private void writeCellClickToModel(String buttonId){   
        int p1 = buttonId.indexOf("_",0);
        int p2 = buttonId.indexOf("_",p1+1);
        int x = Integer.parseInt(buttonId.substring(p1+1, p2));
        int y = Integer.parseInt(buttonId.substring(p2+1));
        gameBoardModel.toggleCellState(x, y);
        System.out.println("You have chosen: "+x+", "+y);
        System.out.println("cell state: "+gameBoardModel.getCellState(x, y));
        
    }
    
    private void oldrefreshBoard(){
        for (int i=0; i<gameBoardModel.xmax; i++)
        {
            for (int j=0; j<gameBoardModel.ymax; j++)
            {
                String buttonId="cell_"+i+"_"+j;
                Button button = (Button) gridPane1.lookup("#"+buttonId);
                if (gameBoardModel.getCellState(i, j)){
                    button.setText("L");
                }
                else{
                    button.setText("D");
                }
            }
        }
    }
    
    private void refreshBoard(){
        int[] coordinates = gameBoardModel.takeNextCellChange();
        int x = coordinates[0];
        int y = coordinates[1];
        String buttonId="cell_"+x+"_"+y;
        Button button = (Button) gridPane1.lookup("#"+buttonId);
        
        if (gameBoardModel.getCellState(x, y)){
            button.setText("L");
        }
        else{
            button.setText("D");
         }
    }
     
    @FXML
    private void testText(){
        gameBoardModel.dummyPlayGame();
    }
    
    private void gameLogic(){
        for (int i = 1; i<gameBoardModel.xmax-1; i++){
            for (int j = 1; j<gameBoardModel.ymax-1; j++){
		int counter = 0;
		
		if (gameBoardModel.getCellState(i-1,j-1) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i-1,j) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i-1, j+1) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i, j-1) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i, j+1) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i+1, j-1) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i+1, j) == true){
                    counter += 1;
		}
		if (gameBoardModel.getCellState(i+1, j+1) == true){
                    counter += 1;
		}
	

		if (gameBoardModel.getCellState(i, j) == true){
                    if (counter<2 || counter>3){
                        gameBoardModel.addToCellChangeList(i, j);
                    }
		}
                else {
                    if (counter == 3){
			gameBoardModel.addToCellChangeList(i, j);
                    }
		}					
            }
        }
    }
    
}
