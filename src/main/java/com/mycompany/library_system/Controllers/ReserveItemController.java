package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ReservationMangement.ReserveItem;
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
 * Controller-klass för hantering av reservationer i bibliotekssystemet.
 * Användare kan söka efter tillgängliga items (böcker, DVD:er etc.), välja ett datum och reservera dem.
 * Klassen använder en kombination av FXML-element och logikklasser för att hantera interaktionen.
 *
 * Funktioner:
 * - Visa lista över tillgängliga items utifrån valt datum och söktext
 * - Lägga till och ta bort items från en tillfällig "reservationslista"
 * - Genomföra reservationer
 * - Navigera tillbaka till kundmenyn
 * 
 * Fält från FXML:
 * - ItemList: Visar tillgängliga items att reservera
 * - itemReservationList: Visar tillfälligt valda items att reservera
 * - ScearchItem: Textfält för filtrering
 * - dateToReserve: DatePicker för att välja reservationsdatum
 *
 * Denna vy filtrerar bort kategorier med ID 4 och 5.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReserveItemController {
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ListView<Items> ItemList;

    @FXML
    private TextField ScearchItem;
    
    @FXML
    private DatePicker dateToReserve;

    @FXML
    private ListView<Items> itemReservationList;
    
    /**
     * Lägger till valt item från ItemList till reservationslistan.
     * Filtrerar om tillgängliga items efteråt.
     * 
     * @param event klickhändelse på item i ItemList
     */
    @FXML
    void addToReservation(MouseEvent event) {
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            itemReservationList.getItems().add(selectedItem);
            applyFilter();
        }
    }
    
    /**
     * Navigerar tillbaka till kundvyn.
     * 
     * @param event klickhändelse på "Tillbaka"-knappen
     */
    @FXML
    void backToMenu(ActionEvent event) {
        String fxmlf = "CustomerView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
     /**
     * Skapar reservationer för valda items i reservationslistan för inloggad användare.
     * Tömmer listan efter lyckad reservation.
     * 
     * @param event klickhändelse på "Reservera"-knappen
     */
    @FXML
    void makeReservation(ActionEvent event) {
        ReserveItem reserveItem = new ReserveItem();
        ArrayList<Items> itemsToReserve = new ArrayList<>(itemReservationList.getItems());
        Session session = Session.getInstance();
        LocalDate selectedDate = dateToReserve.getValue();

        if (reserveItem.addToReservationRows(session.getUserId(), itemsToReserve, selectedDate)) {
            itemReservationList.getItems().clear();
        }
    }
    
    /**
     * Tar bort ett item från reservationslistan.
     * Filtrerar om tillgängliga items efteråt.
     * 
     * @param event -> klickhändelse på item i itemReservationList
     */
    @FXML
    void removeFromReservation(MouseEvent event) {
        Items selectedItem = itemReservationList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            itemReservationList.getItems().remove(selectedItem);
            applyFilter();
        }
    }

    /**
     * Initierar kontrollerna när vyn laddas:
     * - Sätter cellrenderers
     * - Lyssnar på datum- och textförändringar
     * - Visar tillgängliga items vid start (om datum är valt)
     */
    @FXML
    public void initialize() {
        ItemList.setCellFactory(list -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.toString());
            }
        });

        dateToReserve.valueProperty().addListener((obs, oldDate, newDate) -> applyFilter());

        LocalDate selectedDate = dateToReserve.getValue();
        if (selectedDate != null) {
            SearchItems searchItems = new SearchItems();
            ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate, "");

            availableItems.removeAll(itemReservationList.getItems());
            availableItems.removeIf(item -> item.getCategoryID() == 4 || item.getCategoryID() == 5);

            ItemList.getItems().setAll(availableItems);
        }
        
        ScearchItem.textProperty().addListener((obs, oldText, newText) -> applyFilter());
    }


    /**
    * Filtrerar listan med tillgängliga objekt baserat på valt datum och sökterm.
    * Tar bort redan reserverade objekt och objekt med otillåtna kategorier.
    */
    private void applyFilter() {
        LocalDate selectedDate = dateToReserve.getValue();
        String search = ScearchItem.getText();
        if (selectedDate == null) return;

        System.out.println("Filtrerar för datum: " + selectedDate); // ✅ Testlogg

        SearchItems searchItems = new SearchItems();
        ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate, search);

        availableItems.removeAll(itemReservationList.getItems());
        availableItems.removeIf(item -> item.getCategoryID() == 4 || item.getCategoryID() == 5);
        ItemList.getItems().setAll(availableItems);
    }
}
