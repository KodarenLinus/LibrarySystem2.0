/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;

/**
 *
 * @author emildahlback
 */
public class AddCustomer {
    
    
    void insertCustomer(Customer customer){
    
    DatabaseConnector connDB = new ConnDB();
    Connection conn = connDB.connect();

    try {
        PreparedStatement stmt1 = conn.prepareStatement(
            "INSERT INTO Customer (CustomerID, CustomerCategoryID, CategoryName, FirstName, LastName, Email, Adress, TelNumber, UserID, PasswordCustomer) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        stmt1.setNull(1, java.sql.Types.INTEGER); // Du kan generera unikt ID i databasen istället
        stmt1.setInt(2, 10);  // Exempel: Staff
        stmt1.setString(3, "Studenet");
        stmt1.setString(4, customer.getFirstName());
        stmt1.setString(5, customer.getLastName());
        stmt1.setString(6, customer.getEmail());
        stmt1.setString(7, "ltu"); // Du kan byta till riktig adress senare
        stmt1.setInt(8, customer.getTelNr());
        stmt1.setInt(9, 2); // T.ex. användar-ID om du har inloggning
        stmt1.setString(10, customer.getPassword());

        stmt1.executeUpdate();

    } catch (SQLException ex){
        ex.printStackTrace(); // Bra att skriva ut för felsökning
    }
}
}
