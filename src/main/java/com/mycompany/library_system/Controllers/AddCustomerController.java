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

    // Textfält för att mata in förnamn
    @FXML
    private TextField firstName;

    // Textfält för att mata in efternamn
    @FXML
    private TextField lastName;

    // Textfält för att mata in telefonnummer
    @FXML
    private TextField telNr;

    // Textfält för att mata in lösenord
    @FXML
    private TextField password;

    // Växlingsknapp för att lägga till en ny kund
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
        
        // Lägg till lyssnare för när man klickar på en kund
        customerList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedCustomer) -> {
        if (selectedCustomer != null) {
            populateFieldsWithCustomer(selectedCustomer);
        }
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
        System.out.println("test");
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
    
    private void populateFieldsWithCustomer(Customer customer) {
    firstName.setText(customer.getFirstName());
    lastName.setText(customer.getLastName());
    email.setText(customer.getEmail());
    telNr.setText(String.valueOf(customer.getTelNr()));
    password.setText(String.valueOf(customer.getPassword()));
}
}
