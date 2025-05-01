/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

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
    
    /**
     * Lägger till en customer i databasen
     *
     * @param Ett Customer objekt som vi skickar till databasen
     */
    public void insertCustomer(Customer customer){
        
        // Skapar en databasanslutning
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // SQL-fråga för att infoga en ny kund
        String insertCustomer = "INSERT INTO Customer (CustomerCategoryID, CategoryName, FirstName, LastName, Email, Adress, TelNumber, PasswordCustomer) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
             PreparedStatement stmt1 = conn.prepareStatement(insertCustomer);   
        ){
            // Sätter värden i frågan
            stmt1.setInt(1, 3);  // Exempel: Staff: @ToDo fixa så att vi hämtar categorier från db.
            stmt1.setString(2, "Studenet");
            stmt1.setString(3, customer.getFirstName());
            stmt1.setString(4, customer.getLastName());
            stmt1.setString(5, customer.getEmail());
            stmt1.setString(6, "ltu"); // Du kan byta till riktig adress senare
            stmt1.setInt(7, customer.getTelNr());
            stmt1.setString(8, customer.getPassword());
            
            // Utför insättningen
            stmt1.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace(); // Bra att skriva ut för felsökning
        }
    }
}
