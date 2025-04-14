/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 *
 * @author emildahlback
 */
public class AddCustomerController {
    
    
    @FXML
    private TextField Email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField telNr;

    @FXML
    private ToggleButton addCustomer;
    
    
    @FXML
    void addCustomer(ActionEvent event) {
        AddCustomer addCustomer = new AddCustomer();
        addCustomer.insertCustomer(firstName.getText(), lastName.getText(), Integer.parseInt(telNr.getText()), Email.getText());
    
    }
    
    @FXML
    void backToMenu(ActionEvent event) {
        
        String fxmlf = "StartMenu.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
}
    

