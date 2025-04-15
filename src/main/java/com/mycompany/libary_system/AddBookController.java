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
       
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);

    }

    @FXML
    void addBook(ActionEvent event) {
    try {
        // Ladda FXML-filen för popupen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newBookPop.fxml"));
        Parent root = loader.load();  // Här laddas rootkomponenten från FXML-filen

        // Skapa en ny Stage (popup)
        Stage stage = new Stage();
        stage.setTitle("Popup Title");

        // Sätt scenen för popupen
        stage.setScene(new Scene(root));

        // Visa popupen och vänta på att den stängs
        stage.showAndWait();

        // Efter att popupen har stängts, lägg till boken i databasen
        AddBook addBook = new AddBook();
        Book book = new Book(Title.getText(), Location.getText(), Integer.parseInt(ISBN.getText()));
        addBook.insertBook(book);

    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
