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
    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ListView<Items> ItemList;

    @FXML
    private TextField SearchItem;

    /**
     * Lägger till en författare (om boken är vald) eller visar varning om objektet är en DVD.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void AddDirectorOrAuthor(ActionEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleAddCreator(selectedItem, null); // event kan vara null om du inte behöver det
        }
    }
    
    /**
     * Tar bort valt objekt från listan och databasen.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void RemoveObject(ActionEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            RemoveItem removeItem = new RemoveItem();
            boolean success = removeItem.deleteItem(selectedItem);

            if (success) {
                ItemList.getItems().remove(selectedItem);
            } 
        } else {
            AlertHandler alert = new AlertHandler();
            title = "Inget objekt valt";
            header = "Du måste välja ett objekt";
            content = "Vänligen välj ett objekt i listan att ta bort.";
            alert.createAlert(title, header, content);
        }
    }

    /**
     * Navigerar till vyn för att uppdatera valt objekt.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void UpdateObject(ActionEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObjectSession.getInstance().setCurrentItem(selectedItem);
            String fxmlFile = "UpdateTitleItem.fxml";
            PopUpWindow popUpWindow = new PopUpWindow();
            popUpWindow.popUp(event, fxmlFile);
        } else {
            title = "Inget objekt valt";
            header = "Du måste välja ett objekt";
            content = "Välj ett objekt i listan att uppdatera.";
            alert.createAlert(title, header, content);
        }
    }
    
    /**
     * Navigerar tillbaka till startmenyn.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     * @throws IOException om FXML-filen inte kan laddas
     */
    @FXML
    void back(ActionEvent event) throws IOException{
        String fxmlf = "StaffStart.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    
    /**
     * Initierar komponenter och laddar in objekt i listan.
     * Ställer in cellformat och lägger till lyssnare för sökning.
     */
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
    
    /**
     * Hanterar tillägg av skapare till valt objekt beroende på typ.
     * Öppnar nytt fönster för bok, annars visas varning.
     * 
     * @param item  Det valda objektet
     * @param event MouseEvent om popup kräver det (kan vara null)
     */
    private void handleAddCreator(Items item, MouseEvent event) {
        ObjectSession.getInstance().setCurrentItem(item);
        
        PopUpWindow popUpWindow = new PopUpWindow();
        
        Object choicenItem = ObjectSession.getInstance().getCurrentItem();
        if (choicenItem instanceof Book) {
            // Öppna fönster för att lägga till författare
            popUpWindow.popUp(event, "AddAuthorToBook.fxml");
        } else if (choicenItem instanceof DVD) {
            title = "Kan inte lägga till författare";
            header = "DVD kan inte lägga till författare";
            content = "Vänlingen välj en book";
            alert.createAlert(title, header, content);
        } else {
            System.out.println("Okänd typ av item");
        }
    }
    
    
    /**
    * kollar om filter är applicerad och uppdaterar listan med items.
    */
    private void applyFilter() {
       String searchTerm = SearchItem.getText();
       SearchItems searchItems = new SearchItems();
       ArrayList<Items> allItems = searchItems.search(searchTerm, false);
       ItemList.getItems().setAll(allItems);
    }
}
