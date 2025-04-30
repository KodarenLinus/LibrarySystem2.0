/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddCustomer;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.Customer;
import com.mycompany.library_system.Search.SearchCustomer;
import com.mycompany.library_system.Utils.PopUpWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;


/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddCustomerController {
    
    
    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField telNr;

    @FXML
    private ToggleButton addNewCustomer;
    
    @FXML
    private ListView<Customer> customerList;
    
    @FXML
    private TextField searchCustomers;
    
    @FXML 
    private TextField password;
    
    
    @FXML
    public void initialize() {
        // Gör så att vi visar titlen på våra items!!!!
        customerList.setCellFactory(list -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (empty || customer == null) {
                    setText(null);
                } else {
                    setText(customer.toString()); 
                }
            }
        });

        // Updaterar våran customer lista i real tid!!!
        searchCustomers.textProperty().addListener((observable, oldValue, newValue) -> {
            SearchCustomer searchCustomer = new SearchCustomer();
            customerList.getItems().clear();
            customerList.getItems().addAll(searchCustomer.searchCustomer(newValue));
        });
    }
    
    
    @FXML
    void addCustomer(ActionEvent event) {
        

        try {
        // Ladda FXML-filen för popupen
        PopUpWindow popUpWindow = new PopUpWindow();
        String fxmlf = "newDVDPop.fxml";
        popUpWindow.popUp(event, fxmlf);

        AddCustomer addCustomer = new AddCustomer();
        Customer customer = new Customer(firstName.getText(), lastName.getText(), Integer.parseInt(telNr.getText()), email.getText(), password.getText());
        addCustomer.insertCustomer(customer);
        } catch (Exception e) {
        e.printStackTrace();
    
    }
}
    
    
    @FXML
    void backToMenu(ActionEvent event) {
        
        String fxmlf = "StartMenu.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
}
    

