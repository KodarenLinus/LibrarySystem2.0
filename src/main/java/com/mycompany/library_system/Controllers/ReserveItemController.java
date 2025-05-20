package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ReservationMangement.ReserveItem;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.CategoryType;
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
 * 
 * Fungerar tillsammans med ReserveItem.fxml
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
        // Spara ned ett valt Item från ItemList
        Items selectedItem = ItemList.getSelectionModel().getSelectedItem();
        
        // Om vi valt ett item flyttar vi det till itemReservationList
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
        String fxmlf = "CustomerStart.fxml";
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
        // skapar ett ReserveItem Objekt
        ReserveItem reserveItem = new ReserveItem();
        
        // Skapar en List med de item som vi vill reservera
        ArrayList<Items> itemsToReserve = new ArrayList<>(itemReservationList.getItems());
        
        // Hämtar nuvarande Session
        Session session = Session.getInstance();
        
        // Sparar ned valdt datum från dateToReserve i selectedDate
        LocalDate selectedDate = dateToReserve.getValue();
        
        // Kollar att reservationen går igenom. Om den lyckas tas Itemsen bort från itemReservationList
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
        // Spara ned valdt item från itemReservationList till selectedItem
        Items selectedItem = itemReservationList.getSelectionModel().getSelectedItem();
        
        // Kollar att vi valdt ett item och tar bort det
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
        // Intierar ItemList
        ItemList.setCellFactory(list -> new ListCell<Items>() {
            @Override
            protected void updateItem(Items item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.toString());
            }
        });
        
        // Applicerar filter på valt datum
        dateToReserve.valueProperty().addListener((obs, oldDate, newDate) -> applyFilter());
        
        // Sparar ned valdt datum från dateToReserve i selectedDate
        LocalDate selectedDate = dateToReserve.getValue();
        
        /*
         * Om vi inte valt ett datum kommer ItemList vara tom.
         * Har vi valt ett datum kommer listan enbart visa items som är tillgängliga
         */
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
        // Sparar ned valdt datum från dateToReserve i selectedDate
        LocalDate selectedDate = dateToReserve.getValue();
        
        // Spara ned sök text från ScearchItem till search
        String search = ScearchItem.getText();
        
        // Om vi inte har ett datum så returnerar vi inga items
        if (selectedDate == null) return;
        
        // Skapar ett searchItems objekt
        SearchItems searchItems = new SearchItems();
        
        // Skapar en Items ArrayList och söker i metoden searchAvailableItems och spara ned den listan
        ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate, search);
        
        // Tar bort alla items i itemReservationList
        availableItems.removeAll(itemReservationList.getItems());
        
        // Tar bort alla tidskrifter och referens kopior
        availableItems.removeIf(item -> item.getCategoryID() == CategoryType.REFRENCE_COPY.getId() 
                || item.getCategoryID() == CategoryType.MAGAZINE.getId());
        
        // ItemList innehåller nu enbart tillgängliga items
        ItemList.getItems().setAll(availableItems);
    }
}
