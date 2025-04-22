/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Controllers;

import com.mycompany.libary_system.Utils.ChangeWindow;
import com.mycompany.libary_system.Models.Items;
import com.mycompany.libary_system.Logic.LoanItem;
import com.mycompany.libary_system.Search.SearchItems;
import com.mycompany.libary_system.Login.Session;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
    void addToCart(MouseEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();

        if (selectedItem != null && !itemCartList.getItems().contains(selectedItem)) {
            itemCartList.getItems().add(selectedItem);
            refreshItemList();
        }
    }
    
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
        
        String fxmlf = "CustomerView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
        
        // Skapar ett lån utifrån vad vi har i kundvagnen!!!
        LoanItem loanItem = new LoanItem();
        ArrayList<Items> itemsToLoan = new ArrayList<>(itemCartList.getItems());
        Session session = Session.getInstance();
        loanItem.addToLoanRows(session.getUserId(), itemsToLoan, event);
    }
    
    @FXML
    public void initialize()  {
        
        // Gör så att vi visar titlen på våra items!!!!
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
        
        // Visar alla våra items när sidan laddas in!!
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("");
        allItems.removeAll(itemCartList.getItems());
        ItemList.getItems().setAll(allItems);
        
        // Updaterar våran item lista i real tid!!!
        ScearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Items> searchResults = searchItems.search(newValue);
            searchResults.removeAll(itemCartList.getItems());
            ItemList.getItems().setAll(searchResults);
        });
    }
    
    // en metod som vi använder för att uppdatera våra items i de olika listorna (kundvagn och tillgängliga items)
    private void refreshItemList() {
        String searchTerm = ScearchItem.getText();
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> searchResults = new ArrayList<>(searchItems.search(searchTerm));
        searchResults.removeAll(itemCartList.getItems());
        ItemList.getItems().setAll(searchResults);
    }
}
