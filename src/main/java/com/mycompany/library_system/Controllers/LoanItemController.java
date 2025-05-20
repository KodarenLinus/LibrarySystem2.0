/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ReservationMangement.GetReservationDate;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Logic.LoanMangement.LoanItem;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.CategoryType;
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
 * Controller-klass för att hantera utlåning av objekt i bibliotekssystemet.
 * Användare kan söka efter objekt, filtrera, lägga till i kundvagn och genomföra utlåning.
 * 
 * Fungerar tillsammans med LoanItem.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoanItemController {

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
    private TextField ScearchItem;

    @FXML
    private ListView<Items> itemCartList;

    @FXML
    private RadioButton magazineFilterButton;

    @FXML
    private RadioButton referensFilterButton;

    /**
     * Filtrerar listan när magasin-filter aktiveras.
     * 
     * @param event en radiobutton som när den är aktiv filtrerar bort tidskrifter
     */
    @FXML
    void FilterMagazine(ActionEvent event) {
        applyFilter();
    }

    /**
     * Filtrerar listan när referenslitteratur-filter aktiveras.
     * 
     * @param event en radiobutton som när den är aktiv filrerar bort referenslitteratur
     */
    @FXML
    void FilterReferensBook(ActionEvent event) {
        applyFilter();
    }

    /**
     * Lägger till valt objekt i kundvagnen om det är giltigt och inte redan tillagt.
     * Visar varning om det är referenslitteratur eller magasin.
     *
     * @param event Klick på ett objekt i objektlistan
     */
    @FXML
    void addToCart(MouseEvent event) {
        // Spara ner värde från ItemList som vi valt
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        
        // ID för tidskrift och referenslitratur
        final int REFERENSLITTERATUR = CategoryType.REFRENCE_COPY.getId();
        final int MAGAZINES = CategoryType.MAGAZINE.getId();
        
        /* 
         * Kollar att vi valdt något. 
         * Om vi valt något så kommer det läggas i våran kundvagn så länge det inte är
         * En tidsskirft eller Referense kopia. Är det otilåtet att låna kommer användaren att informeras
         * Om vi inte valt något så kommer inget att hända
         */
        if (selectedItem != null) {
            int categoryId = selectedItem.getCategoryID();
            
            // Kollar om det är en tidsskift eller en referense kopia
            if (categoryId == REFERENSLITTERATUR || categoryId == MAGAZINES) {
                title = "Ej tillåtet";
                header ="Kan inte läggas till"; 
                content = (selectedItem.getCategoryName() + " är inte tillåten i kundvagnen.");
                alert.createAlert(title, header, content);
                return;
            }
            
            // Lägger till valt item i kundvagn och tar bort från ItemList
            if (!itemCartList.getItems().contains(selectedItem)) {
                itemCartList.getItems().add(selectedItem);
                applyFilter();
            }
        }
    }

    /**
     * Tar bort ett objekt från kundvagnen när det klickas.
     *
     * @param event Klick på ett objekt i kundvagnen
     */
    @FXML
    void removeFromCart(MouseEvent event) {
        // Spara ner värde från ItemCartList som vi valt
        Items selectedItem = itemCartList.getSelectionModel().getSelectedItem();
        
         /* 
         * Kollar att vi valdt något. 
         * Om vi valt något så kommer det tas bort från våran kundvagn
         */
        if (selectedItem != null) {
            itemCartList.getItems().remove(selectedItem);
            applyFilter();
        }
    }

    /**
     * Går tillbaka till huvudmenyn för kunden.
     *
     * @param event Klick på "Tillbaka"-knappen
     * @throws IOException Om något går fel vid laddning av FXML
     */
    @FXML
    void backToMenu(ActionEvent event) {
        String fxmlf = "CustomerStart.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Skapar ett lån baserat på objekt i kundvagnen och tömmer vagnen om lyckat.
     *
     * @param event Klick på "Låna"-knappen
     * @throws IOException Om något går fel vid hantering av vyn
     */
    @FXML
    void makeOrder(ActionEvent event) {
        // LoanItem objekt
        LoanItem loanItem = new LoanItem();
        
        // Spara ner våran kundvagn i en items Arraylist
        ArrayList<Items> itemsToLoan = new ArrayList<>(itemCartList.getItems());
        
        // Hämtar nuvarande användar Session 
        Session session = Session.getInstance();
        
        // Om vårat lån går igenom töms våran kundvagn.
        if (loanItem.addToLoanRows(session.getUserId(), itemsToLoan)) {
            itemCartList.getItems().clear();
        }
    }

    /**
     * Initierar listan med objekt, sätter upp cellrendering för reservationsstatus
     * och implementerar sökfunktionalitet med filtrering.
     */
    @FXML
    public void initialize() {
        GetReservationDate getReservationDate = new GetReservationDate();

        // Anpassar celler i objektlistan
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
                    
                    //Sätter ut att item är reserverat om de är inom lån tid.
                    String dateInfo = (reservationDate != null && (reservationDate.isAfter(LocalDate.now()) || reservationDate.isEqual(LocalDate.now()))) ?
                            " (Reserverad: " + reservationDate + ")" : " (Ej reserverad)";
                    setText(item.toString() + dateInfo);
                }
            }
        });

        // Laddar objekt initialt
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search("", true);
        allItems.removeAll(itemCartList.getItems());
        ItemList.getItems().setAll(allItems);

        // Sätter upp live-sökning
        ScearchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Items> searchResults = searchItems.search(newValue, true);
            searchResults.removeAll(itemCartList.getItems());
            ItemList.getItems().setAll(searchResults);
        });
    }

    /**
     * Använder aktuell sökterm och filterinställningar för att uppdatera visade objekt.
     */
    private void applyFilter() {
        // Hämtar värde från SearchItem och spara ner de
        String searchTerm = ScearchItem.getText();
        
        // Skapar ett searchItems objekt och skapar en lista utefrån all matchar våran sökning får i metoden search
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search(searchTerm, true);
        
        // ta bort alla items som ligger i vår kundvagn
        allItems.removeAll(itemCartList.getItems());
        
        // Ny arrayLista
        ArrayList<Items> filteredItems = new ArrayList<>();
        
        // Kollar ifall vi filtrerat bort Tidskrifter och Referense kopior
        for (Items item : allItems) {
            if (magazineFilterButton.isSelected() && item.getCategoryID() == CategoryType.MAGAZINE.getId()) {
                continue;
            }
            if (referensFilterButton.isSelected() && item.getCategoryID() == CategoryType.REFRENCE_COPY.getId()) {
                continue;
            }
            // Ny filtrerad ArrayList
            filteredItems.add(item);
        }
        
        // ItemList innehåller filteredItems
        ItemList.getItems().setAll(filteredItems);
    }
}

