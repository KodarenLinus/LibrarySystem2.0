package com.mycompany.libary_system.Controllers;

import com.mycompany.libary_system.Logic.AddBook;
import com.mycompany.libary_system.Models.Book;
import com.mycompany.libary_system.Utils.ChangeWindow;
import com.mycompany.libary_system.Utils.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
       
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);

    }

    @FXML
    void addBook(ActionEvent event) {       
        PopUpWindow popUpWindow = new PopUpWindow();
        String fxmlf = "newBookPop.fxml";
        popUpWindow.popUp(event, fxmlf);
        // Efter att popupen har stängts, lägg till boken i databasen
        AddBook addBook = new AddBook();
        Book book = new Book(Title.getText(), Location.getText(), Integer.parseInt(ISBN.getText()));
        addBook.insertBook(book);
    }
}
