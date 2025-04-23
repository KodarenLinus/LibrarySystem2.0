/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Login.Login;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author emildahlback
 */
public class LoginViewController{
    
    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;
    
    /**
     * Hanterar inloggning för användare
     *
     * @param event ActionEvent som triggas när användaren vill logga in
     */
    @FXML
    void loginButton(ActionEvent event) throws IOException{
        
        Login login = new Login();
        boolean isLoggedIn = login.doLogin(userName.getText(), userPassword.getText());

        if (isLoggedIn) {

            String fxmlf = "CustomerView.fxml";
            ChangeWindow changeWindow = new ChangeWindow();
            changeWindow.windowChange(event, fxmlf);
        } else {
                System.out.println("Login failed: wrong username or password");
                // Lägg till 
        }
    }
        
    @FXML
    void loginButtonEmployee(ActionEvent event) throws IOException{
        
        String fxmlf = "StartMenu.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    
    
}