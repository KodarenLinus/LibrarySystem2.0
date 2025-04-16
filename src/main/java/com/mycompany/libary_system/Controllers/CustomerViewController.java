/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Controllers;

import com.mycompany.libary_system.Utils.ChangeWindow;
import com.mycompany.libary_system.Login.Session;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author emildahlback
 */
public class CustomerViewController {
    @FXML
    void loanItem(ActionEvent event) throws IOException {
        
        String fxmlf = "LoanItem.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    @FXML
    void logOut(ActionEvent event) throws IOException{
        
        Session.getInstance().clear(); // Tar bort sessionen!!!
        String fxmlf = "LoginView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
}