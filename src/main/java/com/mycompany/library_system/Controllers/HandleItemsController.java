/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Utils.ObjectSession;
import com.mycompany.library_system.Utils.PopUpWindow;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Linus
 */
public class HandleItemsController {
    
    @FXML
    private ListView<Items> ItemList;

    @FXML
    private TextField SearchItem;

    @FXML
    void AddDirectorOrAuthor(ActionEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleAddCreator(selectedItem, null); // event kan vara null om du inte behöver det
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
        ArrayList<Items> allItems = searchItems.search("");
        ItemList.getItems().setAll(allItems);
        
        // Söker efter objekt i realtid och visar matchningar
        SearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
       
    }
    
    private void handleAddCreator(Items item, MouseEvent event) {
        ObjectSession.setCurrentItem(item);
    
        PopUpWindow popUpWindow = new PopUpWindow();

        if (item instanceof Book) {
            // Öppna fönster för att lägga till författare
            popUpWindow.popUp(event, "AddAuthorToBook.fxml");
        } else if (item instanceof DVD) {
            // Öppna fönster för att lägga till regissör
            popUpWindow.popUp(event, "AddDirectorToDVD.fxml");
        } else {
            System.out.println("Okänd typ av item");
        }
    }
    
    
    /**
    * 
    * kollar om filter är applicerad och uppdaterar listan med items.
    * 
    */
    private void applyFilter() {
       String searchTerm = SearchItem.getText();
       SearchItems searchItems = new SearchItems();
       ArrayList<Items> allItems = searchItems.search(searchTerm);
       ItemList.getItems().setAll(allItems);
    }
}
