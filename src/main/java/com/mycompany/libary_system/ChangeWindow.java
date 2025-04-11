/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Linus
 */
public class ChangeWindow {
    
    public void windowChange(Event event, String fmxlf) {
        
         try {
            Parent root = FXMLLoader.load(getClass().getResource(fmxlf));
            Scene scene = new Scene(root);
            
            //Hämtar nuvarande fönster
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            stage.setScene(scene);
            stage.setTitle("Library System");
            stage.show();
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
}

