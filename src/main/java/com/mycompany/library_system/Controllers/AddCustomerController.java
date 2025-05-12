package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.CustomerMangement.AddCustomer;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.Customer;
import com.mycompany.library_system.Search.SearchCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;

/**
 * Controllerklass som hanterar skapandet och sökningen av kunder i bibliotekssystemet.
 * Hanterar gränssnittskomponenter och logik för att lägga till kunder i databasen.
 * 
 * Används tillsammans med AddCustomer.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddCustomerController {

    // Fält för användarens input
    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField telNr;

    @FXML
    private TextField password;

    @FXML
    private ToggleButton addNewCustomer;

    // Lista som visar existerande kunder
    @FXML
    private ListView<Customer> customerList;

    // Textfält för att söka efter kunder
    @FXML
    private TextField searchCustomers;

    /**
     * Initierar komponenterna när scenen laddas.
     * Sätter hur kunder visas i listan samt lyssnar på sökfältet för att filtrera i realtid.
     */
    @FXML
    public void initialize() {
        // Anpassad cell-rendering: visar toString()-representation av Customer-objekt
        customerList.setCellFactory(list -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText((empty || customer == null) ? null : customer.toString());
            }
        });

        // Live-sökning av kunder medan man skriver
        searchCustomers.textProperty().addListener((observable, oldValue, newValue) -> {
            SearchCustomer searchCustomer = new SearchCustomer();
            customerList.getItems().clear();
            customerList.getItems().addAll(searchCustomer.searchCustomer(newValue));
        });
    }

    /**
     * Skapar en ny kund och lägger till den i databasen.
     * Visar även en popup som bekräftelse.
     *
     * @param event Händelsen som triggas av att användaren klickar på "Lägg till kund"-knappen.
     */
    @FXML
    void addCustomer(ActionEvent event) {
        try {
            // Skapa kundobjekt från inputfält och lagra i databasen
            Customer customer = new Customer(
                firstName.getText(), 
                lastName.getText(), 
                Integer.parseInt(telNr.getText()), 
                email.getText(), 
                password.getText()
            );
            AddCustomer addCustomer = new AddCustomer();
            addCustomer.insertCustomer(customer);
        } catch (Exception e) {
            // Vid fel skrivs stacktrace ut – bör ersättas med ett användarvänligt felmeddelande
            e.printStackTrace();
        }
    }

    /**
     * Navigerar användaren tillbaka till startmenyn.
     *
     * @param event Händelsen som triggas av användarens klick på "Tillbaka"-knappen.
     */
    @FXML
    void backToMenu(ActionEvent event) {
        String fxmlf = "StartMenu.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
}
