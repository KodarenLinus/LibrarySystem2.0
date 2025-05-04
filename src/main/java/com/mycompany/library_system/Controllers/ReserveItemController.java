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
 * Controller för att reservera items.
 * För teständamål visar vi bara tillgängliga items från searchAvailableItems().
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
    void addToReservation(MouseEvent event) {
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

        if (reserveItem.addToReservationRows(session.getUserId(), itemsToReserve, selectedDate)) {
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
     * Initierar vyn – för test visar vi bara resultat från searchAvailableItems().
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
            ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate);

            availableItems.removeAll(itemReservationList.getItems());
            availableItems.removeIf(item -> item.getCategoryID() == 4 || item.getCategoryID() == 5);

            ItemList.getItems().setAll(availableItems);
        }
    }


    private void applyFilter() {
        LocalDate selectedDate = dateToReserve.getValue();
        if (selectedDate == null) return;

        System.out.println("Filtrerar för datum: " + selectedDate); // ✅ Testlogg

        SearchItems searchItems = new SearchItems();
        ArrayList<Items> availableItems = searchItems.searchAvailableItems(selectedDate);

        availableItems.removeAll(itemReservationList.getItems());
        availableItems.removeIf(item -> item.getCategoryID() == 4 || item.getCategoryID() == 5);
        ItemList.getItems().setAll(availableItems);
    }
}
