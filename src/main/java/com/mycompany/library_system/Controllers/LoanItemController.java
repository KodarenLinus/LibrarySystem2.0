/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.GetReservationDate;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Logic.LoanItem;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Utils.AlertHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
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
    }

    @FXML
    void FilterReferensBook(ActionEvent event) {
         applyFilter();
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
                applyFilter();
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
            applyFilter();
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

        // Skapar ett lån utifrån vad vi har i kundvagnen!!!
        LoanItem loanItem = new LoanItem();
        ArrayList<Items> itemsToLoan = new ArrayList<>(itemCartList.getItems());
        Session session = Session.getInstance();
        
        // Kör loan item och kollar att lånets genomförs, om de inte körs kommer våran kundvagn inte tömmas.
        if (loanItem.addToLoanRows(session.getUserId(), itemsToLoan) == true)
        {
            itemCartList.getItems().clear();
        }
    }
    
    /**
    * Initierar vyn när den laddas. Ställer in hur objekt listas, hämtar alla objekt och lägger till sökfunktionalitet.
    */
    @FXML
    public void initialize() throws SQLException  {
        
        GetReservationDate getReservationDate = new GetReservationDate();
        
        // Visar titel för varje objekt i listan
        ItemList.setCellFactory(list -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    LocalDate reservationDate = null;
                    try {
                        reservationDate = getReservationDate.getReservationDateForItem(item.getItemID());
                    } catch (SQLException ex) {
                        Logger.getLogger(ReserveItemController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String dateInfo = (reservationDate != null && reservationDate.isAfter(LocalDate.now())) ? " (Reserverad: " + reservationDate + ")" : " (Ej reserverad)";
                    setText(item.toString() + dateInfo);
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
    * 
    * kollar om filter är applicerad och uppdaterar listan med items.
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
