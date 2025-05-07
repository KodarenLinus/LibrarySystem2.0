/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ChangeWindow;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class StartViewController {
    
    @FXML
    private TextField SearchItem;

    @FXML
    private ListView<Items> ItemList;

    @FXML
    void login(ActionEvent event) throws IOException {
        
        String fxmlf = "LoginView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    @FXML
    void itemClicked(MouseEvent event) {
    Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
        // Visa alert-varning
                String title = "Inloggning krävs";
                String header ="Du måste vara inloggad"; 
                String content = ("Logga in för att kunna låna detta objekt.");
                AlertHandler alertHandler = new AlertHandler();
                alertHandler.createAlert(title, header, content);
                return;
    }
}
    @FXML
    public void initialize()  {
        
        // Visar titel för varje objekt i listan
        ItemList.setCellFactory(list -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); 
                }
            }
        });
        
        // Laddar in alla objekt vid start
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("", false);
        ItemList.getItems().setAll(allItems);
        
        
        // Söker efter objekt i realtid och visar matchningar
        SearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Items> searchResults = searchItems.search(newValue, false);
            ItemList.getItems().setAll(searchResults); 
        });
    }
    
    

}
