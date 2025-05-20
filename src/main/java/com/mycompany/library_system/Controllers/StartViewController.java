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
 * Controller för startsidan där användare (utan inloggning) kan se tillgängliga objekt i biblioteket.
 * Denna vy används som en "publik katalog" innan användaren loggar in.
 * 
 * Fungerar tillsammans med StartView.fmxl
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class StartViewController {
    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private TextField SearchItem; // Textfält för sökning efter objekt

    @FXML
    private ListView<Items> ItemList; // Lista som visar objekten (böcker/DVD etc.)

    /**
     * Hanterar klick på "Logga in"-knappen. Navigerar användaren till LoginView.fxml.
     *
     * @param event Klick på login-knappen
     * @throws IOException Om fönsterbytet misslyckas
     */
    @FXML
    void login(ActionEvent event) throws IOException {
        String fxmlf = "LoginView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Hanterar klick på ett objekt i listan. Visar en varning om att inloggning krävs.
     *
     * @param event Klick på ett objekt i listan
     */
    @FXML
    void itemClicked(MouseEvent event) {
        // Sparar ned valdt item från ItemList till selectedItem
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        
        /* 
         * Om vi klickar på ett item kommer en alert dycka 
         * upp och berätta att man kan inte låna items om man inte loggat in
         */
        if (selectedItem != null) {
            // Visa varning om att inloggning krävs
            title = "Inloggning krävs";
            header = "Du måste vara inloggad";
            content = "Logga in för att kunna låna detta objekt.";
            alert.createAlert(title, header, content);
        }
    }

    /**
     * Initialiserar vyn:
     * - Sätter cellfabriken så att varje objekt visas med titel
     * - Laddar in alla objekt vid start
     * - Lägger till lyssnare för sökfältet så att sökresultaten uppdateras i realtid
     */
    @FXML
    public void initialize() {
        // Konfigurerar hur varje objekt visas i listan
        ItemList.setCellFactory(list -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); // toString() från Items visar titel/namn
                }
            }
        });

        // Hämta alla objekt från databasen (utan filter, ej lånebara)
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("", false);
        ItemList.getItems().setAll(allItems);

        // Lägg till söklyssnare – uppdatera listan vid varje teckenanvändning
        SearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Items> searchResults = searchItems.search(newValue, false);
            ItemList.getItems().setAll(searchResults);
        });
    }
}
