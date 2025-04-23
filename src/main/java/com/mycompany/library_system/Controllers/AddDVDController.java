/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddDVD;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Utils.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 *
 * @author Linus
 */
public class AddDVDController {

    @FXML
    private TextField Location;

    @FXML
    private TextField Title;

    @FXML
    private ToggleButton addNewDVD;

    @FXML
    void GoToItem(ActionEvent event) {
          
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);

    }

    @FXML
    void addDVD(ActionEvent event) {
        PopUpWindow popUpWindow = new PopUpWindow();
        String fxmlf = "newDVDPop.fxml";
        popUpWindow.popUp(event, fxmlf);
        
        AddDVD addDVD = new AddDVD();
        DVD dvd = new DVD(Title.getText(), Location.getText());
        addDVD.insertDVD(dvd);
    }

}
