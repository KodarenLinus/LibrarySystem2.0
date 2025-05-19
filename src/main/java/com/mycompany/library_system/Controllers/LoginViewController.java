/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Login.Login;
import com.mycompany.library_system.Utils.AlertHandler;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoginViewController{
    
    String title;
    String header;
    String content;
    AlertHandler alert = new AlertHandler();
    
    private Login login = new Login();
    
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
    private void loginButton(ActionEvent event) throws IOException {

        boolean isLoggedIn = login.doLogin(userName.getText(), userPassword.getText());

        if (isLoggedIn) {
            String fxmlf = "CustomerStart.fxml";
            ChangeWindow changeWindow = new ChangeWindow();
            changeWindow.windowChange(event, fxmlf);
        } else {
            System.out.println("Login failed: wrong username or password");
            title = "Fel vid inloggning";
            header = "Fel användarnamn eller lösenord";
            content = "Kolla att du skrivit användaruppgifterna är rätt";
            alert.createAlert(title, content, content);
        }
    }
    
    
    /**
     * Navigerar tillbaka till startvyn.
     *
     * @param event ActionEvent som triggas vid knapptryckning
     * @throws IOException om det uppstår fel vid vybyte
     */
    @FXML
    void back(ActionEvent event) throws IOException{
        
        String fxmlf = "StartView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
        
    /**
     * Hanterar inloggning för anställda.
     *
     * @param event ActionEvent som triggas när anställd vill logga in
     * @throws IOException om det uppstår fel vid vybyte
     */
    @FXML
    private void loginButtonEmployee(ActionEvent event) throws IOException{
        boolean isLoggedIn = login.doStaffLogin(userName.getText(), userPassword.getText());

        if (isLoggedIn) {
            String fxmlf = "StaffStart.fxml";
            ChangeWindow changeWindow = new ChangeWindow();
            changeWindow.windowChange(event, fxmlf);
        } else {
            System.out.println("Login failed: wrong username or password");
            title = "Fel vid inloggning";
            header = "Fel användarnamn eller lösenord";
            content = "Kolla att du skrivit användaruppgifterna är rätt";
            alert.createAlert(title, content, content);
        }
        
    }   
    
}