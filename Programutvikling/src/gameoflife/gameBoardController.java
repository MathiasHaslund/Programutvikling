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
 * @author Espen
 */
public class gameBoardController implements Initializable{
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initBoard();
    }
    
    @FXML
    private GridPane gridPane1;
    
    @FXML
    private void initBoard() {
        for (int i=0; i<10; i++)
        {
            for (int j=0; j<10; j++)
            {
                String buttonId = "cell_"+i+"_"+j;
                Button button = new Button();
                button.setText("N");
                button.setId(buttonId);
                button.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                    getCoordinates(buttonId);                   
                    };
                });
                       
                gridPane1.add(button, i, j);
                System.out.println(button.getId());
            }
        }
    }
    private void getCoordinates(String buttonId){   
        int p1 = buttonId.indexOf("_",0);
        int p2 = buttonId.indexOf("_",p1+1);
        int x = Integer.parseInt(buttonId.substring(p1+1, p2));
        int y = Integer.parseInt(buttonId.substring(p2+1));
        System.out.println("Du har valgt: "+x+", "+y);
        
    }
    
}
