package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.CustomerMangement.AddCustomer;
import com.mycompany.library_system.Logic.CustomerMangement.GetCustomerCategory;
import com.mycompany.library_system.Logic.CustomerMangement.UpdateCustomer;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.Customer;
import com.mycompany.library_system.Models.CustomerCategory;
import com.mycompany.library_system.Search.SearchCustomer;
import com.mycompany.library_system.Utils.AlertHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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
    
    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ComboBox<CustomerCategory> Category;
    
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
    private ListView<Customer> customerList;

    @FXML
    private TextField searchCustomers;
    
    /**
     * Försöker skapa och lägga till en ny kund efter att ha validerat fälten
     * Om en kund med samma mail finns så updateras den kunden.
     * Visar en alert vid lyckad registrering eller om något fält är felaktigt ifyllt.
     * 
     * @param event -> Händelse för när man klickar på "Lägg till/ uppdatera kund ny kund"
     */
    @FXML
    void addNewCustomerOrUpdate(ActionEvent event) {
        System.out.println("test");
        CustomerCategory selectedCategory = Category.getValue();

        try {
            // Skapa ett kundobjekt från inputfält
            Customer customer = new Customer(
                firstName.getText(), 
                lastName.getText(), 
                Integer.parseInt(telNr.getText()), 
                email.getText(), 
                password.getText(),
                selectedCategory.getCategoryID(),
                selectedCategory.getCategoryName()
            );

            // Försök att uppdatera kunden, annars lägg till den
            UpdateCustomer updater = new UpdateCustomer();
            boolean updated = updater.updateCustomerIfExists(customer);

            if (updated) {
                title = "Uppdaterad";
                header = "Kundinformation uppdaterad"; 
                content = "Befintlig kund har uppdaterats.";
            } else {
                AddCustomer adder = new AddCustomer();
                adder.insertCustomer(customer);

                title = "Lyckades";
                header = "Kund har lagts till"; 
                content = "Grattis, du har lagt till en ny kund i systemet.";
            }

            alert.createAlert(title, header, content);

            // Uppdatera kundlistan i gränssnittet
            loadCustomerList();

            // (valfritt) Rensa formuläret
            clearFields();

        } catch (NumberFormatException e) {
            title = "Ej tillåten input";
            header = "Telefonnummer måste vara heltal"; 
            content = "Du skrev in ogiltiga tecken i telefonnummerfältet. Använd endast siffror.";
            alert.createAlert(title, header, content);
        }
    }

    
    /**
     * Initierar komponenterna när scenen laddas.
     * Sätter hur kunder visas i listan samt lyssnar på sökfältet för att filtrera i realtid.
     */
    @FXML
    public void initialize() throws SQLException {
        customerList.setCellFactory(list -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText((empty || customer == null) ? null : customer.toString());
            }
        });

        searchCustomers.textProperty().addListener((observable, oldValue, newValue) -> {
            SearchCustomer searchCustomer = new SearchCustomer();
            customerList.getItems().clear();
            customerList.getItems().addAll(searchCustomer.searchCustomer(newValue));
        });

        customerList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedCustomer) -> {
            if (selectedCustomer != null) {
                try {
                    populateFieldsWithCustomer(selectedCustomer);
                } catch (SQLException ex) {
                    Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Category.setCellFactory(list -> new ListCell<CustomerCategory>() {
            @Override
            protected void updateItem(CustomerCategory category, boolean empty) {
                super.updateItem(category, empty);
                setText((empty || category == null) ? null : category.toString());
            }
        });

        // Sätt kategorier
        GetCustomerCategory getCustomerCategory = new GetCustomerCategory();
        ArrayList<CustomerCategory> allPublisher = getCustomerCategory.getAllCustomerCategory();
        Category.getItems().setAll(allPublisher);

        // Ladda kundlistan initialt
        loadCustomerList();
    }

    /**
     * Navigerar användaren tillbaka till startmenyn.
     *
     * @param event Händelsen som triggas av användarens klick på "Tillbaka"-knappen.
     */
    @FXML
    void backToMenu(ActionEvent event) {
        String fxmlf = "StaffStart.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    /**
     * Om man väljer en kund så hämtar den data från kunden
     * 
     * @param customer 
     */
    private void populateFieldsWithCustomer(Customer customer) throws SQLException {
        firstName.setText(customer.getFirstName());
        lastName.setText(customer.getLastName());
        email.setText(customer.getEmail());
        telNr.setText(String.valueOf(customer.getTelNr()));
        password.setText(String.valueOf(customer.getPassword()));
        
        GetCustomerCategory getCustomerCategory = new GetCustomerCategory();
        CustomerCategory category = getCustomerCategory.getCustomerCategoryByID(customer.getCategoryID());
        Category.setValue(category);
    }
    
    /**
     * Laddar in kunder
     * 
     */
    private void loadCustomerList() {
        SearchCustomer searchCustomer = new SearchCustomer();
        customerList.getItems().setAll(searchCustomer.searchCustomer(""));
    }
    
    /**
     * Tömmer fälten
     * 
     */
    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
        telNr.clear();
        password.clear();
        Category.setValue(null);
    }
}
