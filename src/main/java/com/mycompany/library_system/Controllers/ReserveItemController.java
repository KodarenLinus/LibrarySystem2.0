/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ReserveItem;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Utils.ChangeWindow;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReserveItemController {
        @FXML
    private ListView<Items> ItemList;

    @FXML
    private TextField ScearchItem;
    
    @FXML
    private DatePicker dateToReserve;

    @FXML
    private ListView<Items> itemReservationList;

    @FXML
    void addToReservation (MouseEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            itemReservationList.getItems().add(selectedItem);
            applyFilter();
        }
    }

    @FXML
    void backToMenu(ActionEvent event) {
        String fxmlf = "CustomerView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    @FXML
    void makeReservation(ActionEvent event) {
        ReserveItem reserveItem = new ReserveItem();
        ArrayList<Items> itemsToReserve = new ArrayList<>(itemReservationList.getItems());
        Session session = Session.getInstance();
        LocalDate selectedDate = dateToReserve.getValue();
        
         // Kör loan item och kollar att lånets genomförs, om de inte körs kommer våran kundvagn inte tömmas.
        if (reserveItem.addToReservationRows(session.getUserId(), itemsToReserve, selectedDate) == true)
        {
            itemReservationList.getItems().clear();
        }
    }

    @FXML
    void removeFromReservation(MouseEvent event) {
        Items selectedItem = itemReservationList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            itemReservationList.getItems().remove(selectedItem);
            applyFilter();
        }
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

        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("");

        // Lägg till null-kontroll
        LocalDate selectedDate = dateToReserve.getValue();
        if (selectedDate != null) {
            ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate);
            allItems.retainAll(availableItems);
        }

        allItems.removeAll(itemReservationList.getItems());
        allItems.removeIf(item -> item.getCategoryID() == 4 || item.getCategoryID() == 5);
        ItemList.getItems().setAll(allItems);

        // Lyssna på sökfältet
        ScearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Items> searchResults = searchItems.search(newValue);
            searchResults.removeAll(itemReservationList.getItems());
            ItemList.getItems().setAll(searchResults);
        });

        // Lägg till en lyssnare som uppdaterar listan när datumet ändras
        dateToReserve.valueProperty().addListener((obs, oldDate, newDate) -> {
            applyFilter();
        });
    }
    
    private void applyFilter() {
        String searchTerm = ScearchItem.getText();
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search(searchTerm);
        LocalDate selectedDate = dateToReserve.getValue();

        if (selectedDate != null) {
            ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate);
            allItems.retainAll(availableItems);
        }

        allItems.removeAll(itemReservationList.getItems());
        allItems.removeIf(item -> item.getCategoryID() == 4 || item.getCategoryID() == 5);
        ItemList.getItems().setAll(allItems);
    }
    
}
