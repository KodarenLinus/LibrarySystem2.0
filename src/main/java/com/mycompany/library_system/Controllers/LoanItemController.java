/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Logic.LoanItem;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Utils.AlertHandler;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author emildahlback
 */
public class LoanItemController {
    
    @FXML
    private ListView<Items> ItemList;

    @FXML
    private TextField ScearchItem;

    @FXML
    private ListView<Items> itemCartList;
    
    @FXML
    private RadioButton magazineFilterButton;

    @FXML
    private RadioButton referensFilterButton;
    
    @FXML
    void FilterMagazine(ActionEvent event) {
         applyFilter();
         //refreshItemList();
    }

    @FXML
    void FilterReferensBook(ActionEvent event) {
         applyFilter();
         //refreshItemList();
    }
    
    /**
     * Lägger till valt objekt i kundvagnen om det inte redan finns där.
     *
     * @param event MouseEvent som triggar när man klickar på ett objekt i listan
     */
    @FXML
    void addToCart(MouseEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        
        // Kategorier som inte får läggas till i varukorgen
        final int REFERENSLITTERATUR = 4;
        final int MAGAZINES = 5;
        
        if (selectedItem != null) {
            
            int categoryId = selectedItem.getCategoryID();
            if (categoryId == REFERENSLITTERATUR || categoryId == MAGAZINES) {
                // Visa alert-varning
                String title = "Ej tillåtet";
                String header ="Kan inte läggas till"; 
                String content = (selectedItem.getCategoryName() + " är inte tillåten i kundvagnen.");
                AlertHandler alertHandler = new AlertHandler();
                alertHandler.createAlert(title, header, content);
                return;
            }
            
            if (!itemCartList.getItems().contains(selectedItem)) {
                itemCartList.getItems().add(selectedItem);
                refreshItemList();
            }
        }
    }
    
    /**
     * Tar bort valt objekt från kundvagnen.
     *
     * @param event MouseEvent som triggar när man klickar på ett objekt i kundvagnen
     */
    @FXML
    void removeFromCart(MouseEvent event) {
         Items selectedItem = itemCartList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            itemCartList.getItems().remove(selectedItem);
            refreshItemList();
        }
    }
    
    @FXML
    void backToMenu(ActionEvent event) throws IOException{
        
        String fxmlf = "CustomerView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    
    @FXML
    void makeOrder(ActionEvent event) throws IOException{
        
        
        /*String fxmlf = "CustomerView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);*/
        
        // Skapar ett lån utifrån vad vi har i kundvagnen!!!
        LoanItem loanItem = new LoanItem();
        ArrayList<Items> itemsToLoan = new ArrayList<>(itemCartList.getItems());
        Session session = Session.getInstance();
        loanItem.addToLoanRows(session.getUserId(), itemsToLoan, event);
    }
    
     /**
     * Initierar vyn när den laddas. Ställer in hur objekt listas, hämtar alla objekt och lägger till sökfunktionalitet.
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
                    setText(item.toString()); 
                }
            }
        });
        
        // Laddar in alla objekt vid start
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("");
        allItems.removeAll(itemCartList.getItems());
        ItemList.getItems().setAll(allItems);
        
        // Söker efter objekt i realtid och visar matchningar
        ScearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Items> searchResults = searchItems.search(newValue);
            searchResults.removeAll(itemCartList.getItems());
            ItemList.getItems().setAll(searchResults);
        });
    }
    
    /**
    * Uppdaterar listan med tillgängliga objekt baserat på söktermen och tar bort de som redan finns i kundvagnen.
    */
    private void refreshItemList() {
        String searchTerm = ScearchItem.getText();
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> searchResults = new ArrayList<>(searchItems.search(searchTerm));
        searchResults.removeAll(itemCartList.getItems());
        ItemList.getItems().setAll(searchResults);
    }
    
    /**
    * 
    * Filtrerar bort categorier baserat på id.
    * 
    */
    private void applyFilter() {
        String searchTerm = ScearchItem.getText();
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search(searchTerm);
        allItems.removeAll(itemCartList.getItems());

        ArrayList<Items> filteredItems = new ArrayList<>();
        for (Items item : allItems) {
            if (magazineFilterButton.isSelected() && item.getCategoryID() == 5) {
                continue; // Om vi filtrerar på magasin och det inte är magasin → hoppa över
}
            if (referensFilterButton.isSelected() && item.getCategoryID() == 4) {
                continue; // Om vi filtrerar på referens och det inte är referens → hoppa över
            }
            filteredItems.add(item);
        }

        ItemList.getItems().setAll(filteredItems);
    }
}
