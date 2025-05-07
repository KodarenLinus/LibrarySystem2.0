/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ItemManagement.RemoveItem;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Utils.ObjectSession;
import com.mycompany.library_system.Utils.PopUpWindow;
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
    void RemoveObject(ActionEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            RemoveItem removeItem = new RemoveItem();
            boolean success = removeItem.deleteItem(selectedItem);

            if (success) {
                ItemList.getItems().remove(selectedItem);
            } else {
                // Visa felmeddelande
                AlertHandler alert = new AlertHandler();
                alert.createAlert("Fel vid borttagning", "Objektet kunde inte tas bort", "Kontrollera databasen och försök igen.");
            }
        } else {
            AlertHandler alert = new AlertHandler();
            alert.createAlert("Inget objekt valt", "Du måste välja ett objekt", "Vänligen välj ett objekt i listan att ta bort.");
        }
    }

    @FXML
    void UpdateObject(ActionEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObjectSession.getInstance().setCurrentItem(selectedItem);
            String fxmlFile = "UpdateTitleItem.fxml";
            PopUpWindow popUpWindow = new PopUpWindow();
            popUpWindow.popUp(event, fxmlFile);
        } else {
            AlertHandler alert = new AlertHandler();
            alert.createAlert("Inget objekt valt", "Du måste välja ett objekt", "Välj ett objekt i listan att uppdatera.");
        }
    }
    
    
    @FXML
    void back(ActionEvent event) throws IOException{
        String fxmlf = "StartMenu.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
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
                    if (item instanceof Book) {
                        setText(item.toString() + ": Bok"); 
                    } else {
                        setText(item.toString() + ": DVD");
                    }
                }
            }
        });
        
        // Laddar in alla objekt vid start
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("", false);
        ItemList.getItems().setAll(allItems);
        
        // Söker efter objekt i realtid och visar matchningar
        SearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
       
    }
    
    private void handleAddCreator(Items item, MouseEvent event) {
        ObjectSession.getInstance().setCurrentItem(item);
        
        PopUpWindow popUpWindow = new PopUpWindow();
        
        Object choicenItem = ObjectSession.getInstance().getCurrentItem();
        if (choicenItem instanceof Book) {
            // Öppna fönster för att lägga till författare
            popUpWindow.popUp(event, "AddAuthorToBook.fxml");
        } else if (choicenItem instanceof DVD) {
            String title = "Kan inte lägga till författare";
            String header = "DVD kan inte lägga till författare";
            String content = "Vänlingen välj en book";
            AlertHandler alertHandler = new AlertHandler();
            alertHandler.createAlert(title, header, content);
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
       ArrayList<Items> allItems = searchItems.search(searchTerm, false);
       ItemList.getItems().setAll(allItems);
    }
}
