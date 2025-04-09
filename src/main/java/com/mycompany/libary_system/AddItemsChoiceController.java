/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

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
 * @author Linus
 */
public class AddItemsChoiceController {
    
    @FXML
    void AddBook(ActionEvent event) throws IOException {
        
           try {
            Parent root = FXMLLoader.load(getClass().getResource("AddBook.fxml"));
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

    @FXML
    void AddDvD(ActionEvent event) {

    }

}
