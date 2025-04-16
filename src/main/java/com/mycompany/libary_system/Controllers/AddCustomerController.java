/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Controllers;

import com.mycompany.libary_system.Logic.AddCustomer;
import com.mycompany.libary_system.Utils.ChangeWindow;
import com.mycompany.libary_system.Models.Customer;
import com.mycompany.libary_system.Search.SearchCustomer;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 *
 * @author emildahlback
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newDVDPop.fxml"));
        Parent root = loader.load();  // Här laddas rootkomponenten från FXML-filen

        // Skapa en ny Stage (popup)
        Stage stage = new Stage();
        stage.setTitle("Popup Title");

        // Sätt scenen för popupen
        stage.setScene(new Scene(root));

        // Visa popupen och vänta på att den stängs
        stage.showAndWait();

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
    

