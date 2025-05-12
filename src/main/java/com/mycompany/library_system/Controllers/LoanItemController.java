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
 * Samverkar med LoanItem.fxml.
 * 
 * Funktioner:
 * - Söka efter objekt
 * - Filtrera referenslitteratur och magasin
 * - Visa reservationsstatus
 * - Lägga till och ta bort från kundvagn
 * - Skapa lån
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoanItemController {

    // AlertHandler-instans för att visa varningsmeddelanden
    private AlertHandler alertHandler = new AlertHandler();

    // Texter för varningspopup
    private String title;
    private String header; 
    private String content;

    @FXML
    private ListView<Items> ItemList; // Lista med tillgängliga objekt

    @FXML
    private TextField ScearchItem; // Textfält för att söka objekt

    @FXML
    private ListView<Items> itemCartList; // Lista med valda objekt att låna

    @FXML
    private RadioButton magazineFilterButton; // Filter för magasin

    @FXML
    private RadioButton referensFilterButton; // Filter för referenslitteratur

    /**
     * Filtrerar listan när magasin-filter aktiveras.
     * 
     * @param event -> en radiobutton som när den är aktiv filtrerar bort tidskrifter
     */
    @FXML
    void FilterMagazine(ActionEvent event) {
        applyFilter();
    }

    /**
     * Filtrerar listan när referenslitteratur-filter aktiveras.
     * 
     * @param event -> en radiobutton som när den är aktiv filrerar bort referenslitteratur
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
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        final int REFERENSLITTERATUR = 4;
        final int MAGAZINES = 5;

        if (selectedItem != null) {
            int categoryId = selectedItem.getCategoryID();
            if (categoryId == REFERENSLITTERATUR || categoryId == MAGAZINES) {
                title = "Ej tillåtet";
                header ="Kan inte läggas till"; 
                content = (selectedItem.getCategoryName() + " är inte tillåten i kundvagnen.");
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
     * Tar bort ett objekt från kundvagnen när det klickas.
     *
     * @param event Klick på ett objekt i kundvagnen
     */
    @FXML
    void removeFromCart(MouseEvent event) {
        Items selectedItem = itemCartList.getSelectionModel().getSelectedItem();

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
        String fxmlf = "CustomerView.fxml";
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
        LoanItem loanItem = new LoanItem();
        ArrayList<Items> itemsToLoan = new ArrayList<>(itemCartList.getItems());
        Session session = Session.getInstance();

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

                    String dateInfo = (reservationDate != null && reservationDate.isAfter(LocalDate.now())) ?
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
        String searchTerm = ScearchItem.getText();
        SearchItems searchItems = new SearchItems();
        ArrayList<Items> allItems = searchItems.search(searchTerm, true);
        allItems.removeAll(itemCartList.getItems());

        ArrayList<Items> filteredItems = new ArrayList<>();
        for (Items item : allItems) {
            if (magazineFilterButton.isSelected() && item.getCategoryID() == 5) {
                continue;
            }
            if (referensFilterButton.isSelected() && item.getCategoryID() == 4) {
                continue;
            }
            filteredItems.add(item);
        }

        ItemList.getItems().setAll(filteredItems);
    }
}

