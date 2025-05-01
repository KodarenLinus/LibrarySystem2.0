/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Login.Session;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class CustomerViewController {
    
      @FXML
    void GoToMyLoans(ActionEvent event) {
        String fxmlf = "MyLoans.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
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