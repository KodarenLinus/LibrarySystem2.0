/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.CustomerMangement;

import com.mycompany.library_system.Models.Customer;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Klass som hanterar logiken för att lägga till kunder i databasen.
 * Skickar ett Customer-objekt till databasen via SQL.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddCustomer {
    
    private final DatabaseConnector dbConnector;

    public AddCustomer () {
        this.dbConnector = new ConnDB();
    }
    
    /**
     * Lägger till en customer i databasen
     *
     * @param Customer objekt som vi skickar till databasen
     */
    public void insertCustomer(Customer customer){
        
        // Skapar en databasanslutning
        Connection conn = dbConnector.connect();
        
        // SQL-fråga för att infoga en ny kund
        String insertCustomer = "INSERT INTO Customer (CustomerCategoryID, CategoryName, FirstName, LastName, Email, TelNumber, PasswordCustomer) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
             PreparedStatement insertCustomerStmt = conn.prepareStatement(insertCustomer);   
        ){
            // Sätter värden i frågan
            insertCustomerStmt.setInt(1, customer.getCategoryID());  
            insertCustomerStmt.setString(2, customer.getCategoryName());
            insertCustomerStmt.setString(3, customer.getFirstName());
            insertCustomerStmt.setString(4, customer.getLastName());
            insertCustomerStmt.setString(5, customer.getEmail());
            insertCustomerStmt.setInt(6, customer.getTelNr());
            insertCustomerStmt.setString(7, customer.getPassword());
            
            // Utför insättningen
            insertCustomerStmt.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace(); 
        }
    }
    

}
