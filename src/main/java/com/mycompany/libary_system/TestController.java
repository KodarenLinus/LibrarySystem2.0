package com.mycompany.libary_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;

public class TestController {

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

    }

    @FXML
    void addBook(ActionEvent event) {
        AddBook addBook = new AddBook();
        addBook.insertBook(Title.getText(), Location.getText(), Integer.parseInt(ISBN.getText()));
    }

}
