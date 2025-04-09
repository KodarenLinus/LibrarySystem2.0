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

public class AddBookController {

     @FXML
    private TextField ISBN;

    @FXML
    private TextField Location;

    @FXML
    private TextField Title;
   
    
    @FXML
    private ToggleButton addNewBook;
    
     @FXML
    void GoToItem(ActionEvent event) {
        
          try {
            Parent root = FXMLLoader.load(getClass().getResource("AddItemsChoice.fxml"));
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
    void addBook(ActionEvent event) {
        AddBook addBook = new AddBook();
        addBook.insertBook(Title.getText(), Location.getText(), Integer.parseInt(ISBN.getText()));
    }

}
